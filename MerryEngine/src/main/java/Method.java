import org.antlr.v4.runtime.Token;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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


    public Method(int start, int end, String path,String fileName, String code, String name) throws Exception {
        this.startLine = start;
        this.endLine = end;
        this.filePath = path;
        this.fileName = fileName;
        this.sourceCode = code;
        this.methodName = name;
        this.lineOfCode = end - start;
        gatherMetrics();
    }

    private void gatherMetrics() throws Exception {
        this.sourceCodeTokens = (new JavaTokenizer(this.sourceCode).tokenize());
        Set<Token> tokenSet = new HashSet<Token>();
        Set<Token> identifierSet = new HashSet<Token>();
        Set<Token> operatorSet = new HashSet<>();
        this.tokenNo = 0;
        this.identifierNo = 0;
        this.operatorNo = 0;
        if(sourceCodeTokens.get(1).getType()==38){
            this.returnType = sourceCodeTokens.get(2).getText();
        }else if(sourceCodeTokens.get(1).getText().equalsIgnoreCase("Override")){
            this.returnType = sourceCodeTokens.get(3).getText();
        }else{
            this.returnType = sourceCodeTokens.get(1).getText();
        }
         for (Token token : this.sourceCodeTokens) {
            String symbolicName = JavaLexer.VOCABULARY.getSymbolicName(token.getType());
            this.tokenNo++;
            tokenSet.add(token);
            if (token.getType()==111) {
                this.identifierNo++;
                identifierSet.add(token);
            }
            if (token.getType() >= 70 && token.getType() <= 104){
                this.operatorNo++;
                operatorSet.add(token);
            }
        }
         this.uniqueTokenNo = tokenSet.size();
         this.uniqueIdentifierNo = identifierSet.size();
         this.uniqueOperatorNo = operatorSet.size();
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
}