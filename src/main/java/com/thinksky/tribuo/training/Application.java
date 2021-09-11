package com.thinksky.tribuo.training;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tribuo.Model;
import org.tribuo.MutableDataset;
import org.tribuo.Trainer;
import org.tribuo.classification.Label;
import org.tribuo.classification.LabelFactory;
import org.tribuo.classification.evaluation.LabelEvaluator;
import org.tribuo.classification.sgd.linear.LogisticRegressionTrainer;
import org.tribuo.data.csv.CSVLoader;
import org.tribuo.evaluation.TrainTestSplitter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String... args) throws IOException, ClassNotFoundException {
        logger.info("Application starts");


        var labelFactory = new LabelFactory();
        var csvLoader = new CSVLoader<>(labelFactory);

        var irisHeaders = new String[]{"sepalLength", "sepalWidth", "petalLength", "petalWidth", "species"};
        var irisesSource = csvLoader.loadDataSource(ClassLoader.getSystemResource("bezdekIris.data"), "species", irisHeaders);
        var irisSplitter = new TrainTestSplitter<>(irisesSource, 0.7, 1L);

        var trainingDataset = new MutableDataset<>(irisSplitter.getTrain());
        var testingDataset = new MutableDataset<>(irisSplitter.getTest());

        System.out.printf("Training data size = %d, number of features = %d, number of classes = %d%n", trainingDataset.size(), trainingDataset.getFeatureMap().size(), trainingDataset.getOutputInfo().size());

        System.out.printf("Testing data size = %d, number of features = %d, number of classes = %d%n", testingDataset.size(), testingDataset.getFeatureMap().size(), testingDataset.getOutputInfo().size());

        //Trainer<Label> trainer = new XGBoostClassificationTrainer(50);
        Trainer<Label> trainer = new LogisticRegressionTrainer();
        logger.info(trainer.toString());

        Model<Label> irisModel = trainer.train(trainingDataset);

        var evaluator = new LabelEvaluator();
        var evaluation = evaluator.evaluate(irisModel, testingDataset);
        logger.info(evaluation.toString());

        logger.info(evaluation.getConfusionMatrix().toString());

        logger.info("\n\n=====================Feature=========================\n");

        var featureMap = irisModel.getFeatureIDMap();
        for (var v : featureMap) {
            System.out.println(v.toString());
            System.out.println();
        }


        logger.info("\n\n=================Save Model==========================\n");
        var writeOutputStream = new FileOutputStream("irisModel.bin");
        var objectOutputStream = new ObjectOutputStream(writeOutputStream);
        objectOutputStream.writeObject(irisModel);
        objectOutputStream.flush();
        objectOutputStream.close();

        logger.info("Application ends training");
    }
}
