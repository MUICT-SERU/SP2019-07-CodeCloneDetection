public class CommandLineArgument {
    private String inputSource;

    public CommandLineArgument(String[] args){
        for(int i=0;i< args.length;i++) {
            if(args[i].equalsIgnoreCase("-input")){
                this.inputSource = args[i+1];
            }
        }
    }

    public String getInputSource() {
        return inputSource;
    }
}
