import weka.classifiers.Evaluation;
import weka.classifiers.trees.REPTree;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;

import javax.sql.DataSource;
import java.awt.*;

public class Weka{
    public static void main(String[] args) throws Exception {
        //Load model
        String modelName = "tree4_model.model";
        REPTree treeModel = (REPTree) weka.core.SerializationHelper.read(modelName);

        //Load data set
        ConverterUtils.DataSource source = new ConverterUtils.DataSource("testDataSet.csv");
        Instances data = source.getDataSet();
        //set class index
        data.setClassIndex(data.numAttributes()-1);

        //prediction
        int correctCount = 0;
        int incorrectCount = 0;
        System.out.println("Actual\t| Predict");
        for(int i = 0 ; i < data.numInstances();i++){
            Instance instance = data.instance(i);
            double actualValue = instance.classValue();
            double predictValue = treeModel.classifyInstance(instance);
            String actual = data.classAttribute().value((int) actualValue);
            String predict = data.classAttribute().value((int) predictValue);
//            System.out.println(actual+"\t| "+predict);
            if(actual.equalsIgnoreCase(predict)){
                correctCount++;
            }else{
                incorrectCount++;
            }

        }
        int total  = correctCount+incorrectCount;
        Evaluation eval = new Evaluation(data);
        eval.evaluateModel(treeModel,data);
        System.out.println("correct "+ correctCount +" from "+total);
        System.out.println("Incorrect "+ incorrectCount +" from "+total);

        System.out.println("pct correct: "+eval.pctCorrect());
        System.out.println("pct incorrect: "+eval.pctIncorrect());
        System.out.println("pct precision: "+eval.weightedPrecision());

        // display classifier
        final javax.swing.JFrame jf =
                new javax.swing.JFrame("Weka Classifier Tree Visualizer: J48");
        jf.setSize(1500,800);
        jf.getContentPane().setLayout(new BorderLayout());
        TreeVisualizer tv = new TreeVisualizer(null,
                (treeModel).graph(),
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
}
