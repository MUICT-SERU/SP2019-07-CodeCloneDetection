public class CommandLineArgument {
    private String inputSource;
    private boolean sizeFilter = true;
    private double sizeFilterThreshold = 0.8;
    private int code2vecSize = 384;
    private boolean training = false;
    private int treeDepth = 8;
    private String execID = "";
    private String DBUrl;
    private int DBPort;
    private String outputSource ="result/clonesResult.csv";
    private String c2vPath;
    private String workingDir;
    private boolean isSyntactic = true;
    private boolean isSemantic = true;
    private String model = "randomForest";
    private String DBName = "MerryDB";
    private int minLOC = 10;

    public CommandLineArgument(String[] args){
        for(int i=0;i< args.length;i++) {
            if(args[i].equalsIgnoreCase("-input")){
                this.inputSource = args[i+1];
            }
            if(args[i].equalsIgnoreCase("-size-filter")){
                if(args[i+1].equalsIgnoreCase("off")){
                    this.sizeFilter = false;
                }
            }
            if(args[i].equalsIgnoreCase("-size-threshold")){
                this.sizeFilterThreshold = Double.parseDouble(args[i+1]);
                if(this.sizeFilterThreshold > 1.00 || this.sizeFilterThreshold < 0.00){
                    this.sizeFilterThreshold = 0.8;
                    System.out.println("The size filter threshold is exceed limit \n this execution run with default threshold = 0.8");
                }
            }
            if(args[i].equalsIgnoreCase("-code2vec-size")){
                this.code2vecSize = Integer.parseInt(args[i+1]);
                if(this.code2vecSize < 1){
                    this.code2vecSize = 384;
                }
            }
            if(args[i].equalsIgnoreCase("-training")) {
                if (args[i + 1].equalsIgnoreCase("on")) {
                    this.training = true;
                }
            }
            if(args[i].equalsIgnoreCase("-tree_depth")){
                this.treeDepth = Integer.parseInt(args[i+1]);
                if(this.treeDepth < 2){
                    this.treeDepth = 8;
                }
            }
            if(args[i].equalsIgnoreCase("-execID" )){
                this.execID = args[i+1];
            }
            if(args[i].equalsIgnoreCase("-DBUrl")){
                this.DBUrl = args[i+1];
            }
            if(args[i].equalsIgnoreCase("-Dbport")){
                this.DBPort = Integer.parseInt(args[i+1]);
            }
            if(args[i].equalsIgnoreCase("-output")){
                this.outputSource = args[i+1];
            }
            if(args[i].equalsIgnoreCase("-c2vPath")){
                this.c2vPath = args[i+1];
            }
            if(args[i].equalsIgnoreCase("-workingDir")){
                this.workingDir = args[i+1];
            }
            if(args[i].equalsIgnoreCase("-Syntactic")){
                if (args[i + 1].equalsIgnoreCase("off")) {
                    this.isSyntactic = false;
                }
            }
            if(args[i].equalsIgnoreCase("-Semantic")){
                if (args[i + 1].equalsIgnoreCase("off")) {
                    this.isSemantic = false;
                }
            }
            if(this.isSemantic == false && this.isSyntactic == false){
                this.isSyntactic = true;
                this.isSemantic = true;
                System.out.println("Turn off both Syntactic metrics and Semantic metric \n this execution run with both metrics");
            }
            if(args[i].equalsIgnoreCase("-model")){
                this.model = args[i+1];
                if(this.model.equalsIgnoreCase("SMO")||this.model.equalsIgnoreCase("Dtree")||this.model.equalsIgnoreCase("decisionTree")||this.model.equalsIgnoreCase("randomForest")||this.model.equalsIgnoreCase("SVM")){

                }else{
                    this.model = "SMO";
                    System.out.println("The execution can be run with SMO, DecisionTree, or randomForest only \n this execution run with SMO");
                }
            }
            if(args[i].equalsIgnoreCase("-dbName")){
                this.DBName = args[i+1];
            }
            if(args[i].equalsIgnoreCase("-minLOC")){
                this.minLOC = Integer.parseInt(args[i+1]);
            }

        }
    }

    public String getInputSource() {
        return inputSource;
    }

    public boolean isSizeFilter() {
        return sizeFilter;
    }

    public double getSizeFilterThreshold(){
        return this.sizeFilterThreshold;
    }

    public int getCode2vecSize(){
        return this.code2vecSize;
    }

    public boolean isTraining() {
        return training;
    }

    public int getTreeDepth() {
        return treeDepth;
    }

    public String getExecID() {
        return execID;
    }

    public String getDBUrl() {
        return DBUrl;
    }

    public int getDBPort() {
        return DBPort;
    }

    public String getOutputSource() {
        return outputSource;
    }

    public String getC2vPath() {
        return c2vPath;
    }

    public String getWorkingDir() {
        return workingDir;
    }

    public boolean isSyntactic() {
        return isSyntactic;
    }

    public boolean isSemantic() {
        return isSemantic;
    }

    public String getModel() {
        return model;
    }

    public String booleanString(boolean boo){
        if(boo){
            return "on";
        }
        return "off";
    }

    public String getDBName() {
        return DBName;
    }

    public int getMinLOC() {
        return minLOC;
    }
}
