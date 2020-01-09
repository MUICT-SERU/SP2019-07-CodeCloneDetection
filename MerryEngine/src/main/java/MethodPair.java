import info.debatty.java.stringsimilarity.*;

import static java.lang.Math.*;

public class MethodPair {
    private Method method1;
    private Method method2;
    private int diffTokenNo;
    private int diffUniqueTokenNo;
    private int diffIdentifierNo;
    private int diffUniqueIdentifierNo;
    private int diffOperatorNo;
    private int diffUniqueOperatorNo;
    private int diffTokenTypeDiversity;
    private int diffLOC;
    private double similarFileNameScore;
    private double similarFilePathScore;
    private double similarMethodNameScore;
    private boolean isSameReturnType;
//    private double similarMethodNameScore2;

    public MethodPair(Method m1,Method m2){
        this.method1 = m1;
        this.method2 = m2;
        computeMetrics();
    }

    private void computeMetrics(){
        NormalizedLevenshtein l = new NormalizedLevenshtein();
//        JaroWinkler jw = new JaroWinkler();
//        Jaccard j = new Jaccard();
//        NGram ngram = new NGram(2);
        this.diffTokenNo = abs(this.method1.getTokenNo()-this.method2.getTokenNo());
        this.diffUniqueTokenNo = abs(this.method1.getUniqueTokenNo()-this.method2.getUniqueTokenNo());
        this.diffIdentifierNo = abs(this.method1.getIdentifierNo()-this.method2.getIdentifierNo());
        this.diffUniqueIdentifierNo = abs(this.method1.getUniqueIdentifierNo()-this.method2.getUniqueIdentifierNo());
        this.diffOperatorNo = abs(this.method1.getOperatorNo()-this.method2.getOperatorNo());
        this.diffUniqueOperatorNo = abs(this.method1.getUniqueOperatorNo()-this.method2.getUniqueOperatorNo());
        this.diffTokenTypeDiversity = abs(this.method1.getTokenTypeDiversity()-this.method2.getTokenTypeDiversity());
        this.diffLOC = abs(this.method1.getLineOfCode()-this.method2.getLineOfCode());
        this.similarFileNameScore = l.distance(this.method1.getFileName(),this.method2.getFileName());
//        this.similarFilePathScore = l.distance(this.method1.getFilePath(),this.method2.getFilePath());
        this.similarMethodNameScore = l.distance(this.method1.getMethodName(),this.method2.getMethodName());
//        this.similarMethodNameScore = jw.distance(this.method1.getMethodName(),this.method2.getMethodName());
//        this.similarFileNameScore = stringSimilarity(this.method1.getFileName(),this.method2.getFileName());
//        this.similarFilePathScore = stringSimilarity(this.method1.getFilePath(),this.method2.getFilePath());
//        this.similarMethodNameScore = stringSimilarity(this.method1.getMethodName(),this.method2.getMethodName());

        if(this.method1.getReturnType().equals(this.method2.getReturnType())){
            this.isSameReturnType = true;
        }else{
            this.isSameReturnType = false;
        }
    }
    private double stringSimilarity(String s1,String s2){
        int s1l = s1.length();
        int s2l = s2.length();
        int minLength = min(s1l,s2l);
        int maxLength = max(s1l,s2l);
        double diffCount = 0.0;
        for(int i = 0 ; i<minLength ; i++){
            if(s1.charAt(i)!=s2.charAt(i)){
                diffCount = diffCount + 1.0;
            }
        }
        diffCount += maxLength-minLength;
        double similarityScore = diffCount/maxLength;
        return similarityScore;
    }

    public Method getMethod1() {
        return method1;
    }

    public Method getMethod2() {
        return method2;
    }

    public int getDiffTokenNo() {
        return diffTokenNo;
    }

    public int getDiffUniqueTokenNo() {
        return diffUniqueTokenNo;
    }

    public int getDiffIdentifierNo() {
        return diffIdentifierNo;
    }

    public int getDiffUniqueIdentifierNo() {
        return diffUniqueIdentifierNo;
    }

    public int getDiffOperatorNo() {
        return diffOperatorNo;
    }

    public int getDiffUniqueOperatorNo() {
        return diffUniqueOperatorNo;
    }

    public int getDiffTokenTypeDiversity() {
        return diffTokenTypeDiversity;
    }

    public int getDiffLOC() {
        return diffLOC;
    }

    public double getSimilarFileNameScore() {
        return similarFileNameScore;
    }

    public double getSimilarFilePathScore() {
        return similarFilePathScore;
    }

    public double getSimilarMethodNameScore() {
        return similarMethodNameScore;
    }

    public boolean isSameReturnType() {
        return isSameReturnType;
    }
}

