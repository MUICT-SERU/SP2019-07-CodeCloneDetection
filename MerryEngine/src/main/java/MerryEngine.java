
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
        List<String> fileList = new ArrayList<String>();
        search(".*\\.java", folder, fileList);
        List<Method> methodList = new ArrayList<Method>();
        HashMap<MultiKey,Method> methodHashMap = new HashMap<>();
        System.out.println("Java files : "+fileList.size());
        for (String f : fileList) {
//            File file = new File(s);
//            BufferedReader br = new BufferedReader(new FileReader(file));
//            String st;
//            while ((st = br.readLine()) != null){
//                System.out.println(st);
//            }
//            methodList.addAll(new JavaMethodParser(f).parseMethod());
            methodHashMap.putAll(new JavaMethodParser(f).parseMethod());
        }
        int id=1;
        for (Map.Entry<MultiKey, Method> entry : methodHashMap.entrySet()) {
            Method m = entry.getValue();
            m.setId(id);
            id++;
            methodList.add(m);
            m.writeFile();
        }
        boolean readVector = false;
        String s = null;
        try {

            // run the Unix "ps -ef" command
            // using the Runtime exec method:/
            Process p = Runtime.getRuntime().exec("python3 code2vec/code2vec.py --load code2vec/models/java14_model/saved_model_iter8.release --predict --export_code_vectors");

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(p.getErrorStream()));

            // read the output from the command
            System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);

                if(s.contains("Code vector")){
                    readVector = true;
                }
            }

            // read any errors from the attempted command
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }

        }
        catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
            System.exit(-1);
        }

        BufferedReader csvReader = new BufferedReader(new FileReader("/Users/sidekoiii/Documents/SP2019-DoNotCopy/MerryEngine/c2vVector.csv"));
        String row;
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");
            Method methodToAssign = methodList.get(Integer.parseInt(data[0])-1);
            if(Integer.parseInt(data[0]) == methodToAssign.getId()){
                methodToAssign.setCode2vecVector(data[1],cmd.getCode2vecSize());
            }
        }
        csvReader.close();
        List<Method> removeList = new ArrayList<>();
        for(Method rm : methodList){
            if(rm.isVectorSet()==false){
                removeList.add(rm);
            }
        }
        methodList.removeAll(removeList);
        System.out.println("Method List size : " + methodList.size());
      //method paring
        List<MethodPair> methodPairList = new ArrayList<MethodPair>();
        if(cmd.isTraining()){
            BufferedReader csvTrainingTrueReader = new BufferedReader(new FileReader("/Users/sidekoiii/Documents/SP2019-DoNotCopy/SelectedClones.csv"));
            String pair;
            while ((pair = csvTrainingTrueReader.readLine()) != null) {
                String[] data = row.split(",");
                Method m1 = methodHashMap.get(new MultiKey(data[0],Integer.parseInt(data[1]),Integer.parseInt(data[2])));
                Method m2 = methodHashMap.get(new MultiKey(data[3],Integer.parseInt(data[4]),Integer.parseInt(data[5])));
                methodPairList.add(new MethodPair(m1,m2,true));
            }
            BufferedReader csvTrainingFalseReader = new BufferedReader(new FileReader("/Users/sidekoiii/Documents/SP2019-DoNotCopy/SelectedFalseClones.csv"));
            while ((pair = csvTrainingTrueReader.readLine()) != null) {
                String[] data = row.split(",");
                Method m1 = methodHashMap.get(new MultiKey(data[0],Integer.parseInt(data[1]),Integer.parseInt(data[2])));
                Method m2 = methodHashMap.get(new MultiKey(data[3],Integer.parseInt(data[4]),Integer.parseInt(data[5])));
                methodPairList.add(new MethodPair(m1,m2,false));
            }
        }else {
            for(int i = 0 ; i < methodList.size();i++){
                for(int j=i+1 ; j< methodList.size();j++){
                    //size filter
                    //with turn on and off control
                    if (useSizeFilter == true) {
                        int m1l = methodList.get(i).getTokenNo();
                        int m2l = methodList.get(j).getTokenNo();
                        double T = cmd.getSizeFilterThreshold();
                        if(m1l * T <= m2l && m2l <= m1l/T){
//                            System.out.println(methodList.get(i).getId()+" and "+methodList.get(j).getId());
                            MethodPair methodPair = new MethodPair(methodList.get(i),methodList.get(j));
                            methodPairList.add(methodPair);
                        }
                    }else{
                        MethodPair methodPair = new MethodPair(methodList.get(i),methodList.get(j));
                        methodPairList.add(methodPair);
                    }
                    //end size filter
                }
            }
        }
if(cmd.isTraining()){
    FileWriter csvWriter = new FileWriter("/Users/sidekoiii/Documents/SP2019-DoNotCopy/MerryEngine/mertics.csv");
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
    csvWriter.append("SimilarFileNameScore");
    csvWriter.append(",");
    csvWriter.append("SimilarMethodNameScore");
    csvWriter.append(",");
    csvWriter.append("SameReturnType");
    csvWriter.append(",");
    for(int j=0 ; j<12 ; j++){
        csvWriter.append("c2v_"+j);
        csvWriter.append(",");
    }
    csvWriter.append("Decision");
    csvWriter.append("\n");
    for (int i = 0; i<methodPairList.size();i++){
        MethodPair mp = methodPairList.get(i);
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
        csvWriter.write(mp.isDecision()+"");
        csvWriter.append("\n ");
    }
    csvWriter.flush();
    csvWriter.close();
}else{
    FileWriter csvWriter = new FileWriter("/Users/sidekoiii/Documents/SP2019-DoNotCopy/MerryEngine/mertics.csv");
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
    csvWriter.append("SimilarFileNameScore");
    csvWriter.append(",");
    csvWriter.append("SimilarMethodNameScore");
    csvWriter.append(",");
    csvWriter.append("SameReturnType");
    csvWriter.append(",");
    for(int j=0 ; j<11 ; j++){
        csvWriter.append("c2v_"+j);
        csvWriter.append(",");
    }
    csvWriter.append("c2v_11");
    csvWriter.append("\n");
    for (int i = 0; i<methodPairList.size();i++){
        MethodPair mp = methodPairList.get(i);
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
        csvWriter.write(c2v[11]+"");
        csvWriter.append("\n ");
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