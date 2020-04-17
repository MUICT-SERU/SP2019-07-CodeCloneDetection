static Configuration getConfiguration(boolean loadHadoopDefaults, String... resources) {
    Configuration conf = new Configuration(loadHadoopDefaults);
    String confDir = System.getProperty(KMS_CONFIG_DIR);
    if (confDir != null) {
        try {
            Path confPath = new Path(confDir);
            if (!confPath.isUriPathAbsolute()) {
                throw new RuntimeException("System property '" + KMS_CONFIG_DIR + "' must be an absolute path: " + confDir);
            }
            for (String resource : resources) {
                conf.addResource(new URL("file://" + new Path(confDir, resource).toUri()));
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    } else {
        for (String resource : resources) {
            conf.addResource(resource);
        }
    }
    return conf;
}