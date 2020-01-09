import org.antlr.v4.runtime.Token;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors


public class Method {
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


    public Method(int start, int end, String path,String fileName, String code, String name,String type) throws Exception {
        this.startLine = start;
        this.endLine = end;
        this.filePath = path;
        this.fileName = fileName;
        this.sourceCode = code;
        this.methodName = name;
        this.lineOfCode = end - start;
        this.returnType = type;
        gatherMetrics();
        writeFile(this.sourceCode);
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
            if(tokenType < 70 && tokenType > 104 && tokenType < 51 && tokenType > 60 && tokenType < 109 && tokenType > 111 && tokenType < 32 && tokenType > 35){
                tokenTypeSet.add(tokenType);
            }else if(tokenType >= 51 && tokenType <= 60){
                tokenTypeSet.add(55);
            }else if(tokenType >= 32 && tokenType <= 35){
                tokenTypeSet.add(33);
            }else if(tokenType >= 109 && tokenType <= 110){
                tokenTypeSet.add(110);
            }
            if (tokenType==111) {
                this.identifierNo++;
                identifierSet.add(token.getText());
                tokenTypeSet.add(111);
            }
            if (tokenType >= 70 && tokenType <= 104){
                this.operatorNo++;
                operatorSet.add(token.getText());
                tokenTypeSet.add(77);
            }
        }
         this.tokenTypeDiversity = tokenTypeSet.size();
         this.uniqueTokenNo = tokenSet.size();
         this.uniqueIdentifierNo = identifierSet.size();
         this.uniqueOperatorNo = operatorSet.size();
    }

    private void writeFile(String code){
        try {
            FileWriter myWriter = new FileWriter("/Users/sidekoiii/Documents/code2vec/Input.java");
            myWriter.write(code);
            myWriter.close();
//            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

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