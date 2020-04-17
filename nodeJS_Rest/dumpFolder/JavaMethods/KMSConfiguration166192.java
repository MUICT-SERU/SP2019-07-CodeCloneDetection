public static void initLogging() {
    String confDir = System.getProperty(KMS_CONFIG_DIR);
    if (confDir == null) {
        throw new RuntimeException("System property '" + KMSConfiguration.KMS_CONFIG_DIR + "' not defined");
    }
    if (System.getProperty("log4j.configuration") == null) {
        System.setProperty("log4j.defaultInitOverride", "true");
        boolean fromClasspath = true;
        File log4jConf = new File(confDir, LOG4J_PROPERTIES).getAbsoluteFile();
        if (log4jConf.exists()) {
            PropertyConfigurator.configureAndWatch(log4jConf.getPath(), 1000);
            fromClasspath = false;
        } else {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            URL log4jUrl = cl.getResource(LOG4J_PROPERTIES);
            if (log4jUrl != null) {
                PropertyConfigurator.configure(log4jUrl);
            }
        }
        LOG.debug("KMS log starting");
        if (fromClasspath) {
            LOG.warn("Log4j configuration file '{}' not found", LOG4J_PROPERTIES);
            LOG.warn("Logging with INFO level to standard output");
        }
    }
}