public class CommandLineArgument {
    private String inputSource;
    private boolean sizeFilter = true;
    private double sizeFilterThreshold = 0.8;
    private int code2vecSize = 384;
    private boolean training = false;
    private int treeDepth = 8;
    private String execID = "";

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
}
