import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FFilter {
    public static void main(String[] args) throws Exception {
        CommandLineArgument cmd = new CommandLineArgument(args);
        System.out.println("running...");
        System.out.println("Input : " + cmd.getInputSource());

        final File folder = new File("/home/sidekoiii/"+ cmd.getInputSource());
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
            methodList.addAll(new MethodParser(f).parseMethod());
        }
//        for(Method m : methodList){
//            System.out.println(m.getSourceCode());
//        }
        System.out.println("Method 1 : "+ methodList.get(0).getMethodName());
        System.out.println(methodList.get(0).getFilePath()+"   "+methodList.get(0).getStartLine()+"   "+methodList.get(0).getEndLine());
        System.out.println(methodList.get(0).getSourceCode());
        System.out.println("Method list size : "+methodList.size());
        List<MethodPair> methodPairList = new ArrayList<MethodPair>();
        for(int i = 0 ; i < methodList.size();i++){
            for(int j=i+1 ; j<methodList.size();j++){
                MethodPair methodPair = new MethodPair(methodList.get(i),methodList.get(j));
                methodPairList.add(methodPair);
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
