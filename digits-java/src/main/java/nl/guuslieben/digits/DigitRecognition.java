package nl.guuslieben.digits;

import org.deeplearning4j.datasets.iterator.impl.EmnistDataSetIterator;
import org.deeplearning4j.datasets.iterator.impl.EmnistDataSetIterator.Set;
import org.deeplearning4j.datasets.iterator.impl.MnistDataSetIterator;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.ConvolutionLayer;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.conf.layers.SubsamplingLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.IOException;

@SuppressWarnings("TypeMayBeWeakened")
public class DigitRecognition {

    private static final int OUTPUT = 10;
    private static final int BATCH_SIZE = 256;
    private static final int EPOCHS = 25;
    private static final int SEED = 123;
    private static final int IMAGE_WIDTH = 28;
    private static final int IMAGE_HEIGHT = 28;


    public static void main(String[] args) throws IOException {
        var emnistSet = EmnistDataSetIterator.Set.BALANCED;
        var emnistTrain = new MnistDataSetIterator(BATCH_SIZE, true, SEED);
        var emnistTest = new MnistDataSetIterator(BATCH_SIZE, false, SEED);
        System.out.println("Preparing model...");
        var model = getModel(emnistSet);
        System.out.println("Training...");
        var network = train(model, emnistTrain);
        System.out.println("Evaluating...");
        evaluate(network, emnistTest);
    }

    public static MultiLayerNetwork getModel(Set emnistSet) {
        var conf = new NeuralNetConfiguration.Builder()
                .seed(SEED)
                .l2(0.0005)
                .weightInit(WeightInit.XAVIER)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .list()
                .layer(0, new ConvolutionLayer.Builder(5, 5)
                        //nIn and nOut specify depth. nIn here is the nChannels and nOut is the number of filters to be applied
                        .nIn(1)
                        .stride(1, 1)
                        .nOut(20)
                        .activation(Activation.RELU)
                        .build())
                .layer(1, new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX)
                        .kernelSize(2, 2)
                        .stride(2, 2)
                        .build())
                .layer(2, new ConvolutionLayer.Builder(5, 5)
                        .stride(1, 1)
                        .nOut(50)
                        .activation(Activation.RELU)
                        .build())
                .layer(3, new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX)
                        .kernelSize(2, 2)
                        .stride(2, 2)
                        .build())
                .layer(4, new DenseLayer.Builder().activation(Activation.RELU)
                        .nOut(500).build())
                .layer(5, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .nOut(OUTPUT)
                        .activation(Activation.SOFTMAX)
                        .build())
                .setInputType(InputType.convolutionalFlat(IMAGE_HEIGHT, IMAGE_WIDTH, 1)) //See note below
                .build();

        MultiLayerNetwork model = new MultiLayerNetwork(conf);
        model.init();
        model.setListeners(new ScoreIterationListener(5));
        return model;
    }

    public static MultiLayerNetwork train(MultiLayerNetwork model, DataSetIterator emnistTrain) {
        for (int i = 0; i < EPOCHS; i++) {
            System.out.println("Epoch: " + i);
            model.fit(emnistTrain);
        }
        return model;
    }

    private static void evaluate(MultiLayerNetwork network, DataSetIterator emnistTest) {
        // evaluate basic performance
        var eval = network.evaluate(emnistTest);
        System.out.println(eval.accuracy());
        System.out.println(eval.precision());
        System.out.println(eval.recall());

        // evaluate ROC and calculate the Area Under Curve
        var roc = network.evaluateROCMultiClass(emnistTest, 0);
        roc.calculateAUC(0);

        // optionally, you can print all stats from the evaluations
        System.out.print(eval.stats());
        System.out.print(roc.stats());
    }

}
