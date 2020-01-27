import info.debatty.java.stringsimilarity.*;
import java.util.Arrays;
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
    private double[] code2VecSimilarityScore = new double[12];
    private boolean decision = Boolean.parseBoolean(null);

    public MethodPair(Method m1,Method m2){
        this.method1 = m1;
        this.method2 = m2;
        computeMetrics();
        computeCode2VecSimilarity();
    }

    public MethodPair(Method m1,Method m2, boolean dec){
        this.method1 = m1;
        this.method2 = m2;
        this.decision = dec;
        computeMetrics();
        computeCode2VecSimilarity();

    }

    private void computeMetrics(){
        NormalizedLevenshtein l = new NormalizedLevenshtein();
//        JaroWinkler jw = new JaroWinkler();
//        Jaccard j = new Jaccard();
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
//        this.similarFileNameScore = stringSimilarity(this.method1.getFileName(),this.method2.getFileName());
//        this.similarFilePathScore = stringSimilarity(this.method1.getFilePath(),this.method2.getFilePath());
//        this.similarMethodNameScore = stringSimilarity(this.method1.getMethodName(),this.method2.getMethodName());

        if(this.method1.getReturnType().equals(this.method2.getReturnType())){
            this.isSameReturnType = true;
        }else{
            this.isSameReturnType = false;
        }
    }

    private void computeCode2VecSimilarity(){
//        double[] vecSim= new double[12];
        double[] vec1 = method1.getCode2vecVector();
        double[] vec2 = method2.getCode2vecVector();
        int size = vec1.length/12;
        int index = 0;
        for(int i = 0 ; i<12 ; i++){
            int beg = i*size , end = beg+size;
            double[] v1 = Arrays.copyOfRange(vec1,beg,end);
            double[] v2 = Arrays.copyOfRange(vec2,beg,end);
            code2VecSimilarityScore[i] = cosineSimilarity(v1,v2);
        }
//        return vecSim;
    }

    private double cosineSimilarity(double vec1[],double vec2[])
    {
        double dop=0;
        for (int n=0;n<vec1.length;n++)
            dop +=vec1[n]*vec2[n] ;

        double mag1=0.0,mag2=0.0;
        for (int n=0;n<vec1.length;n++)
        {
            mag1 +=  Math.pow(vec1[n],2) ;
            mag2 +=  Math.pow(vec2[n],2) ;
        }

        mag1=Math.sqrt(mag1);
        mag2=Math.sqrt(mag2);

        double csim = dop / (mag1 * mag2);
        return csim;
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

    public void setDecision(boolean decision) {
        this.decision = decision;
    }

    public boolean isDecision() {
        return decision;
    }

    public double[] getCode2VecSimilarityScore() {
        return code2VecSimilarityScore;
    }
}

