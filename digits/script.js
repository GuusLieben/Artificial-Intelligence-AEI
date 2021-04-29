import {MnistData} from './data.js';

document.addEventListener('DOMContentLoaded', run);

const BASELINE = true;

const EPOCHS = BASELINE ? 1 : 25;
const BATCH_SIZE = BASELINE ? 128 : 512;
const TRAIN_DATA_SIZE = BASELINE ? 500 : 5500; // For performance, keep this below 7500
const TEST_DATA_SIZE = 1000; // Test data doesn't matter much as this is sampled anyway (and if trained properly the results should be accurate ;))

/* ==========================================
 Running the application.
  - Collects the datasets
  - Creates the NN model
  - Trains the model
  - Tests the model for accuracy, loss, and confusion
 ========================================== */
async function run() {
  // Load data from the MNIST database (http://yann.lecun.com/exdb/mnist/)
  const data = new MnistData();
  await data.load();

  // Create a NN model
  const model = getModel();

  // Train and test our model
  await train(model, data);

  // Show the accuracy/loss and confusion matrix
  await showAccuracy(model, data);
  await showConfusion(model, data);
}


/* ==========================================
 Creating the neural network model
  - Defines the input image specifications
  - 1) Shifts bits based on their variance
  - 2) Pools the output (downsampling)
  - 3) Repeat 1 and 2
  - 4) Flattens 2d layers (arrays) to 1d vector (array)
  - 5) Splits into 10 output classes
 ========================================== */
function getModel() {
  const model = tf.sequential();

  // Models in MNIST data are 28x28 (px) and black-and-white (1 color channel)
  const IMAGE_WIDTH = 28;
  const IMAGE_HEIGHT = 28;
  const IMAGE_CHANNELS = 1;

  // The network contains two convolution functions, each shifting bits in the image based one its brightness.
  // Learned more about it here: https://setosa.io/ev/image-kernels/
  model.add(tf.layers.conv2d({
    inputShape: [IMAGE_WIDTH, IMAGE_HEIGHT, IMAGE_CHANNELS], // Images drawArea 28x28 pixels and contain only black and white (1 channel; white for not white)

    // Kernels are applied for filter windows, here the image is walked through with a 5x5 block, for 8 times
    kernelSize: 5, // The size of filter windows, here 5 indicates a square window sized 5x5 (px)
    filters: 8, // Amount of filters (which are the size of kernelSize)

    // Each iteration we change 1
    strides: 1, // How much can we switch each iteration?

    activation: 'relu', // Returns the original value if it is positive, or zero. This is is a common default activation

    kernelInitializer: 'varianceScaling' // Variance indicates the expected deviation of a random variable from the mean value, here we scale the filter windows to find deviations
  }));

  // Acts as a sort of downsampling using max values instead of averaging. E.g.:
  // [[3, 10]
  //  [5, -1]]
  // is pooled to [10] (max) instead of 4,25 (average (3+10+5-1=17, 17/4=4,25))
  // Learned more about it here: https://computersciencewiki.org/index.php/Max-pooling_/_Pooling
  model.add(tf.layers.maxPooling2d({poolSize: [2, 2], strides: [2, 2]}));

  // Repeat previous 2 steps
  model.add(tf.layers.conv2d({
    kernelSize: 5, filters: 16, strides: 1,
    activation: 'relu', kernelInitializer: 'varianceScaling'
  }));
  model.add(tf.layers.maxPooling2d({poolSize: [2, 2], strides: [2, 2]}));

  // Flattens 2d layers into 1d vector (comparable to arrays)
  model.add(tf.layers.flatten());

  // Our last layer is a dense layer which has 10 output units, one for each
  // output class (i.e. 0, 1, 2, 3, 4, 5, 6, 7, 8, 9).
  const NUM_OUTPUT_CLASSES = 10;
  model.add(tf.layers.dense({
    units: NUM_OUTPUT_CLASSES,
    kernelInitializer: 'varianceScaling',

    // Gets the maximum value by using the exponent of the value divided by the exponent of the sum of all values. This ensures no value is below zero, or above one (range [0,1])
    // Learned more about it here: https://www.machinecurve.com/index.php/2020/01/08/how-does-the-softmax-activation-function-work/#how-does-softmax-work
    activation: 'softmax'
  }));


  // Optimizes our model, calculating our loss function and accuracy metric
  const optimizer = tf.train.adam();
  model.compile({
    optimizer: optimizer,
    // Combining softmax activation and cross-entropy loss, we can train the NN to match a multi-class classification.
    // Learned more about it here: https://gombru.github.io/2018/05/23/cross_entropy_loss/
    loss: 'categoricalCrossentropy',
    metrics: ['accuracy'],
  });

  return model;
}


