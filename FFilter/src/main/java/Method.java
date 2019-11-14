public class Method {
    private int startLine;
    private int endLine;
    private String filePath;
    private String sourceCode;
    private String methodName;

    public Method(int start, int end, String path, String code,String name){
        this.startLine = start;
        this.endLine = end;
        this.filePath = path;
        this.sourceCode = code;
        this.methodName = name;
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
}