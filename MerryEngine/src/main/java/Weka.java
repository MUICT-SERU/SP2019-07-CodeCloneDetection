import weka.classifiers.Evaluation;
import weka.classifiers.trees.REPTree;
import weka.classifiers.trees.RandomForest;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVSaver;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

import java.io.File;

public class Weka{
    public static void main(String[] arg) throws Exception {
        //Load model
        String modelName = "models/randomForest5_model.model";
        RandomForest treeModel = (RandomForest) weka.core.SerializationHelper.read(modelName);

        //Load data set
        ConverterUtils.DataSource source = new ConverterUtils.DataSource("assets/testDataSet.csv");
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

        //prediction
        for(int i = 0; i< data.numInstances();i++){
            Instance instance = data.instance(i);
            double predictValue = treeModel.classifyInstance(instance);
            dataSet.get(i).setValue(dataSet.numAttributes()-1,predictValue);
        }
        CSVSaver saver = new CSVSaver();
        saver.setInstances(dataSet);
        saver.setFile(new File("result/result.csv"));
        saver.writeBatch();
    }
}
