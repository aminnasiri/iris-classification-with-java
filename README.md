# The project

This project is using [Tribuo](https://tribuo.org) from Oracle Lab, and Tribuo is a library for creating Machine
Learning (ML) models and for using those models to make predictions on previously unseen data.

there is two sections:

+ Train model
+ Predicate input data

## The Iris Flower classification

[Iris dataset](https://en.wikipedia.org/wiki/Iris_flower_data_set) includes three species of iris flowers, 
and our mission is base on the information of these flowers predict which species it has in a new data coming.  

- Setosa
- Versicolor
- Virginica

1st Column: Sepal-Length
2nd Column: Sepal-Width
3rd Column: Petal-Length
4th Column: Petal-Width 
5th Column: species

So this dataset has 4 features classes: the length and the width of the sepals and petals, in centimeters.

## Train the model and store it as a file.

There is a test class for training model, and we can run it a couple of times to make mature model. Then this mature
trained model can be store as file on disk, and it will be doable by this maven command.

```
$ mvn test -Dtest=com.thinksky.tribuo.training.TrainingModelTest
```

Main job of training a model is doing on [TrainingModel](https://github.com/aminnasiri/iris-classification-with-java/blob/main/src/main/java/com/thinksky/tribuo/training/TrainingModel.java), 
this class has two main public methods.
- 
- [train()](https://github.com/aminnasiri/iris-classification-with-java/blob/main/src/main/java/com/thinksky/tribuo/training/TrainingModel.java#L25)
    1) Load dataset as a csv file format from ClassLoader as Java resource.
    2) Use `TrainTestSplitter` from Tribuo lib to separate train and test datasets.
    3) Use `LogisticRegressionTrainer`to train mode.
  
- [evaluator()](https://github.com/aminnasiri/iris-classification-with-java/blob/main/src/main/java/com/thinksky/tribuo/training/TrainingModel.java#L49)
   1) It will get trained model and return `LabelEvaluation`, and it has lots of useful statistics data of evaluation of model.

## Test the trained model

Mostly testing a Java application occurs on uni-test, so we can run our test with JUnit and TrainingModel.
`TrainingModelTest` has a method which it will repeatedly run to have mature model.

[trainingTest()](https://github.com/aminnasiri/iris-classification-with-java/blob/main/src/test/java/com/thinksky/tribuo/training/TrainingModelTest.java#L16)
  - This method will load the `bezdekIris.data` file as URL object and get a train moel and evaluate information.
  - `evaluator` object includes useful information
 

    | Class            |      n     |     tp    |      fn    |    fp  |   recall   |   prec    |   f1   |
    |------------------|------------|-----------|------------|--------|------------|-----------|--------|
    | Iris-versicolor  |     16     |     16    |      0     |    1   |   1.000    |   0.941   |  0.970 |
    | Iris-virginica   |     15     |     14    |      1     |    0   |   0.933    |   1.000   |  0.966 |
    | Iris-setosa      |     14     |     14    |      0     |    0   |   1.000    |  1.000    |  1.000 |
    | Total            |     45     |     44    |      1     |    1   |            |           |        |

     
     - Accuracy                                                                    0.978
     - Micro Average                                                               0.978       0.978       0.978
     - Macro Average                                                               0.978       0.980       0.978
     - Balanced Error Rate                                                         0.022

This output lists:
- class, the different classes in the test set
- n, the number of ground truth labels of that class
- tp, the number of true positives (i.e., the number of times the classifier correctly predicted this class)
- fn, the number of false negatives (i.e., the number of times the classifier predicted this class as another class)
- fp, the number of false positives (i.e., the number of times the classifier incorrectly predicted this class when it was another class)
- recall, the true positives divided by the number of ground truth labels (i.e., the fraction of this class that the classifier can detect)
- precision, the true positives divided by the predicted positives (i.e, the fraction of the time that this class is predicted correctly)
- accuracy, the sum of the true positives divided by the total number of test examples
- balanced error rate, the average of the per class error rates

## Predict Iris

If we have a mature trained model and store it in a file then it is ready to predict a dataset of new data. We can build
the Java application by a maven command.

```
$ mvn clean install
```

then it is ready to predict a new dataset by execute java app. The java application will read `iris-prediction.data` and
print the result of prediction on the console.

```
$ java -jar target/iris-classification-1.0-SNAPSHOT.jar
```

this is a sample of prediction.

```shell
2021-09-12 22:53:22:238 -0700 [main] INFO com.thinksky.tribuo.prediction.Application - Predicted: Setosa
2021-09-12 22:53:22:238 -0700 [main] INFO com.thinksky.tribuo.prediction.Application - Predicted: Versicolor
2021-09-12 22:53:22:238 -0700 [main] INFO com.thinksky.tribuo.prediction.Application - Predicted: Virginica
2021-09-12 22:53:22:238 -0700 [main] INFO com.thinksky.tribuo.prediction.Application - Application ends
```

[comment]: <> (## Building the native image)

[comment]: <> (Note that it requires `native-image` installed in your environment, [intallation guid]&#40;https://thinksky.com/blog/1/01/01/install-graalvm-native-image/&#41;)

[comment]: <> (```)

[comment]: <> (mvn clean install -P native)

[comment]: <> (```)

[comment]: <> (After it finishes, you can find the generated native image in the `target` folder, and you can run the following command to execute it.)

[comment]: <> (```)

[comment]: <> (target/iris-classification)

[comment]: <> (```)
