@Override
public void contextDestroyed(ServletContextEvent sce) {
    try {
        keyProviderCryptoExtension.close();
    } catch (IOException ioe) {
        LOG.error("Error closing KeyProviderCryptoExtension", ioe);
    }
    kmsAudit.shutdown();
    kmsAcls.stopReloader();
    jmxReporter.stop();
    jmxReporter.close();
    metricRegistry = null;
    LOG.info("KMS Stopped");
}