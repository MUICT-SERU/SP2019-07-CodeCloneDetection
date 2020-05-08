import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.*;
import java.sql.Timestamp;


public class MerryEngine {
    public static void main(String[] args) throws Exception {
        int totalLOC = 0;
        int totalMethods = 0;
        Date startDate = new Date();
        long startTime = startDate.getTime();
        CommandLineArgument cmd = new CommandLineArgument(args);
        System.out.println("running...");
        System.out.println("Input : " + cmd.getInputSource());
        System.out.println("Size filter : " + cmd.isSizeFilter());
        System.out.println("code2vec Size : " + cmd.getCode2vecSize());
        System.out.println("training mode : " + cmd.isTraining());
        System.out.println("working Directory : "+cmd.getWorkingDir());
        System.out.println("Model : "+cmd.getModel());
        System.out.println("Metrics : Syntactic "+cmd.isSyntactic()+" , Semantic "+cmd.isSemantic());
//        FileUtils.cleanDirectory(new File(cmd.getWorkingDir()));
        FileUtils.forceMkdir(new File(cmd.getWorkingDir() + "/assets"));
        FileUtils.forceMkdir(new File(cmd.getWorkingDir() + "/JavaMethods"));
        FileUtils.forceMkdir(new File(cmd.getWorkingDir() + "/models"));
        FileUtils.forceMkdir(new File(cmd.getWorkingDir() + "/result"));
        boolean useSizeFilter = cmd.isSizeFilter();
        final File folder = new File(cmd.getInputSource());
        ArrayList<String> fileList = new ArrayList<String>();
        search(".*\\.java", folder, fileList);
        List<Method> methodList = new ArrayList<>();
        HashMap<String, Method> methodHashMap = new HashMap<>();
        System.out.println("Java files : " + fileList.size());
        for (String f : fileList) {
//            methodList.addAll(new JavaMethodParser(f).parseMethod());
            methodHashMap.putAll(new JavaMethodParser(f).parseMethod());
        }
        System.out.println("Done creating Hashmap");
//        write each method to .java file for code2vec in folder
        FileUtils.cleanDirectory(new File(cmd.getWorkingDir()+"/JavaMethods"));
        System.out.println("Start writing files");
        for (Map.Entry<String, Method> entry : methodHashMap.entrySet()) {
            Method m = entry.getValue();
            m.writeFile(cmd.getWorkingDir()+"/JavaMethods");
        }
        System.out.println("Done Write files");
        boolean readVector = false;
//        FileUtils.forceDelete(new File("assets/c2vVector.csv"));
        String s = null;
        try {
            // run the Unix "ps -ef" command
            // using the Runtime exec method:/
            Process p = Runtime.getRuntime().exec("python "+ cmd.getC2vPath()+ "/code2vec.py --load "+cmd.getC2vPath()+"/models/java14_model/saved_model_iter8.release --predict --export_code_vectors");

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
        System.out.println("Done get vector from code2vec");

        if (cmd.isTraining()) {
            HashMap<String,String> originalFileNameHashMap = new HashMap<>();
            HashMap<String, Method> selectedMethodHashMap = new HashMap<>();
            BufferedReader csvFilterTrueReader = new BufferedReader(new FileReader("/Users/sidekoiii/Desktop/testdataCSV/trainMultiClass.csv"));
            String line;
            while ((line = csvFilterTrueReader.readLine()) != null) {
                String[] data = line.split(",");
                originalFileNameHashMap.put(data[1],data[0]);
                Method m = methodHashMap.get(data[1].replace(".java", "") + data[2] + data[3]);
                if (m != null) {
                    selectedMethodHashMap.put(data[1].replace(".java", "") + data[2] + data[3], m);
                }
                originalFileNameHashMap.put(data[6],data[5]);
                m = methodHashMap.get(data[6].replace(".java", "") + data[7] + data[8]);
                if (m != null) {
                    selectedMethodHashMap.put(data[6].replace(".java", "") + data[7] + data[8], m);
                }
            }
            csvFilterTrueReader.close();
//            BufferedReader csvFilterFalseReader = new BufferedReader(new FileReader("assets/FalseClones.csv"));
//            while ((line = csvFilterFalseReader.readLine()) != null) {
//                String[] data = line.split(",");
//                originalFileNameHashMap.put(data[1],data[0]);
//                Method m = methodHashMap.get(data[1].replace(".java", "") + data[2] + data[3]);
//                if (m != null) {
//                    selectedMethodHashMap.put(data[1].replace(".java", "") + data[2] + data[3], m);
//                }
//                originalFileNameHashMap.put(data[6],data[5]);
//                m = methodHashMap.get(data[6].replace(".java", "") + data[7] + data[8]);
//                if (m != null) {
//                    selectedMethodHashMap.put(data[6].replace(".java", "") + data[7] + data[8], m);
//                }
//            }
//            csvFilterFalseReader.close();
            methodHashMap.clear();
            methodHashMap.putAll(selectedMethodHashMap);
            selectedMethodHashMap.clear();

            for (Map.Entry<String, Method> entry : methodHashMap.entrySet()) {
                Method method = entry.getValue();
                String filePathOrigin = originalFileNameHashMap.get(method.getFileName()+".java");
                String[] path = filePathOrigin.split("/");
                String fileNameOrigin = path[path.length-1].replace(".java","");
                method.setFileName(fileNameOrigin);
            }
        }

        int ch = 0;
        BufferedReader csvReader = new BufferedReader(new FileReader(cmd.getWorkingDir()+"/assets/c2vVector.csv"));
        String row;
        while ((row = csvReader.readLine()) != null) {
            ch++;
            String[] data = row.split(",");
            String key = data[0].replace(".java", "");
            Method methodToAssign = methodHashMap.get(key);
            try {
                methodToAssign.setCode2vecVector(data[1].trim(), cmd.getCode2vecSize());
            }catch (Exception e){
//                System.out.println(e);
            }
        }
        csvReader.close();
        System.out.println("Method hash size : " + methodHashMap.size());
        //method paring
        List<MethodPair> methodPairList = new ArrayList<>();
        int vecSize = cmd.getCode2vecSize();
        if (cmd.isTraining()) {
            BufferedReader csvTrainingTrueReader = new BufferedReader(new FileReader("/Users/sidekoiii/Desktop/testdataCSV/trainMultiClass.csv"));
            String pair;
            int pairIDSetter = 0;
            while ((pair = csvTrainingTrueReader.readLine()) != null) {
                String[] data = pair.split(",");
                Method m1 = methodHashMap.get(data[1].replace(".java", "") + data[2] + data[3]);
                Method m2 = methodHashMap.get(data[6].replace(".java", "") + data[7] + data[8]);
                if (m1 != null && m2 != null) {
                    if (m1.isVectorSet() && m2.isVectorSet()) {
                        MethodPair newMethodPair = new MethodPair(pairIDSetter,m1,m2);
//                        newMethodPair.setDecision(true);
                        if(data[data.length-1].equalsIgnoreCase("true")){
                            newMethodPair.setType("Type-"+data[10]);
                        }else{
                            newMethodPair.setType("false");
                        }
                        methodPairList.add(newMethodPair);
                        pairIDSetter++;
                    }
                }

            }
//            csvTrainingTrueReader.close();
//            BufferedReader csvTrainingFalseReader = new BufferedReader(new FileReader("assets/FalseClones.csv"));
//            while ((pair = csvTrainingFalseReader.readLine()) != null) {
//                String[] data = pair.split(",");
//                Method m1 = methodHashMap.get(data[1].replace(".java", "") + data[2] + data[3]);
//                Method m2 = methodHashMap.get(data[6].replace(".java", "") + data[7] + data[8]);
//                if (m1 != null && m2 != null) {
//                    if (m1.isVectorSet() && m2.isVectorSet()) {
//                        MethodPair newMethodPair = new MethodPair(pairIDSetter,m1,m2);
//                        newMethodPair.setDecision(false);
//                        methodPairList.add(newMethodPair);
//                        pairIDSetter++;
//                    }
//                }
//            }
//            csvTrainingFalseReader.close();
        } else {
//            this part is for select only test( ground truth pair)
//            HashMap<String,String> originalFileNameHashMap = new HashMap<>();
//            HashMap<String, Method> selectedMethodHashMap = new HashMap<>();
//            BufferedReader csvTestingReader = new BufferedReader(new FileReader("/Users/sidekoiii/Desktop/testdataCSV/testMultiClass.csv"));
//            String pair;
//            while ((pair = csvTestingReader.readLine()) != null){
//                String[] data = pair.split(",");
//                originalFileNameHashMap.put(data[0],data[1]);
//                Method m = methodHashMap.get(data[0].replace(".java", "") + data[2] + data[3]);
//                if (m != null) {
//                    selectedMethodHashMap.put(data[0].replace(".java", "") + data[2] + data[3], m);
//                }
//                originalFileNameHashMap.put(data[5],data[6]);
//                m = methodHashMap.get(data[5].replace(".java", "") + data[7] + data[8]);
//                if (m != null) {
//                    selectedMethodHashMap.put(data[5].replace(".java", "") + data[7] + data[8], m);
//                }
//            }
//            methodHashMap.clear();
//            methodHashMap.putAll(selectedMethodHashMap);
//            selectedMethodHashMap.clear();
//            System.out.println("Method hash size after filter: " + methodHashMap.size());
//            csvTestingReader.close();
//            for (Map.Entry<String, Method> entry : methodHashMap.entrySet()) {
//                Method method = entry.getValue();
//                String filePathOrigin = originalFileNameHashMap.get(method.getFileName()+".java");
//                String[] path = filePathOrigin.split("/");
//                String fileNameOrigin = path[path.length-1].replace(".java","");
//                method.setFileName(fileNameOrigin);
//            }
//            BufferedReader csvTestingPairingReader = new BufferedReader(new FileReader("/Users/sidekoiii/Desktop/testdataCSV/testMultiClass.csv"));
//            int pairIDSetter = 0;
//            int trueCounter = 0;
//            while ((pair = csvTestingPairingReader.readLine()) != null) {
//                String[] data = pair.split(",");
//                Method m1 = methodHashMap.get(data[0].replace(".java", "") + data[2] + data[3]);
//                Method m2 = methodHashMap.get(data[5].replace(".java", "") + data[7] + data[8]);
////                if(Boolean.parseBoolean(data[data.length-1])==false||(Boolean.parseBoolean(data[data.length-1])==true&&Integer.parseInt(data[10])==3&&Double.parseDouble(data[11])>=0.7&&Double.parseDouble(data[11])<0.9)){
//                    if (m1 != null && m2 != null) {
//                        if (m1.isVectorSet() && m2.isVectorSet()) {
//                            MethodPair newMethodPair = new MethodPair(pairIDSetter,m1,m2);
////                            newMethodPair.setDecision(Boolean.parseBoolean(data[data.length-1]));
//                            if(data[data.length-1].equalsIgnoreCase("true")){
//                                newMethodPair.setType("Type-"+data[10]);
//                            }else{
//                                newMethodPair.setType("false");
//                            }
//                            methodPairList.add(newMethodPair);
//                            pairIDSetter++;
//                            if(Boolean.parseBoolean(data[data.length-1])==true){
//                                trueCounter++;
//                            }
//                        }
//                    }
////                }
//            }
//            System.out.println("number of true : "+ trueCounter);
//            csvTestingPairingReader.close();
//            end part is for select only test( ground truth pair)
//            paring for normal detection

            for (Map.Entry<String, Method> entry : methodHashMap.entrySet()) {
                methodList.add(entry.getValue());
                totalLOC += entry.getValue().getLineOfCode();
            }
            System.out.println("total LOC : "+totalLOC);
            totalMethods = methodList.size();
            System.out.println("total method : "+totalMethods);
            int pairIDSetter = 0;
            for (int i = 0; i < methodList.size(); i++) {
                for (int j = i + 1; j < methodList.size(); j++) {
                    //size filter
                    //with turn on and off control
                    if (useSizeFilter) {
                        int m1Tokens = methodList.get(i).getTokenNo();
                        int m2Tokens = methodList.get(j).getTokenNo();
                        int m1Loc = methodList.get(i).getLineOfCode();
                        int m2Loc = methodList.get(j).getLineOfCode();
                        double T = cmd.getSizeFilterThreshold();
                        if (m1Tokens * T <= m2Tokens && m2Tokens <= m1Tokens / T && m1Loc > cmd.getMinLOC() && m2Loc > cmd.getMinLOC()) {
                            try{
                                MethodPair methodPair = new MethodPair(pairIDSetter, methodList.get(i), methodList.get(j));
                                methodPairList.add(methodPair);
                                pairIDSetter++;
                            }catch (Exception e){
//                                System.out.println(e);
                            }
                        }
                    } else {
                        try{
                            MethodPair methodPair = new MethodPair(pairIDSetter, methodList.get(i), methodList.get(j));
                            methodPairList.add(methodPair);
                            pairIDSetter++;
                        }catch (Exception e){
//                                System.out.println(e);
                        }

                    }
                    //end size filter
                }
            }

        }
        if (cmd.isTraining()) {
            FileWriter csvWriter = new FileWriter(cmd.getWorkingDir()+"/assets/trainModelMetrics.csv");
            csvWriter.write("PairID");
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
            for (int j = 0; j < 12; j++) {
                csvWriter.append("c2v_" + j);
                csvWriter.append(",");
            }
            csvWriter.append("Decision");
            for (int i = 0; i < methodPairList.size(); i++) {
                MethodPair mp = methodPairList.get(i);
                csvWriter.append("\n ");
                csvWriter.append(mp.getId() + "");
                csvWriter.append(",");
                csvWriter.append(mp.getDiffLOC() + "");
                csvWriter.append(",");
                csvWriter.append(mp.getDiffIdentifierNo() + "");
                csvWriter.append(",");
                csvWriter.append(mp.getDiffOperatorNo() + "");
                csvWriter.append(",");
                csvWriter.append(mp.getDiffTokenNo() + "");
                csvWriter.append(",");
                csvWriter.append(mp.getDiffTokenTypeDiversity() + "");
                csvWriter.append(",");
                csvWriter.append(mp.getDiffUniqueIdentifierNo() + "");
                csvWriter.append(",");
                csvWriter.append(mp.getDiffUniqueOperatorNo() + "");
                csvWriter.append(",");
                csvWriter.append(mp.getDiffUniqueTokenNo() + "");
                csvWriter.append(",");
                csvWriter.append(mp.getSimilarFileNameScore() + "");
                csvWriter.append(",");
                csvWriter.append(mp.getSimilarMethodNameScore() + "");
                csvWriter.append(",");
                csvWriter.append(mp.isSameReturnType() + "");
                csvWriter.append(",");
                double[] c2v = mp.getCode2VecSimilarityScore();
                for (int j = 0; j < 12; j++) {
                    csvWriter.append(c2v[j] + "");
                    csvWriter.append(",");
                }
                csvWriter.append(mp.getType() + "");

            }
            csvWriter.flush();
            csvWriter.close();
        } else {
            FileWriter csvWriter = new FileWriter(cmd.getWorkingDir()+"/assets/metrics.csv");
            csvWriter.write("PairID");
            csvWriter.append(",");
            if(cmd.isSyntactic()){
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
            }
            if(cmd.isSemantic()){
                for (int j = 0; j < 12; j++) {
                    csvWriter.append("c2v_" + j);
                    csvWriter.append(",");
                }
            }
            csvWriter.append("Decision");
            for (int i = 0; i < methodPairList.size(); i++) {
                MethodPair mp = methodPairList.get(i);
                csvWriter.append("\n ");
                csvWriter.append(mp.getId() + "");
                csvWriter.append(",");
                if(cmd.isSyntactic()){
                    csvWriter.append(mp.getDiffLOC() + "");
                    csvWriter.append(",");
                    csvWriter.append(mp.getDiffIdentifierNo() + "");
                    csvWriter.append(",");
                    csvWriter.append(mp.getDiffOperatorNo() + "");
                    csvWriter.append(",");
                    csvWriter.append(mp.getDiffTokenNo() + "");
                    csvWriter.append(",");
                    csvWriter.append(mp.getDiffTokenTypeDiversity() + "");
                    csvWriter.append(",");
                    csvWriter.append(mp.getDiffUniqueIdentifierNo() + "");
                    csvWriter.append(",");
                    csvWriter.append(mp.getDiffUniqueOperatorNo() + "");
                    csvWriter.append(",");
                    csvWriter.append(mp.getDiffUniqueTokenNo() + "");
                    csvWriter.append(",");
                    csvWriter.append(mp.getSimilarFileNameScore() + "");
                    csvWriter.append(",");
                    csvWriter.append(mp.getSimilarMethodNameScore() + "");
                    csvWriter.append(",");
                    csvWriter.append(mp.isSameReturnType() + "");
                    csvWriter.append(",");
                }
                if(cmd.isSemantic()){
                    double[] c2v = mp.getCode2VecSimilarityScore();
                    for (int j = 0; j < 12; j++) {
                        csvWriter.append(c2v[j] + "");
                        csvWriter.append(",");
                    }
                }
                if(mp.getId()%2==0){
                    csvWriter.append("true");
                }else {
                    csvWriter.append("false");
                }
//                csvWriter.append(mp.getType()+"");
            }
            csvWriter.flush();
            csvWriter.close();
        }

        System.out.println("Method Pair List size : " + methodPairList.size());
        if (cmd.isTraining()) {
            String[] wekaArgs = {"-tree_depth",cmd.getTreeDepth()+""};
            WekaTraining.main(wekaArgs);
        } else {
            Set<Method> cloneMethodSet = new HashSet<>();
            String[] predictArgs = {"-workingDir",cmd.getWorkingDir(),"-Syntactic",cmd.booleanString(cmd.isSyntactic()),"-Semantic",cmd.booleanString(cmd.isSemantic()),"-model",cmd.getModel()};
            Weka.main(predictArgs);
            String prediction;
//            MongoClient mongoClient = new MongoClient(cmd.getDBUrl(), cmd.getDBPort());
//            DB database = mongoClient.getDB(cmd.getDBName());
//            DBCollection collection = database.getCollection("Result");
//            BasicDBObject document;
            BufferedReader modelResultReader = new BufferedReader(new FileReader(cmd.getWorkingDir()+"/result/result.csv"));
            FileWriter cloneWriter = new FileWriter(cmd.getOutputSource());
            String result = "method1FileName,method1Start,Method1End,method1SourceCode,method2FileName,method2Start,Method2End,method2SourceCode";
            cloneWriter.write(result);
            System.out.println("Start Wrinting Result");
            while ((prediction = modelResultReader.readLine()) != null) {
                String[] data = prediction.split(",");
                if (data[data.length - 1].equalsIgnoreCase("true")) {
                    int pairId = Integer.parseInt(data[0]);
                    if (pairId == methodPairList.get(pairId).getId()) {
                        Method m1 = methodPairList.get(pairId).getMethod1();
                        Method m2 = methodPairList.get(pairId).getMethod2();
                        cloneMethodSet.add(methodHashMap.get(m1.getId()));
                        cloneMethodSet.add(methodHashMap.get(m2.getId()));
                        result = "\n" + m1.getFilePath() + "," + m1.getStartLine() + "," + m1.getEndLine() + "," + "," + m2.getFilePath() + "," + m2.getStartLine() + "," + m2.getEndLine() + ",";
                        cloneWriter.append(result);
//                        document = new BasicDBObject();
//                        document.put("ExecutionID",cmd.getExecID());
//                        document.put("File1Path",m1.getFilePath());
//                        document.put("startLine1",m1.getStartLine());
//                        document.put("endLine1",m1.getEndLine());
//                        document.put("sourceCode1",m1.getSourceCode());
//                        document.put("File2Path",m2.getFilePath());
//                        document.put("startLine2",m2.getStartLine());
//                        document.put("endLine2",m2.getEndLine());
//                        document.put("sourceCode2",m2.getSourceCode());
//                        collection.insert(document);
                    }
                }
            }
            cloneWriter.flush();
            cloneWriter.close();
            System.out.println("Done Write Result");
            int cloneLOC = 0;
            for(Method m : cloneMethodSet){
                cloneLOC+= m.getLineOfCode();
            }
//            DBCollection infoCollection = database.getCollection("graphInfo");
//            BasicDBObject infoDocument = new BasicDBObject();
//            infoDocument.put("_id",cmd.getExecID());
//            infoDocument.put("total_loc",totalLOC);
//            infoDocument.put("total_methods",totalMethods);
//            infoDocument.put("clone_loc",cloneLOC);
//            infoDocument.put("clone_methods",cloneMethodSet.size());
//            infoCollection.insert(infoDocument);
//            mongoClient.close();
        }
        Date endDate = new Date();
        long endTime = endDate.getTime();
        long runTime = endTime - startTime;
        Timestamp sts = new Timestamp(startTime);
        Timestamp ets = new Timestamp(endTime);
        System.out.println("Start Time Stamp: " + sts);
        System.out.println("End Time Stamp: " + ets);
        System.out.println("Run time = " + runTime / 1000.00 + "sec");
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
