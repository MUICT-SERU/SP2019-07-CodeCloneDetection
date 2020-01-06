public class CommandLineArgument {
    private String inputSource;
    private boolean sizeFilter = true;

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
        }
    }

    public String getInputSource() {
        return inputSource;
    }

    public boolean isSizeFilter() {
        return sizeFilter;
    }
}
