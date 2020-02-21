import weka.classifiers.trees.REPTree;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

import javax.sql.DataSource;

public class Weka{
    public static void main(String[] args) throws Exception {
        //Load model
        String modelName = "";
        REPTree treeModel = (REPTree) weka.core.SerializationHelper.read(modelName);

        //Load data set
        ConverterUtils.DataSource source = new ConverterUtils.DataSource("testDataSet.csv");
        Instances data = source.getDataSet();
        //set class index
        data.setClassIndex(data.numAttributes()-1);

        System.out.println("Actual\t| Predict");
        for(int i = 0 ; i < data.numInstances();i++){
            Instance instance = data.instance(i);
            double actualValue = instance.classValue();
            double predictValue = treeModel.classifyInstance(instance);
            String actual = data.classAttribute().value((int) actualValue);
            String predict = data.classAttribute().value((int) predictValue);
            System.out.println(actual+"\t| "+predict);

        }


    }
}
