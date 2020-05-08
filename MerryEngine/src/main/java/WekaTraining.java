import com.github.javaparser.resolution.declarations.ResolvedAnnotationMemberDeclaration;
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
import java.util.Random;

public class WekaTraining {
    public static void main(String[] args) throws Exception {
        CommandLineArgument wekaConfig = new CommandLineArgument(args);
        //read csv file as a data set
        ConverterUtils.DataSource source = new ConverterUtils.DataSource("/Users/sidekoiii/Desktop/crossValidation/trainModelMetrics.csv");
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
//        System.out.println("2 is "+dataSet.classAttribute().value(2));
//        System.out.println("3 is "+dataSet.classAttribute().value(3));



//        //Decision Tree
        //set classifier option
//        int treeDepth = 5;//wekaConfig.getTreeDepth();
//        String[] options = new String[2];
//        options[0] = "-L"; options[1] = treeDepth+"";

        //Build the classifier
        REPTree model = new REPTree();
//        model.setOptions(options);
        model.buildClassifier(dataSet);

        //Random forest
//        //Build the classifier
//        RandomForest model = new RandomForest();
//        model.buildClassifier(dataSet);

        //SVM SMO
//        SMO model = new SMO();
//        model.buildClassifier(dataSet);

        //LibSVM
//        LibSVM model = new LibSVM();
//        model.buildClassifier(dataSet);

        //cross validation
//        int seed = 1;
//        int folds =10;
//        Random rand = new Random(seed);
//        Instances randData = new Instances(dataSet);
//        randData.randomize(rand);
//        if(randData.classAttribute().isNominal()){
//            randData.stratify(folds);
//        }
//        double[] prec = new double[folds], rec = new double[folds], f1s = new double[folds], err = new double[folds];
//        for(int n = 0 ; n < folds; n++){
//            Evaluation crossEval = new Evaluation(randData);
//            Instances train = randData.trainCV(folds,n);
//            Instances test = randData.testCV(folds,n);
//            model.buildClassifier(train);
//            crossEval.evaluateModel(model,test);
//            prec[n] = crossEval.precision(0);
//            rec[n] = crossEval.recall(0);
//            f1s[n] = crossEval.fMeasure(0);
//            err[n] = crossEval.errorRate();
//            //eval output
//            System.out.println();
//            int fold = n+1;
//            System.out.println(crossEval.toMatrixString("===== Confusion matrix for fold "+fold+"/"+folds+" ====="));
////            System.out.println("correct pct : "+crossEval.pctCorrect());
////            System.out.println("incorrect pct : "+crossEval.pctIncorrect());
//            System.out.println("Precision : "+crossEval.precision(0));
//            System.out.println("Recall : "+crossEval.recall(0));
//            System.out.println("F1-Score : "+crossEval.fMeasure(0));
//            System.out.println("Error rate :" + crossEval.errorRate());
//        }
//
//        System.out.println("\n========================================\n");
//        System.out.println("Average Precision : "+ computeAverage(prec));
//        System.out.println("Average Recall : "+computeAverage(rec));
//        System.out.println("Average F1-Score : "+computeAverage(f1s));
//        System.out.println("Average Error rate :" + computeAverage(err));
//        System.out.println("Standard Deviation Precision : "+ computeSD(prec));
//        System.out.println("Standard Deviation Recall : "+computeSD(rec));
//        System.out.println("Standard Deviation F1-Score : "+computeSD(f1s));
//        saving model
//        weka.core.SerializationHelper.write("models/randomForestMulticlass"+"_model.model",model);
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
        final javax.swing.JFrame jf =
                new javax.swing.JFrame("Weka Classifier Tree Visualizer");
        jf.setSize(1500,800);
        jf.getContentPane().setLayout(new BorderLayout());
        TreeVisualizer tv = new TreeVisualizer(null,
                (model).graph(),
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

    private static double computeAverage(double arr[]){
        double sum = 0.0;
        int length = arr.length;
        for(Double value : arr){
            sum+= value;
        }
        return sum/length;
    }

    private static double computeSD(double arr[]){
        double sd=0.0;
        int length = arr.length;
        double mean = computeAverage(arr);
        for(double num: arr) {
            sd += Math.pow(num - mean, 2);
        }

        return Math.sqrt(sd/length);
    }

}
