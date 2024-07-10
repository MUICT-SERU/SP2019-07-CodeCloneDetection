import org.netlib.arpack.Smout;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.functions.SMO;
import weka.classifiers.trees.REPTree;
import weka.classifiers.trees.RandomForest;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVSaver;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;

import java.awt.*;
import java.io.File;
import java.sql.SQLOutput;
import java.util.Random;

public class Weka{
    public static void main(String[] arg) throws Exception {
        CommandLineArgument cmd = new CommandLineArgument(arg);
        //Load model
        Classifier model = null;
        boolean syn = cmd.isSyntactic();
        boolean sem = cmd.isSemantic();
        String modelName = "";
        if(cmd.getModel().equalsIgnoreCase("SMO")){
            if(!syn && sem){
                modelName = cmd.getWorkingDir()+"/models/SMOOnlyc2v_model.model";
            }else if(syn && !sem){
                modelName = cmd.getWorkingDir()+"/models/SMOOnlySyn_model.model";
            }else {
                modelName = cmd.getWorkingDir()+"/models/SMODefault_model.model";
            }
            model = (SMO) weka.core.SerializationHelper.read(modelName);
        }
        if(cmd.getModel().equalsIgnoreCase("Dtree")||cmd.getModel().equalsIgnoreCase("decisionTree")){
            if(!syn && sem){
                modelName = cmd.getWorkingDir()+"/models/DtreeOnlyc2v_model.model";
            }else if(syn && !sem){
                modelName = cmd.getWorkingDir()+"/models/DtreeOnlySyn_model.model";
            }else {
                modelName = cmd.getWorkingDir()+"/models/DTreeDefault_model.model";
            }
            model = (REPTree) weka.core.SerializationHelper.read(modelName);
        }
        if(cmd.getModel().equalsIgnoreCase("randomForest")){
            if(!syn && sem){
                modelName = cmd.getWorkingDir()+"/models/RandomForestOnlyc2v_model.model";
            }else if(syn && !sem){
                modelName = cmd.getWorkingDir()+"/models/RandomForestOnlySyn_model.model";
            }else {
                modelName = cmd.getWorkingDir()+"/models/RandomForestDefault_model.model";
            }
            model = (RandomForest) weka.core.SerializationHelper.read(modelName);
        }
        if(cmd.getModel().equalsIgnoreCase("SVM")){
            if(!syn && sem){
                modelName = cmd.getWorkingDir()+"/models/LibSVMOnlyc2v_model.model";
            }else if(syn && !sem){
                modelName = cmd.getWorkingDir()+"/models/LibSVMOnlySyn_model.model";
            }else {
                modelName = cmd.getWorkingDir()+"/models/LibSVMDefault_model.model";
            }
            model = (LibSVM) weka.core.SerializationHelper.read(modelName);
        }
//        Multi-Class model
//        model = (RandomForest) weka.core.SerializationHelper.read(cmd.getWorkingDir()+"/models/randomForestMulticlass_model.model");

        //Load data set
//        ConverterUtils.DataSource source = new ConverterUtils.DataSource(cmd.getWorkingDir()+"/assets/metrics.csv");
        ConverterUtils.DataSource source = new ConverterUtils.DataSource(cmd.getWorkingDir()+"/assets/metrics.csv");
//        ConverterUtils.DataSource source = new ConverterUtils.DataSource("assets/trainModelMetrics.csv");
        Instances dataSet = source.getDataSet();
        String[] removeOpts = new String[2];
        removeOpts[0] = "-R"; removeOpts[1] = "1";
        Remove remove = new Remove();
        remove.setOptions(removeOpts);
        remove.setInputFormat(dataSet);
        Instances data = Filter.useFilter(dataSet,remove);
        //set class index
        data.setClassIndex(data.numAttributes()-1);
        dataSet.setClassIndex(data.numAttributes()-1);

        System.out.println("0 is "+data.classAttribute().value(0));
        System.out.println("1 is "+data.classAttribute().value(1));
//        System.out.println("2 is "+data.classAttribute().value(2));
//        System.out.println("3 is "+data.classAttribute().value(3));

//        System.out.println("Num attribute : "+data.numAttributes());
        //prediction
        int countTrue = 0;
        int countFalse = 0;
        int tp = 0, tn = 0,fn = 0, fp = 0;
        Random rand = new Random(1);
        for(int i = 0; i < data.numInstances();i++){
            Instance instance = data.instance(i);
//            int predictValue =0;
            double predictValue = model.classifyInstance(instance);
//            if(cmd.getModel().equalsIgnoreCase("justRandom")){
//                int actual = (int)instance.classValue();
//                predictValue = rand.nextInt(2);
////                System.out.println(predictValue);
//                if(actual == 0 && predictValue == 0){
//                    tp++;
//                }else if(actual == 1 && predictValue == 1){
//                    tn++;
//                }else if(actual == 1 && predictValue == 0){
//                    fp++;
//                }else if(actual == 0 && predictValue ==1){
//                    fn++;
//                }
//            }
            dataSet.get(i).setValue(dataSet.numAttributes()-1,predictValue);
//            System.out.println(predictValue);
            String predictString;
            if(predictValue == 0){
                predictString = "true";
                countTrue++;
            }else{
                predictString = "false";
                countFalse++;
            }
//            predictString = data.classAttribute().value((int) predictValue);
//            System.out.println(predictString);
            dataSet.get(i).setValue(dataSet.numAttributes()-1,predictString);
//            dataSet.get(i).setClassValue(predictString);
//            System.out.println(dataSet.instance(i).stringValue(dataSet.numAttributes()-1));
        }
        System.out.println("Done Prediction");
        int all=countFalse+countTrue;
        System.out.println("TrueCount = "+countTrue+" from "+all);
//        System.out.println("This for JustRandom NO model Only");
//        System.out.println("TP:" + tp);
//        System.out.println("TN:" + tn);
//        System.out.println("FP:" + fp);
//        System.out.println("FN:" + fn);
//        System.out.println("_________________________________");
//        Evaluation eval = new Evaluation(data);
//        eval.evaluateModel(model,data);
//
//        System.out.println(eval.toMatrixString("===== Confusion matrix ====="));
//
//        System.out.println(data.classAttribute().value(0));
//        System.out.println("Precision "+eval.precision(0));
//        System.out.println("Recall "+eval.recall(0));
//        System.out.println("f1 "+eval.fMeasure(0));
//
//        System.out.println(data.classAttribute().value(1));
//        System.out.println("Precision "+eval.precision(1));
//        System.out.println("Recall "+eval.recall(1));
//        System.out.println("f1 "+eval.fMeasure(1));
//
//        System.out.println(data.classAttribute().value(2));
//        System.out.println("Precision "+eval.precision(2));
//        System.out.println("Recall "+eval.recall(2));
//        System.out.println("f1 "+eval.fMeasure(2));

        CSVSaver saver = new CSVSaver();
        saver.setInstances(dataSet);
        saver.setFile(new File(cmd.getWorkingDir()+"/result/result.csv"));
        saver.writeBatch();
    }
}
