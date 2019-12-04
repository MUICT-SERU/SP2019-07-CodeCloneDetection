import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MerryEngine {
    public static void main(String[] args) throws Exception {
        CommandLineArgument cmd = new CommandLineArgument(args);
        System.out.println("running...");
        System.out.println("Input : " + cmd.getInputSource());

        final File folder = new File(cmd.getInputSource());
        List<String> fileList = new ArrayList<String>();
        search(".*\\.java", folder, fileList);
        List<Method> methodList = new ArrayList<Method>();
        System.out.println("Java files : "+fileList.size());
        for (String f : fileList) {
//            System.out.println(s);
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
                MethodPair methodPair = new MethodPair(methodList.get(i),methodList.get(j));
                methodPairList.add(methodPair);
//                System.out.println((i*methodList.size())+j);
            }
        }
        System.out.println("Method Pair List size : " + methodPairList.size());
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
