import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.Evaluation;

import weka.classifiers.trees.REPTree;
import weka.core.Instance;
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
        CommandLineArgument wekaConfig = new CommandLineArgument(args);
        ConverterUtils.DataSource source = new ConverterUtils.DataSource("trainModelMetrics.csv");
        Instances data = source.getDataSet();
        //set class index
        data.setClassIndex(data.numAttributes()-1);

        //Split data to 80% of train and 20% of test datasets
        data.randomize(new java.util.Random(1));
        int trainSize = (int) Math.round(data.numInstances() * 0.7);
        int validSize = (int) Math.round(data.numInstances() * 0.1);
        int testSize = data.numInstances() - (trainSize + validSize);
        Instances train = new Instances(data, 0, trainSize);
        Instances valid = new Instances(data, trainSize,validSize);
        Instances test = new Instances(data, trainSize+validSize, testSize);



        //save split instances to csv
        saveCsv(train,"trainDataSet.csv");
        saveCsv(valid,"validDataSet.csv");
        saveCsv(test,"testDataSet.csv");


        //set classifier option
        int treeDepth = wekaConfig.getTreeDepth();
        String[] options = new String[2];
        options[0] = "-L"; options[1] = String.valueOf(treeDepth);

        //Build the classifier
       // J48 tree = new J48();
        REPTree tree = new REPTree();
        tree.setOptions(options);
        tree.buildClassifier(train);

        //saving model
        weka.core.SerializationHelper.write("tree"+treeDepth+"_model.model",tree);
        //create model evaluation
         Evaluation eval = new Evaluation(data);
        //load test data set


        //validate model
        eval.evaluateModel(tree,valid);

        System.out.println("Evaluation Result on Validate Data set:");
        System.out.println("Correct % : "+ eval.pctCorrect());
        System.out.println("Incorrect % : "+ eval.pctIncorrect());
        System.out.println("______________________________________");
        System.out.println("precision : "+ eval.precision(1));
        System.out.println("recall : "+ eval.recall(1));
        System.out.println("f1 : "+ eval.fMeasure(1));
        System.out.println("precision : "+ eval.precision(0));
        System.out.println("recall : "+ eval.recall(0));
        System.out.println("f1 : "+ eval.fMeasure(0));
        System.out.println("______________________________________");
        System.out.println("Weight REPORT :");
        System.out.println("Tree Depth : "+ treeDepth);
        System.out.println("precision : "+ eval.weightedPrecision());
        System.out.println("recall : "+ eval.weightedRecall());
        System.out.println("f1 : "+ eval.weightedFMeasure());
        System.out.println("TP : "+ eval.weightedTruePositiveRate());
        System.out.println("TN : "+ eval.weightedTrueNegativeRate());
        System.out.println("FP : "+ eval.weightedFalsePositiveRate());
        System.out.println("FN : "+ eval.weightedFalseNegativeRate());

        //evaluate model
        Evaluation eval2 = new Evaluation(data);
        eval2.evaluateModel(tree,test);
        System.out.println("\n////////////////////////////////////\n");
        System.out.println("Evaluation Result on Test Data set:");
        System.out.println("Correct % : "+ eval2.pctCorrect());
        System.out.println("Incorrect % : "+ eval2.pctIncorrect());
        System.out.println("______________________________________");
        System.out.println("precision : "+ eval2.precision(1));
        System.out.println("recall : "+ eval2.recall(1));
        System.out.println("f1 : "+ eval2.fMeasure(1));
        System.out.println("precision : "+ eval2.precision(0));
        System.out.println("recall : "+ eval2.recall(0));
        System.out.println("f1 : "+ eval2.fMeasure(0));
        System.out.println("______________________________________");
        System.out.println("Weight REPORT :");
        System.out.println("Tree Depth : "+ treeDepth);
        System.out.println("precision : "+ eval2.weightedPrecision());
        System.out.println("recall : "+ eval2.weightedRecall());
        System.out.println("f1 : "+ eval2.weightedFMeasure());
        System.out.println("TP : "+ eval2.weightedTruePositiveRate());
        System.out.println("TN : "+ eval2.weightedTrueNegativeRate());
        System.out.println("FP : "+ eval2.weightedFalsePositiveRate());
        System.out.println("FN : "+ eval2.weightedFalseNegativeRate());

//        double label = tree.classifyInstance(test.instance(0);
//        test.instance(0).setClassValue(label);
//
//        System.out.println(test.instance(0).stringValue(23));

        // display classifier
        final javax.swing.JFrame jf =
                new javax.swing.JFrame("Weka Classifier Tree Visualizer: J48");
        jf.setSize(1500,800);
        jf.getContentPane().setLayout(new BorderLayout());
        TreeVisualizer tv = new TreeVisualizer(null,
                (tree).graph(),
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
