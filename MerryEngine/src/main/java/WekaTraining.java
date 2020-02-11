import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.Evaluation;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.classifiers.trees.J48;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;

import javax.xml.crypto.Data;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class WekaTraining {
    public static void main(String[] args) throws Exception {
        //read csv file as a data set
//        CSVLoader csvLoader = new CSVLoader();
//        csvLoader.setSource(new File("/Users/sidekoiii/Documents/GitHub/SP2019-DoNotCopy/SelectedClonescopy.csv"));
        ConverterUtils.DataSource source = new ConverterUtils.DataSource("/Users/sidekoiii/Documents/GitHub/SP2019-DoNotCopy/MerryEngine/metricsForTrain.csv");
        Instances data = source.getDataSet();
        //set class index
        data.setClassIndex(data.numAttributes()-1);
        //Build the classifier
        J48 tree = new J48();
        tree.buildClassifier(data);

        //create model evaluation
        Evaluation eval = new Evaluation(data);
        //load test data set
        ConverterUtils.DataSource testSource = new ConverterUtils.DataSource("/Users/sidekoiii/Documents/GitHub/SP2019-DoNotCopy/MerryEngine/metricsForTest.csv");
        Instances testData = testSource.getDataSet();
        testData.setClassIndex(testData.numAttributes()-1);

        //evaluate model
        eval.evaluateModel(tree,testData);

        System.out.println("Evalustion Result :");
        System.out.println("Correct % : "+ eval.pctCorrect());
        System.out.println("Incorrect % : "+eval.pctIncorrect());
        System.out.println("precision : "+eval.precision(1));
//        // display classifier
//        final javax.swing.JFrame jf =
//                new javax.swing.JFrame("Weka Classifier Tree Visualizer: J48");
//        jf.setSize(500,400);
//        jf.getContentPane().setLayout(new BorderLayout());
//        TreeVisualizer tv = new TreeVisualizer(null,
//                ((J48) tree).graph(),
//                new PlaceNode2());
//        jf.getContentPane().add(tv, BorderLayout.CENTER);
//        jf.addWindowListener(new java.awt.event.WindowAdapter() {
//            public void windowClosing(java.awt.event.WindowEvent e) {
//                jf.dispose();
//            }
//        });
//
//        jf.setVisible(true);
//        tv.fitToScreen();
    }
}
