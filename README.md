# Artificial Intelligence / ML
## AEI-2021-I-3.4
### Digit recognition ([source](https://github.com/LiebenGuus/Artificial-Intelligence-AEI/blob/master/digits/script.js))
This solution trains a TensorFlow model to correctly identify handwritten digits. 
This uses a JavaScript solution using Keras and Tensorflow, using the MNIST dataset of handwritten digits.

#### Input
Datasets are provided by the MNIST database. A sample of this dataset can be seen below.  

![mnist](https://www.researchgate.net/profile/Steven-Young-5/publication/306056875/figure/fig1/AS:393921575309346@1470929630835/Example-images-from-the-MNIST-dataset.png)

#### Model
The network contains convolution functions, each shifting bits in the image based one its brightness. This function applies image kernels to the image in 5x5 blocks (pixels) with 8 filters. 
Each iteration (at most) 1 block is changed. The convolution function has activation `relu`, which returns the original value if it is positive, or zero. 
The kernel itself is `varianceScaling`, which indicates the expected deviation of a random variable from the mean value of the kernel size.

After the image kernel is applied, max pooling is applied (`maxPooling2d`). This acts as a type of downsampling, using the maximum value of an area instead of the average.
This results in a chunk such as the one below returns `10` (max) instead of `4.25` (`3+10+5-1 -> 17`, `17/4 -> 4,25`)  

![maxpool2d](https://user-images.githubusercontent.com/41061518/116557001-566bd880-a8fe-11eb-9df4-4ab33fa4300f.png)

The convolution and max pooling is then applied again, but with a filter count of 16. 
After this the output is flattened into a 1D vector (from a 2D layered vector). Turning e.g. `[[0,2], [1,3]]` into `[0,2,1,3]`.
Finally the output is separated into 10 classes, each representing a digit from 0 to 9. This is again done using variance scaling, with `softmax` activation.
Softmax gets the maximum value by using the exponent of the value divided by the exponent of the sum of all values. This ensures no value is below zero, or above one (range `[0,1]`).

Finally the model is compiled using a standard training compiler (`Adam`), with categorical cross-entropy loss and accuracy metrics.

_Sources_
- [Powell, V. (Image Kernels)](https://setosa.io/ev/image-kernels/)
- [Computer Science Wiki (Max-pooling / Pooling)](https://computersciencewiki.org/index.php/Max-pooling_/_Pooling)
- [Versloot, C. (How does the Softmax activation function work?)](https://www.machinecurve.com/index.php/2020/01/08/how-does-the-softmax-activation-function-work/#how-does-softmax-work)
- [Gombru, R. (Understanding Categorical Cross-Entropy Loss (...)](https://gombru.github.io/2018/05/23/cross_entropy_loss/)

#### Parameters
_There are a total of 65.000 dataset elements, of which 55.000 can be used as training elements (from MNIST)._

**Baseline** values are configured to run a single epoch with a batch size of 128 using a training dataset size of 500.
**Default** values are configured to run a total of 25 epochs with a batch size of 512 using a training dataset size of 5500.
For additional accuracy, a maximum of 200 epochs can be chosen with the **default** values. 
More epochs can be used, though after ~200 the accuracy is typically already 1.0 ±.

#### Results
**Baseline**
![accuracy_confusion_baseline](https://user-images.githubusercontent.com/10957963/116553937-f0318680-a8fa-11eb-9cd9-adaabfc7922d.png)

**Default**
![accuracy_confusion_25e](https://user-images.githubusercontent.com/10957963/116553934-ef98f000-a8fa-11eb-8b6e-db8d3888c4cf.png)

**Accuracy**
![accuracy_confusion_200e](https://user-images.githubusercontent.com/10957963/116553936-f0318680-a8fa-11eb-8ab3-76fff77203ae.png)

#### Alternative ([source](https://github.com/LiebenGuus/Artificial-Intelligence-AEI/blob/master/digits-java/src/main/java/DigitRecognition.java))
This same model has been implemented in Java, using [Deep Learning for Java (DL4J)](https://deeplearning4j.org/). 
This implementation has a lower weight decay, and takes longer to train. However the accuracy of results is noticeably better, as can be seen in the results below.
##### Confusion Matrix
```bash
    0    1    2    3    4    5    6    7    8    9
---------------------------------------------------
  972    0    1    0    0    1    2    1    3    0 | 0 = 0
    0 1123    2    1    0    0    2    0    7    0 | 1 = 1
    3    2 1005    6    2    1    2    6    5    0 | 2 = 2
    1    1    3  985    0    7    0    5    6    2 | 3 = 3
    1    0    3    0  958    0    5    2    2   11 | 4 = 4
    4    2    1   13    0  862    7    1    2    0 | 5 = 5
    8    3    1    0    6    5  933    0    2    0 | 6 = 6
    1    6   22    3    0    0    0  987    3    6 | 7 = 7
    7    0    2    6    3    2    6    4  938    6 | 8 = 8
    4    8    0    7   12    6    1    5    2  964 | 9 = 9
```

##### Accuracy 
```bash
Label               AUC         # Pos     # Neg
0                   0.9998      980       9020
1                   0.9998      1135      8865
2                   0.9995      1032      8968
3                   0.9989      1010      8990
4                   0.9996      982       9018
5                   0.9994      892       9108
6                   0.9995      958       9042
7                   0.9986      1028      8972
8                   0.9988      974       9026
9                   0.9987      1009      8991
```
```bash
Average AUC: 0.9993
```
