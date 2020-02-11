import org.antlr.v4.runtime.Token;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors


public class Method {
    private String id;
    private int startLine;
    private int endLine;
    private String filePath;
    private String fileName;
    private String sourceCode;
    private String methodName;
    private int lineOfCode;
    private ArrayList<Token> sourceCodeTokens;
    private int tokenNo;
    private int uniqueTokenNo;
    private int identifierNo;
    private int uniqueIdentifierNo;
    private int operatorNo;
    private int uniqueOperatorNo;
    private int tokenTypeDiversity;
    private String returnType;
    private double[] code2vecVector ;
    private boolean vectorSet;


    public Method(int start, int end, String path,String fileName, String code, String name,String type) throws Exception {
        this.startLine = start;
        this.endLine = end;
        this.filePath = path;
        this.fileName = fileName;
        this.sourceCode = code;
        this.methodName = name;
        this.lineOfCode = end - start;
        this.returnType = type;
        this.vectorSet = false;
        this.id = fileName+start+end;
        gatherMetrics();
//        setZeroCode2VecVector(384);
//        System.out.println("code2vec Vector : \n"+this.code2vecVector);
    }

    private void gatherMetrics() throws Exception {
        this.sourceCodeTokens = (new JavaTokenizer(this.sourceCode).tokenize());
        Set<String> tokenSet = new HashSet<>();
        Set<String> identifierSet = new HashSet<>();
        Set<String> operatorSet = new HashSet<>();
        Set<Integer> tokenTypeSet = new HashSet<>();
        this.tokenNo = 0;
        this.identifierNo = 0;
        this.operatorNo = 0;
         for (Token token : this.sourceCodeTokens) {
            String symbolicName = JavaLexer.VOCABULARY.getSymbolicName(token.getType());
            this.tokenNo++;
            tokenSet.add(token.getText());
            int tokenType = token.getType();
            if(tokenType < JavaLexer.ASSIGN && tokenType > JavaLexer.ARROW && tokenType < JavaLexer.DECIMAL_LITERAL && tokenType > JavaLexer.NULL_LITERAL && tokenType < JavaLexer.COMMENT && tokenType > JavaLexer.IDENTIFIER && tokenType < JavaLexer.PRIVATE && tokenType > JavaLexer.PUBLIC){
                tokenTypeSet.add(tokenType);
            }else if(tokenType >= JavaLexer.DECIMAL_LITERAL && tokenType <= JavaLexer.NULL_LITERAL){
                tokenTypeSet.add(JavaLexer.NULL_LITERAL);
            }else if(tokenType >= JavaLexer.PRIVATE && tokenType <= JavaLexer.PUBLIC){
                tokenTypeSet.add(JavaLexer.PUBLIC);
            }else if(tokenType >= JavaLexer.COMMENT && tokenType <= JavaLexer.LINE_COMMENT){
                tokenTypeSet.add(JavaLexer.COMMENT);
            }
            if (tokenType==JavaLexer.IDENTIFIER) {
                this.identifierNo++;
                identifierSet.add(token.getText());
                tokenTypeSet.add(JavaLexer.IDENTIFIER);
            }
            if (tokenType >= JavaLexer.ASSIGN && tokenType <= JavaLexer.ARROW){
                this.operatorNo++;
                operatorSet.add(token.getText());
                tokenTypeSet.add(JavaLexer.ASSIGN);
            }
        }
         this.tokenTypeDiversity = tokenTypeSet.size();
         this.uniqueTokenNo = tokenSet.size();
         this.uniqueIdentifierNo = identifierSet.size();
         this.uniqueOperatorNo = operatorSet.size();
    }

    public void writeFile(){
        try {
            FileWriter myWriter = new FileWriter("JavaMethods/"+id+".java");
            myWriter.write(this.sourceCode);
            myWriter.close();
//            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    private String executeCode2Vec() throws IOException {
        // set up the command and parameter
//        String pythonScriptPath = "code2vec.py";
//        String args = "--load models/java14_model/saved_model_iter8.release --predict --export_code_vectors";
        boolean readVector = false;
        String vector = "";
        String s = null;
        try {

            // run the Unix "ps -ef" command
            // using the Runtime exec method:/
            Process p = Runtime.getRuntime().exec("python3 SP2019-DoNotCopy/MerryEngine/code2vec/code2vec.py --load SP2019-DoNotCopy/MerryEngine/code2vec/models/java14_model/saved_model_iter8.release --predict --export_code_vectors");

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(p.getErrorStream()));

            // read the output from the command
            System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
                if(readVector == true){
                    vector += s.toString();
                }
                if(s.contains("Code vector")){
                    readVector = true;
                }
            }

            // read any errors from the attempted command
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }

//            System.exit(0);
        }
        catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
            System.exit(-1);
        }
        return vector;
    }

    public int getStartLine() {
        return startLine;
    }

    public int getEndLine() {
        return endLine;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getFileName() {
        return fileName;
    }

    public int getLineOfCode() {
        return lineOfCode;
    }

    public int getTokenNo() {
        return tokenNo;
    }

    public int getIdentifierNo() {
        return identifierNo;
    }

    public int getOperatorNo() {
        return operatorNo;
    }

    public int getUniqueTokenNo() {
        return uniqueTokenNo;
    }

    public int getUniqueIdentifierNo() {
        return uniqueIdentifierNo;
    }

    public int getUniqueOperatorNo() {
        return uniqueOperatorNo;
    }

    public int getTokenTypeDiversity() {
        return tokenTypeDiversity;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setCode2vecVector(String vector,int size) {
        code2vecVector = new double[size];
        String[] vec = vector.split(" ");
        for(int i=0 ;i<vec.length;i++){
            code2vecVector[i] = Double.parseDouble(vec[i]);
        }
        vectorSet = true;
    }
    private void setZeroCode2VecVector(int size){
        code2vecVector = new double[size];
        for(int i = 0 ; i < size; i++){
            code2vecVector[i] = 0.0000000;
        }
    }

    public double[] getCode2vecVector() {
        return code2vecVector;
    }

    public boolean isVectorSet() {
        return vectorSet;
    }

    public String getMetricsAsString(){
        String s = "Method Name : " + this.methodName +
                "\nLine of Code : " + this.lineOfCode +
                "\nReturn Type : " + this.returnType +
                "\nFile Name : " + this.fileName +
                "\nFile Path : " + this.filePath +
                "\nToken No : " + this.tokenNo +
                "\nIdentifier No : " + this.identifierNo +
                "\nOperation No : " + this.operatorNo +
                "\nUnique Token No : " + this.uniqueTokenNo +
                "\nUnique Identifier No : " + this.uniqueIdentifierNo +
                "\nUnique Operation No : " + this.uniqueOperatorNo +
                "\nToken type diversity : " + this.tokenTypeDiversity;
        return s;
    }
}