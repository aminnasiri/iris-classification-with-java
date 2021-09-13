package com.thinksky.tribuo.training;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tribuo.Model;
import org.tribuo.MutableDataset;
import org.tribuo.Trainer;
import org.tribuo.classification.Label;
import org.tribuo.classification.LabelFactory;
import org.tribuo.classification.evaluation.LabelEvaluation;
import org.tribuo.classification.evaluation.LabelEvaluator;
import org.tribuo.classification.sgd.linear.LogisticRegressionTrainer;
import org.tribuo.data.csv.CSVLoader;
import org.tribuo.evaluation.TrainTestSplitter;

import java.io.IOException;
import java.net.URL;

public class TrainingModel {
    private static final Logger logger = LoggerFactory.getLogger(TrainingModel.class);

    private MutableDataset<Label> testingDataset;

    /**
     * It will get URL of raw dataset file and return trained dataset
     * @param url Dataset file as URL object
     * @return Model generic Label
     * @throws IOException
     */
    public Model<Label> train(URL url) throws IOException {
        logger.info("Application starts");

        var labelFactory = new LabelFactory();
        var csvLoader = new CSVLoader<>(labelFactory);

        var irisHeaders = new String[]{"sepalLength", "sepalWidth", "petalLength", "petalWidth", "species"};
        var irisesSource = csvLoader.loadDataSource(url, "species", irisHeaders);
        var irisSplitter = new TrainTestSplitter<>(irisesSource, 0.7, 1L);

        var trainingDataset = new MutableDataset<>(irisSplitter.getTrain());
        testingDataset = new MutableDataset<>(irisSplitter.getTest());

        System.out.printf("Training data size = %d, number of features = %d, number of classes = %d%n", trainingDataset.size(), trainingDataset.getFeatureMap().size(), trainingDataset.getOutputInfo().size());

        System.out.printf("Testing data size = %d, number of features = %d, number of classes = %d%n", testingDataset.size(), testingDataset.getFeatureMap().size(), testingDataset.getOutputInfo().size());

        //Trainer<Label> trainer = new XGBoostClassificationTrainer(50);
        Trainer<Label> trainer = new LogisticRegressionTrainer();
        logger.info(trainer.toString());

        return trainer.train(trainingDataset);
    }

    /**
     * It will get a model and evaluate it by global test dataset
     * @param model Trained model
     * @return {@link LabelEvaluation}
     */
    public LabelEvaluation evaluator(Model<Label> model){

        var evaluator = new LabelEvaluator();
        return evaluator.evaluate(model, testingDataset);
    }


}
