import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.Evaluation;

import weka.classifiers.trees.REPTree;
import weka.core.Instances;
import weka.core.converters.CSVSaver;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class WekaTraining {
    public static void main(String[] args) throws Exception {
        //read csv file as a data set
        CommandLineArgument wekaConfig = new CommandLineArgument(args);
        ConverterUtils.DataSource source = new ConverterUtils.DataSource("assets/trainModelMetrics.csv");
        Instances dataSet = source.getDataSet();

        //Split data to 80% of train and 20% of test datasets
        dataSet.randomize(new java.util.Random(1));
        int trainSize = (int) Math.round(dataSet.numInstances() * 0.7);
        int validSize = (int) Math.round(dataSet.numInstances() * 0.1);
        int testSize = dataSet.numInstances() - (trainSize + validSize);
        Instances train = new Instances(dataSet, 0, trainSize);
        Instances valid = new Instances(dataSet, trainSize,validSize);
        Instances test = new Instances(dataSet, trainSize+validSize, testSize);

        //save split instances to csv
        saveCsv(train,"assets/trainDataSet.csv");
        saveCsv(valid,"assets/validDataSet.csv");
        saveCsv(test,"assets/testDataSet.csv");

        ConverterUtils.DataSource trainData = new ConverterUtils.DataSource("assets/trainDataSet.csv");
        train = trainData.getDataSet();
        ConverterUtils.DataSource validData = new ConverterUtils.DataSource("assets/validDataSet.csv");
        valid = validData.getDataSet();
        ConverterUtils.DataSource testData = new ConverterUtils.DataSource("assets/testDataSet.csv");
        test = testData.getDataSet();

        //remove attribute (ID)
        String[] removeOpts = new String[2];
        removeOpts[0] = "-R"; removeOpts[1] = "1";
        Remove remove = new Remove();
        remove.setOptions(removeOpts);
        remove.setInputFormat(train);
        train = Filter.useFilter(train,remove);
        valid = Filter.useFilter(valid,remove);
        test = Filter.useFilter(test,remove);

        //set class index
        train.setClassIndex(train.numAttributes()-1);
        valid.setClassIndex(valid.numAttributes()-1);
        test.setClassIndex(test.numAttributes()-1);

        //set classifier option
        int treeDepth = wekaConfig.getTreeDepth();
        String[] options = new String[2];
        options[0] = "-L"; options[1] = String.valueOf(treeDepth);

        //Build the classifier
        REPTree tree = new REPTree();
        tree.setOptions(options);
        tree.buildClassifier(train);

        //saving model
        weka.core.SerializationHelper.write("models/tree"+treeDepth+"_model.model",tree);
        //create model evaluation
        Evaluation eval = new Evaluation(train);

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
        Evaluation eval2 = new Evaluation(train);
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

        // display classifier
        final javax.swing.JFrame jf =
                new javax.swing.JFrame("Weka Classifier Tree Visualizer");
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

    private static void saveCsv(Instances instances, String path) throws IOException {
        CSVSaver saver = new CSVSaver();
        saver.setInstances(instances);
        saver.setFile(new File(path));
        saver.writeBatch();
    }

}
