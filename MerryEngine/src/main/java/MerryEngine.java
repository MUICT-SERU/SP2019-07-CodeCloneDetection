import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.sql.Timestamp;

public class MerryEngine {
    public static void main(String[] args) throws Exception {
        Date startDate= new Date();
        long startTime = startDate.getTime();
        CommandLineArgument cmd = new CommandLineArgument(args);
        System.out.println("running...");
        System.out.println("Input : " + cmd.getInputSource());
        System.out.println("Size filter : "+ cmd.isSizeFilter());
        boolean useSizeFilter = cmd.isSizeFilter();
        final File folder = new File(cmd.getInputSource());
        List<String> fileList = new ArrayList<String>();
        search(".*\\.java", folder, fileList);
        List<Method> methodList = new ArrayList<Method>();
        System.out.println("Java files : "+fileList.size());
        for (String f : fileList) {
//            File file = new File(s);
//            BufferedReader br = new BufferedReader(new FileReader(file));
//            String st;
//            while ((st = br.readLine()) != null){
//                System.out.println(st);
//            }
            methodList.addAll(new JavaMethodParser(f).parseMethod());
        }
//        for(Method m : methodList){
//            System.out.println(m.getSourceCode());
//        }
//        Method exampleMethod = methodList.get(10);
//        System.out.println("Method 1 : "+ exampleMethod.getMethodName());
//        System.out.println(exampleMethod.getFilePath()+"   "+exampleMethod.getStartLine()+"   "+exampleMethod.getEndLine());
//        System.out.println(exampleMethod.getSourceCode());
//        System.out.println("Method list size : "+methodList.size());
//        System.out.println("Number of token : "+exampleMethod.getTokenNo()+" with unique : "+exampleMethod.getUniqueTokenNo());
//        System.out.println("Number of Identifier : "+exampleMethod.getIdentifierNo()+" with unique : "+exampleMethod.getUniqueIdentifierNo());
//        System.out.println("Number of Operator: "+exampleMethod.getOperatorNo()+" with unique : "+exampleMethod.getUniqueOperatorNo());
//        System.out.println("Return Type : "+exampleMethod.getReturnType());
        List<MethodPair> methodPairList = new ArrayList<MethodPair>();
        for(int i = 0 ; i < methodList.size();i++){
            for(int j=i+1 ; j<methodList.size();j++){
                //create size filter
                //with turn on and off control
                int lineDiff = Math.abs(methodList.get(i).getLineOfCode()-methodList.get(j).getLineOfCode());
                if (useSizeFilter == true) {
                    int m1l = methodList.get(i).getTokenNo();
                    int m2l = methodList.get(j).getTokenNo();
                    double T = 0.8;
                    if(m1l * T <= m2l && m2l <= m1l/T){
                        MethodPair methodPair = new MethodPair(methodList.get(i),methodList.get(j));
                        methodPairList.add(methodPair);
                    }
                }else{
                    MethodPair methodPair = new MethodPair(methodList.get(i),methodList.get(j));
                    methodPairList.add(methodPair);
//                    System.out.println("method name score of method "+methodPair.getMethod1().getMethodName()+" and "+methodPair.getMethod2().getMethodName()+" is "+methodPair.getSimilarMethodNameScore());
                }


                //end size filter

//                System.out.println((i*methodList.size())+j);
            }
        }
        System.out.println("Sample Metric ... \n" + methodList.get(5).getMetricsAsString());
        System.out.println("Method List size : " + methodList.size());
        System.out.println("Method Pair List size : " + methodPairList.size());
        Date endDate= new Date();
        long endTime = endDate.getTime();
        long runTime = endTime-startTime;
        Timestamp sts = new Timestamp(startTime);
        Timestamp ets = new Timestamp(endTime);
        System.out.println("Start Time Stamp: " + sts);
        System.out.println("End Time Stamp: " + ets);
        System.out.println("Run time = "+ runTime /1000.00+ "sec");
    }

    public static void search(final String pattern, final File folder, List<String> result) {
        for (final File f : folder.listFiles()) {

            if (f.isDirectory()) {
                search(pattern, f, result);
            }

            if (f.isFile()) {
                if (f.getName().matches(pattern)) {
                    result.add(f.getAbsolutePath());
                }
            }

        }
    }

}
