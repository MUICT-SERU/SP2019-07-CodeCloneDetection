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
        int id=1;
        for(Method m : methodList){
            m.setId(id);
            id++;
            m.writeFile();
        }
//        boolean readVector = false;
//        String s = null;
//        try {
//
//            // run the Unix "ps -ef" command
//            // using the Runtime exec method:/
//            Process p = Runtime.getRuntime().exec("python3 code2vec/code2vec.py --load code2vec/models/java14_model/saved_model_iter8.release --predict --export_code_vectors");
//
//            BufferedReader stdInput = new BufferedReader(new
//                    InputStreamReader(p.getInputStream()));
//
//            BufferedReader stdError = new BufferedReader(new
//                    InputStreamReader(p.getErrorStream()));
//
//            // read the output from the command
//            System.out.println("Here is the standard output of the command:\n");
//            while ((s = stdInput.readLine()) != null) {
//                System.out.println(s);
//
//                if(s.contains("Code vector")){
//                    readVector = true;
//                }
//            }
//
//            // read any errors from the attempted command
//            System.out.println("Here is the standard error of the command (if any):\n");
//            while ((s = stdError.readLine()) != null) {
//                System.out.println(s);
//            }
//
////            System.exit(0);
//        }
//        catch (IOException e) {
//            System.out.println("exception happened - here's what I know: ");
//            e.printStackTrace();
//            System.exit(-1);
//        }
        BufferedReader csvReader = new BufferedReader(new FileReader("/Users/sidekoiii/Documents/SP2019-DoNotCopy/MerryEngine/c2vVector.csv"));
        String row;
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");
//            System.out.println(data[0]);
            if(!data[0].equalsIgnoreCase("MethodID")){
                Method methodToAssign = methodList.get(Integer.parseInt(data[0])-1);
                if(Integer.parseInt(data[0]) == methodToAssign.getId()){
                    methodToAssign.setCode2vecVector(data[1],cmd.getCode2vecSize());
                }
            }


        }
        csvReader.close();

        List<MethodPair> methodPairList = new ArrayList<MethodPair>();
        for(int i = 0 ; i < methodList.size();i++){
            for(int j=i+1 ; j<methodList.size();j++){
                //create size filter
                //with turn on and off control
                int lineDiff = Math.abs(methodList.get(i).getLineOfCode()-methodList.get(j).getLineOfCode());
                if (useSizeFilter == true) {
                    int m1l = methodList.get(i).getTokenNo();
                    int m2l = methodList.get(j).getTokenNo();
                    double T = cmd.getSizeFilterThreshold();
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
//        System.out.println("Sample Metric ... \n" + methodList.get(5).getMetricsAsString());
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