/* ==========================================
 Training and testing the neural network model
  - Decides the metrics to be used
  - Creates two batch types:
    - N training elements (N being TRAIN_DATA_SIZE)
    - N testing elements (N being TEST_DATA_SIZE)
  - Visualizes the batch metrics in a graph
 ========================================== */
async function train(model, data) {
  const metrics = ['loss', 'val_loss', 'acc', 'val_acc'];
  const container = {
    name: 'Model Training', tab: 'Model', styles: { height: '1000px' }
  };
  // Visualizes metrics
  const fitCallbacks = tfvis.show.fitCallbacks(container, metrics);

  // Training batches
  const [trainXs, trainYs] = tf.tidy(() => {
    const d = data.nextTrainBatch(TRAIN_DATA_SIZE);
    return [
      d.xs.reshape([TRAIN_DATA_SIZE, 28, 28, 1]),
      d.labels
    ];
  });

  // Testing batches
  const [testXs, testYs] = tf.tidy(() => {
    const d = data.nextTestBatch(TEST_DATA_SIZE);
    return [
      d.xs.reshape([TEST_DATA_SIZE, 28, 28, 1]),
      d.labels
    ];
  });

  // Fit the model using training batches, validated by testing batches.
  // Across 25 epochs, this can be lower, though more epochs have shown better accuracies
  return model.fit(trainXs, trainYs, {
    batchSize: BATCH_SIZE,
    validationData: [testXs, testYs],
    epochs: EPOCHS,
    shuffle: true,
    callbacks: fitCallbacks
  });
}

// Class labels, ordered by value
const classNames = ['Zero', 'One', 'Two', 'Three', 'Four', 'Five', 'Six', 'Seven', 'Eight', 'Nine'];

/* ==========================================
  Create predictions (with labels) based on the given model and data
  - Collects batches from the dataset
  - Creates predictions from model
 ========================================== */
function doPrediction(model, data, testDataSize = 500) {
  const IMAGE_WIDTH = 28;
  const IMAGE_HEIGHT = 28;
  const testData = data.nextTestBatch(testDataSize);
  const testxs = testData.xs.reshape([testDataSize, IMAGE_WIDTH, IMAGE_HEIGHT, 1]);
  const labels = testData.labels.argMax(-1);
  const preds = model.predict(testxs).argMax(-1);

  testxs.dispose();
  return [preds, labels];
}


async function showAccuracy(model, data) {
  const [preds, labels] = doPrediction(model, data);
  const classAccuracy = await tfvis.metrics.perClassAccuracy(labels, preds);
  const container = {name: 'Accuracy', tab: 'Evaluation'};
  tfvis.show.perClassAccuracy(container, classAccuracy, classNames);

  labels.dispose();
}

async function showConfusion(model, data) {
  const [preds, labels] = doPrediction(model, data);
  const confusionMatrix = await tfvis.metrics.confusionMatrix(labels, preds);
  const container = {name: 'Confusion Matrix', tab: 'Evaluation'};
  tfvis.render.confusionMatrix(container, {values: confusionMatrix, tickLabels: classNames});

  labels.dispose();
}
