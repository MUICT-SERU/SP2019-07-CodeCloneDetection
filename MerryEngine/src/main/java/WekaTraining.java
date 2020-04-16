import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.Evaluation;

import weka.classifiers.functions.LibSVM;
import weka.classifiers.functions.SMO;
import weka.classifiers.trees.REPTree;
import weka.classifiers.trees.RandomForest;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVSaver;
import weka.core.converters.ConverterUtils;
import weka.core.converters.LibSVMLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class WekaTraining {
    public static void main(String[] args) throws Exception {
        CommandLineArgument wekaConfig = new CommandLineArgument(args);
        //read csv file as a data set
        ConverterUtils.DataSource source = new ConverterUtils.DataSource("assets/trainModelMetricsOnlyc2v.csv");
        Instances dataSet = source.getDataSet();

        //Split data to 80% of train and 20% of test datasets
//        dataSet.randomize(new java.util.Random(0));
//        int trainSize = (int) Math.round(dataSet.numInstances() * 0.7);
//        int validSize = (int) Math.round(dataSet.numInstances() * 0.1);
//        int testSize = dataSet.numInstances() - (trainSize + validSize);
//        Instances train = new Instances(dataSet, 0, trainSize);
//        Instances valid = new Instances(dataSet, trainSize,validSize);
//        Instances test = new Instances(dataSet, trainSize+validSize, testSize);
//        System.out.println("Train size should be"+trainSize+"Instances train size is "+train.numInstances()+ " from data size "+ dataSet.numInstances());

        //remove attribute (ID)
        String[] removeOpts = new String[2];
        removeOpts[0] = "-R"; removeOpts[1] = "1";
        Remove remove = new Remove();
        remove.setOptions(removeOpts);
        remove.setInputFormat(dataSet);
        dataSet = Filter.useFilter(dataSet,remove);

        //set class index
        dataSet.setClassIndex(dataSet.numAttributes()-1);
        System.out.println("Trainning no = " + dataSet.numInstances() );
        System.out.println("0 is "+dataSet.classAttribute().value(0));
        System.out.println("1 is "+dataSet.classAttribute().value(1));

//        //Decision Tree
        //set classifier option
//        int treeDepth = 5;//wekaConfig.getTreeDepth();
//        String[] options = new String[2];
//        options[0] = "-L"; options[1] = treeDepth+"";

        //Build the classifier
//        REPTree model = new REPTree();
////        model.setOptions(options);
//        model.buildClassifier(dataSet);

        //Random forest
//        //Build the classifier
//        RandomForest model = new RandomForest();
//        model.buildClassifier(dataSet);

        //SVM SMO
//        SMO model = new SMO();
//        model.buildClassifier(dataSet);

        //LibSVM
        LibSVM model = new LibSVM();
        model.buildClassifier(dataSet);

        //saving model
        weka.core.SerializationHelper.write("models/LibSVMOnlyc2v"+"_model.model",model);
//        //create model evaluation
//        Evaluation eval = new Evaluation(dataSet);
//
//        //validate model
//        eval.evaluateModel(model,valid);
//
//        System.out.println("Evaluation Result on Validate Data set:");
//        System.out.println("Correct % : "+ eval.pctCorrect());
//        System.out.println("Incorrect % : "+ eval.pctIncorrect());
//        System.out.println("______________________________________");
//        System.out.println("precision : "+ eval.precision(1));
//        System.out.println("recall : "+ eval.recall(1));
//        System.out.println("f1 : "+ eval.fMeasure(1));
//        System.out.println("precision : "+ eval.precision(0));
//        System.out.println("recall : "+ eval.recall(0));
//        System.out.println("f1 : "+ eval.fMeasure(0));
//        System.out.println("______________________________________");
//        System.out.println("Weight REPORT :");
//        System.out.println("Tree Depth : "+ treeDepth);
//        System.out.println("precision : "+ eval.weightedPrecision());
//        System.out.println("recall : "+ eval.weightedRecall());
//        System.out.println("f1 : "+ eval.weightedFMeasure());
//        System.out.println("TP : "+ eval.weightedTruePositiveRate());
//        System.out.println("TN : "+ eval.weightedTrueNegativeRate());
//        System.out.println("FP : "+ eval.weightedFalsePositiveRate());
//        System.out.println("FN : "+ eval.weightedFalseNegativeRate());
//
//        //evaluate model
//        Evaluation eval2 = new Evaluation(train);
//        eval2.evaluateModel(model,test);
//        System.out.println("\n////////////////////////////////////\n");
//        System.out.println("Evaluation Result on Test Data set:");
//        System.out.println("Correct % : "+ eval2.pctCorrect());
//        System.out.println("Incorrect % : "+ eval2.pctIncorrect());
//        System.out.println("______________________________________");
//        System.out.println("precision : "+ eval2.precision(1));
//        System.out.println("recall : "+ eval2.recall(1));
//        System.out.println("f1 : "+ eval2.fMeasure(1));
//        System.out.println("precision : "+ eval2.precision(0));
//        System.out.println("recall : "+ eval2.recall(0));
//        System.out.println("f1 : "+ eval2.fMeasure(0));
//        System.out.println("______________________________________");
//        System.out.println("Weight REPORT :");
//        System.out.println("Tree Depth : "+ treeDepth);
//        System.out.println("precision : "+ eval2.weightedPrecision());
//        System.out.println("recall : "+ eval2.weightedRecall());
//        System.out.println("f1 : "+ eval2.weightedFMeasure());
//        System.out.println("TP : "+ eval2.weightedTruePositiveRate());
//        System.out.println("TN : "+ eval2.weightedTrueNegativeRate());
//        System.out.println("FP : "+ eval2.weightedFalsePositiveRate());
//        System.out.println("FN : "+ eval2.weightedFalseNegativeRate());

//         display classifier
//        final javax.swing.JFrame jf =
//                new javax.swing.JFrame("Weka Classifier Tree Visualizer");
//        jf.setSize(1500,800);
//        jf.getContentPane().setLayout(new BorderLayout());
//        TreeVisualizer tv = new TreeVisualizer(null,
//                (model).graph(),
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
