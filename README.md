# App

This project is using [Tribuo](https://tribuo.org) from Oracle Lab, and Tribuo is a library for creating Machine
Learning (ML) models and for using those models to make predictions on previously unseen data.

## Prerequisites

- Java 11+
- Apache Maven
- GraalVM Native Image (optional)

## Train the model and store it as a file. 

```
$ mvn test -Dtest=com.thinksky.tribuo.training.TrainingModelTest
```

## Predict Iris

```
$ mvn clean install
```

then execute java app

```
$ java -jar target/iris-classification-1.0-SNAPSHOT.jar
```

## Building the native image

Note that it requires `native-image` installed in your environment

```
mvn clean package -P native-image
```

After it finishes, you can find the generated native image in the `target
` folder.

You can run the following command to execute it.

```
target/mynativeimageapp
```
