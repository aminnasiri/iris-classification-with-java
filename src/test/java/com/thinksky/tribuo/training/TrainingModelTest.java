package com.thinksky.tribuo.training;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

class TrainingModelTest {

    private static final Logger logger = LoggerFactory.getLogger(TrainingModelTest.class);

    @Test
    void trainingTest() throws Exception {

        var trainingModel = new TrainingModel();
        var train = trainingModel.train(ClassLoader.getSystemResource("bezdekIris.data"));
        var evaluator = trainingModel.evaluator(train);

        logger.info(evaluator.toString());

        logger.info(evaluator.getConfusionMatrix().toString());

        logger.info("\n\n=====================Feature=========================\n");

        var featureMap = train.getFeatureIDMap();
        for (var v : featureMap) {
            System.out.println(v.toString());
            System.out.println();
        }

        assert evaluator.accuracy() > 0.9;
        Assertions.assertTrue(evaluator.accuracy() > 0.9);

        logger.info("\n\n=================Save Model==========================\n");

        var writeOutputStream = new FileOutputStream("target/irisModel.bin");
        var objectOutputStream = new ObjectOutputStream(writeOutputStream);
        objectOutputStream.writeObject(train);
        objectOutputStream.flush();
        objectOutputStream.close();

        logger.info("Application ends training");
    }

}
