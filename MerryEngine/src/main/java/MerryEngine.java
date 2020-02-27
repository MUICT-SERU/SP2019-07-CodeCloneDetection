
import java.io.*;
import java.util.*;
import java.sql.Timestamp;

public class MerryEngine {
    public static void main(String[] args) throws Exception {
        Date startDate= new Date();
        long startTime = startDate.getTime();
        CommandLineArgument cmd = new CommandLineArgument(args);
        System.out.println("running...");
        System.out.println("Input : " + cmd.getInputSource());
        System.out.println("Size filter : "+ cmd.isSizeFilter());
        System.out.println("code2vec Size : "+cmd.getCode2vecSize());
        System.out.println("testing mode : "+cmd.isTraining());
        boolean useSizeFilter = cmd.isSizeFilter();
        final File folder = new File(cmd.getInputSource());
        ArrayList<String> fileList = new ArrayList<String>();
        search(".*\\.java", folder, fileList);
        List<Method> methodList = new ArrayList<>();
        HashMap<String,Method> methodHashMap = new HashMap<>();
        System.out.println("Java files : "+fileList.size());
        for (String f : fileList) {
//            methodList.addAll(new JavaMethodParser(f).parseMethod());
            methodHashMap.putAll(new JavaMethodParser(f).parseMethod());
        }
        System.out.println("Done creating Hashmap");
//        int id=1;
        //write each method to .java file for code2vec in folder
//        System.out.println("Start writing files");
//        for (Map.Entry<String, Method> entry : methodHashMap.entrySet()) {
//            Method m = entry.getValue();
////            m.setId(id);
////            id++;
////            methodList.add(m);
//            m.writeFile();
//        }
//        System.out.println("Done Write files");
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
//        }
//        catch (IOException e) {
//            System.out.println("exception happened - here's what I know: ");
//            e.printStackTrace();
//            System.exit(-1);
//        }
//        System.out.println("Done get vector from code2vec");
        if(cmd.isTraining()){
            HashMap<String,Method> selectedMethodHashMap = new HashMap<>();
            BufferedReader csvFilterTrueReader = new BufferedReader(new FileReader("SelectedClones.csv"));
            String line;
            while ((line = csvFilterTrueReader.readLine()) != null) {
                String[] data = line.split(",");
                Method m = methodHashMap.get(data[0].replace(".java","")+data[1]+data[2]);
                if(m!=null){
                    selectedMethodHashMap.put(data[0].replace(".java","")+data[1]+data[2],m);
                }
                m = methodHashMap.get(data[4].replace(".java","")+data[5]+data[6]);
                if(m!=null){
                    selectedMethodHashMap.put(data[4].replace(".java","")+data[5]+data[6],m);
                }
            }
            csvFilterTrueReader.close();
            BufferedReader csvFilterFalseReader = new BufferedReader(new FileReader("SelectedFalseClones.csv"));
            while ((line = csvFilterFalseReader.readLine()) != null) {
                String[] data = line.split(",");
                Method m = methodHashMap.get(data[0].replace(".java","")+data[1]+data[2]);
                if(m!=null){
                    selectedMethodHashMap.put(data[0].replace(".java","")+data[1]+data[2],m);
                }
                m = methodHashMap.get(data[4].replace(".java","")+data[5]+data[6]);
                if(m!=null){
                    selectedMethodHashMap.put(data[4].replace(".java","")+data[5]+data[6],m);
                }
            }
            csvFilterFalseReader.close();
            methodHashMap.clear();
            methodHashMap.putAll(selectedMethodHashMap);
            selectedMethodHashMap.clear();
        }

        int ch =0;
        BufferedReader csvReader = new BufferedReader(new FileReader("c2vVector.csv"));
        String row;
        while ((row = csvReader.readLine()) != null) {
            ch++;
//            if(ch %100 == 0){
//                System.out.println("estimate current line : "+ch);
//            }
            String[] data = row.split(",");
            String key = data[0].replace(".java","");
            Method methodToAssign = methodHashMap.get(key);
////            System.out.println(key+" --> key");
            if(methodToAssign!= null){
                methodToAssign.setCode2vecVector(data[1].trim(),cmd.getCode2vecSize());
            }
//            methodToAssign.setC2vVectorAsString(data[1].trim());
        }
        csvReader.close();
        System.out.println("Method hash size : " + methodHashMap.size());
      //method paring
        List<MethodPair> methodPairList = new ArrayList<>();
        int vecSize = cmd.getCode2vecSize();
        if(cmd.isTraining()){
            BufferedReader csvTrainingTrueReader = new BufferedReader(new FileReader("SelectedClones.csv"));
            String pair;
            int pairIDSetter = 0;
            while ((pair = csvTrainingTrueReader.readLine()) != null) {
                String[] data = pair.split(",");
                Method m1 = methodHashMap.get(data[0].replace(".java","")+data[1]+data[2]);
                Method m2 = methodHashMap.get(data[4].replace(".java","")+data[5]+data[6]);
                if(m1!=null&&m2!=null){
                    if(m1.isVectorSet()&&m2.isVectorSet()){
                        methodPairList.add(new MethodPair(pairIDSetter,m1,m2,true));
                        pairIDSetter++;
                    }
                }

            }
            csvTrainingTrueReader.close();
            BufferedReader csvTrainingFalseReader = new BufferedReader(new FileReader("SelectedFalseClones.csv"));
            while ((pair = csvTrainingFalseReader.readLine()) != null) {
                String[] data = pair.split(",");
                Method m1 = methodHashMap.get(data[0].replace(".java","")+data[1]+data[2]);
                Method m2 = methodHashMap.get(data[4].replace(".java","")+data[5]+data[6]);
                if(m1!=null&&m2!=null){
                    if(m1.isVectorSet()&&m2.isVectorSet()){
                        methodPairList.add(new MethodPair(pairIDSetter,m1,m2,false));
                        pairIDSetter++;
                    }
                }
            }
            csvTrainingFalseReader.close();
        }else {
            for (Map.Entry<String, Method> entry : methodHashMap.entrySet()) {
                methodList.add(entry.getValue());
            }
            int pairIDSetter= 0;
            for(int i = 0 ; i < methodList.size();i++){
                for(int j=i+1 ; j< methodList.size();j++){
                    //size filter
                    //with turn on and off control
                    if (useSizeFilter) {
                        int m1Tokens = methodList.get(i).getTokenNo();
                        int m2Tokens = methodList.get(j).getTokenNo();
                        double T = cmd.getSizeFilterThreshold();
                        if(m1Tokens * T <= m2Tokens && m2Tokens <= m1Tokens/T){
                            MethodPair methodPair = new MethodPair(pairIDSetter,methodList.get(i),methodList.get(j));
                            methodPairList.add(methodPair);
                            pairIDSetter++;
                        }
                    }else{
                        MethodPair methodPair = new MethodPair(pairIDSetter,methodList.get(i),methodList.get(j));
                        methodPairList.add(methodPair);
                        pairIDSetter++;
                    }
                    //end size filter
                }
            }
        }
if(cmd.isTraining()){
    FileWriter csvWriter = new FileWriter("trainModelMetrics.csv");
    csvWriter.append("PairID");
    csvWriter.append(",");
    csvWriter.append("DiffLOC");
    csvWriter.append(",");
    csvWriter.append("DiffIdentifierNo");
    csvWriter.append(",");
    csvWriter.append("DiffOperatorNo");
    csvWriter.append(",");
    csvWriter.append("DiffTokenNo");
    csvWriter.append(",");
    csvWriter.append("DiffTokenTypeDiversity");
    csvWriter.append(",");
    csvWriter.append("DiffUniqueIdentifierNo");
    csvWriter.append(",");
    csvWriter.append("DiffUniqueOperatorNo");
    csvWriter.append(",");
    csvWriter.append("DiffUniqueTokenNo");
    csvWriter.append(",");
    csvWriter.append("DiffFileNameScore");
    csvWriter.append(",");
    csvWriter.append("DiffMethodNameScore");
    csvWriter.append(",");
    csvWriter.append("IsSameReturnType");
    csvWriter.append(",");
    for(int j=0 ; j<12 ; j++){
        csvWriter.append("c2v_"+j);
        csvWriter.append(",");
    }
    csvWriter.append("Decision");
    for (int i = 0; i<methodPairList.size();i++){
        MethodPair mp = methodPairList.get(i);
        csvWriter.append("\n ");
        csvWriter.append(mp.getId()+"");
        csvWriter.append(",");
        csvWriter.append(mp.getDiffLOC()+"");
        csvWriter.append(",");
        csvWriter.append(mp.getDiffIdentifierNo()+"");
        csvWriter.append(",");
        csvWriter.append(mp.getDiffOperatorNo()+"");
        csvWriter.append(",");
        csvWriter.append(mp.getDiffTokenNo()+"");
        csvWriter.append(",");
        csvWriter.append(mp.getDiffTokenTypeDiversity()+"");
        csvWriter.append(",");
        csvWriter.append(mp.getDiffUniqueIdentifierNo()+"");
        csvWriter.append(",");
        csvWriter.append(mp.getDiffUniqueOperatorNo()+"");
        csvWriter.append(",");
        csvWriter.append(mp.getDiffUniqueTokenNo()+"");
        csvWriter.append(",");
        csvWriter.append(mp.getSimilarFileNameScore()+"");
        csvWriter.append(",");
        csvWriter.append(mp.getSimilarMethodNameScore()+"");
        csvWriter.append(",");
        csvWriter.append(mp.isSameReturnType()+"");
        csvWriter.append(",");
        double[] c2v = mp.getCode2VecSimilarityScore();
        for(int j=0 ; j<12 ; j++){
            csvWriter.append(c2v[j]+"");
            csvWriter.append(",");
        }
        csvWriter.append(mp.isDecision()+"");

    }
    csvWriter.flush();
    csvWriter.close();
}else{
    FileWriter csvWriter = new FileWriter("metrics.csv");
    csvWriter.append("PairID");
    csvWriter.append(",");
    csvWriter.append("DiffLOC");
    csvWriter.append(",");
    csvWriter.append("DiffIdentifierNo");
    csvWriter.append(",");
    csvWriter.append("DiffOperatorNo");
    csvWriter.append(",");
    csvWriter.append("DiffTokenNo");
    csvWriter.append(",");
    csvWriter.append("DiffTokenTypeDiversity");
    csvWriter.append(",");
    csvWriter.append("DiffUniqueIdentifierNo");
    csvWriter.append(",");
    csvWriter.append("DiffUniqueOperatorNo");
    csvWriter.append(",");
    csvWriter.append("DiffUniqueTokenNo");
    csvWriter.append(",");
    csvWriter.append("DiffFileNameScore");
    csvWriter.append(",");
    csvWriter.append("DiffMethodNameScore");
    csvWriter.append(",");
    csvWriter.append("IsSameReturnType");
    csvWriter.append(",");
    for(int j=0 ; j<11 ; j++){
        csvWriter.append("c2v_"+j);
        csvWriter.append(",");
    }
    csvWriter.append("c2v_11,Decision");
    for (int i = 0; i<methodPairList.size();i++){
        MethodPair mp = methodPairList.get(i);
        csvWriter.append("\n ");
        csvWriter.append(mp.getId()+"");
        csvWriter.append(",");
        csvWriter.append(mp.getDiffLOC()+"");
        csvWriter.append(",");
        csvWriter.append(mp.getDiffIdentifierNo()+"");
        csvWriter.append(",");
        csvWriter.append(mp.getDiffOperatorNo()+"");
        csvWriter.append(",");
        csvWriter.append(mp.getDiffTokenNo()+"");
        csvWriter.append(",");
        csvWriter.append(mp.getDiffTokenTypeDiversity()+"");
        csvWriter.append(",");
        csvWriter.append(mp.getDiffUniqueIdentifierNo()+"");
        csvWriter.append(",");
        csvWriter.append(mp.getDiffUniqueOperatorNo()+"");
        csvWriter.append(",");
        csvWriter.append(mp.getDiffUniqueTokenNo()+"");
        csvWriter.append(",");
        csvWriter.append(mp.getSimilarFileNameScore()+"");
        csvWriter.append(",");
        csvWriter.append(mp.getSimilarMethodNameScore()+"");
        csvWriter.append(",");
        csvWriter.append(mp.isSameReturnType()+"");
        csvWriter.append(",");
        double[] c2v = mp.getCode2VecSimilarityScore();
        for(int j=0 ; j<11 ; j++){
            csvWriter.append(c2v[j]+"");
            csvWriter.append(",");
        }
        csvWriter.append(c2v[11]+",?");
    }
    csvWriter.flush();
    csvWriter.close();
}
//        System.out.println("Sample Metric ... \n" + methodList.get(5).getMetricsAsString());
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
