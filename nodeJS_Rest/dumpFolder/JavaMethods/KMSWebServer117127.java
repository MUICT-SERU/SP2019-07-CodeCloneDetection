private static void deprecateEnv(String varName, Configuration conf, String propName, String confFile) {
    String value = System.getenv(varName);
    if (value == null) {
        return;
    }
    LOG.warn("Environment variable {} is deprecated and overriding" + " property {}, please set the property in {} instead.", varName, propName, confFile);
    conf.set(propName, value, "environment variable " + varName);
}