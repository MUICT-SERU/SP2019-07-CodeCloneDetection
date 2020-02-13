import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.Evaluation;

import weka.core.Instances;
import weka.core.converters.CSVSaver;
import weka.core.converters.ConverterUtils;
import weka.classifiers.trees.J48;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class WekaTraining {
    public static void main(String[] args) throws Exception {
        //read csv file as a data set
//        CSVLoader csvLoader = new CSVLoader();
//        csvLoader.setSource(new File("/Users/sidekoiii/Documents/GitHub/SP2019-DoNotCopy/SelectedClonescopy.csv"));
        ConverterUtils.DataSource source = new ConverterUtils.DataSource("trainModelMetrics.csv");
        Instances data = source.getDataSet();
        //set class index
        data.setClassIndex(data.numAttributes()-1);

        //Split data to 80% of train and 20% of test datasets
        data.randomize(new java.util.Random(0));
        int trainSize = (int) Math.round(data.numInstances() * 0.8);
        int testSize = data.numInstances() - trainSize;
        Instances train = new Instances(data, 0, trainSize);
        Instances test = new Instances(data, trainSize, testSize);


        //save split instances to csv
        saveCsv(train,"trainDataSet.csv");
        saveCsv(test,"testDataSet.csv");


        //Build the classifier
        J48 tree = new J48();
        tree.buildClassifier(train);

        //create model evaluation
        Evaluation eval = new Evaluation(train);
        //load test data set
//        ConverterUtils.DataSource testSource = new ConverterUtils.DataSource("/Users/sidekoiii/Documents/GitHub/SP2019-DoNotCopy/MerryEngine/metricsForTest.csv");
//        Instances testData = testSource.getDataSet();
//        testData.setClassIndex(testData.numAttributes()-1);

        //evaluate model
        eval.evaluateModel(tree,test);

        System.out.println("Evaluation Result :");
        System.out.println("Correct % : "+ eval.pctCorrect());
        System.out.println("Incorrect % : "+eval.pctIncorrect());
        System.out.println("precision : "+eval.precision(1));
        System.out.println("recall : "+eval.recall(1));
        System.out.println("f1 : "+eval.fMeasure(1));
        System.out.println("precision : "+eval.precision(0));
        System.out.println("recall : "+eval.recall(0));
        System.out.println("f1 : "+eval.fMeasure(0));
        System.out.println("precision : "+eval.weightedPrecision());
        System.out.println("recall : "+eval.weightedRecall());
        System.out.println("f1 : "+eval.weightedFMeasure());


        // display classifier
        final javax.swing.JFrame jf =
                new javax.swing.JFrame("Weka Classifier Tree Visualizer: J48");
        jf.setSize(1000,1000);
        jf.getContentPane().setLayout(new BorderLayout());
        TreeVisualizer tv = new TreeVisualizer(null,
                ((J48) tree).graph(),
                new PlaceNode2());
        jf.getContentPane().add(tv, BorderLayout.CENTER);
        jf.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                jf.dispose();
            }
        });

        jf.setVisible(true);
        tv.fitToScreen();
    }

    private static void saveCsv(Instances instance, String path) throws IOException {
        CSVSaver saver = new CSVSaver();
        saver.setInstances(instance);
        saver.setFile(new File(path));
        saver.writeBatch();
    }
}
