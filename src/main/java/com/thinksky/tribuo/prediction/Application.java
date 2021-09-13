package com.thinksky.tribuo.prediction;


import com.oracle.labs.mlrg.olcut.provenance.ProvenanceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tribuo.Model;
import org.tribuo.classification.Label;
import org.tribuo.classification.LabelFactory;
import org.tribuo.data.csv.CSVLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Path;

public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String... args) throws IOException, ClassNotFoundException {
        logger.info("Application starts");

        var labelFactory = new LabelFactory();
        var csvLoader = new CSVLoader<>(labelFactory);

        //Read saved model
        var readInputStream = new FileInputStream("target/irisModel.bin");
        var objectInputStream = new ObjectInputStream(readInputStream);
        var savedModel = (Model<Label>) objectInputStream.readObject();

        var provenance = savedModel.getProvenance();
        System.out.println(ProvenanceUtil.formattedProvenanceString(provenance.getDatasetProvenance().getSourceProvenance()));

        System.out.println(ProvenanceUtil.formattedProvenanceString(provenance.getTrainerProvenance()));

        var irisHeadersPre = new String[]{"sepalLength", "sepalWidth", "petalLength", "petalWidth", "species"};
        var irisesSourcePre = csvLoader.loadDataSource(Path.of("iris-prediction.data"), "species", irisHeadersPre);

        var predictionList = savedModel.predict(irisesSourcePre);
        predictionList.forEach(labelPrediction -> {
            logger.info("Predicted: {}", labelPrediction.getOutput().getLabel());
        });


        logger.info("Application ends");
    }
}
