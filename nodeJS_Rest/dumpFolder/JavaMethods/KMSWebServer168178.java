public static void main(String[] args) throws Exception {
    KMSConfiguration.initLogging();
    StringUtils.startupShutdownMessage(KMSWebServer.class, args, LOG);
    Configuration conf = new ConfigurationWithLogging(KMSConfiguration.getKMSConf());
    Configuration sslConf = new ConfigurationWithLogging(SSLFactory.readSSLConfiguration(conf, SSLFactory.Mode.SERVER));
    KMSWebServer kmsWebServer = new KMSWebServer(conf, sslConf);
    kmsWebServer.start();
    kmsWebServer.join();
}