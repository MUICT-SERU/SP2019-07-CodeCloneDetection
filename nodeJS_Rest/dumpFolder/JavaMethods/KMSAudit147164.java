private void initializeAuditLoggers(Configuration conf) {
    Set<Class<? extends KMSAuditLogger>> classes = getAuditLoggerClasses(conf);
    Preconditions.checkState(!classes.isEmpty(), "Should have at least 1 audit logger.");
    for (Class<? extends KMSAuditLogger> c : classes) {
        final KMSAuditLogger logger = ReflectionUtils.newInstance(c, conf);
        auditLoggers.add(logger);
    }
    for (KMSAuditLogger logger : auditLoggers) {
        try {
            LOG.info("Initializing audit logger {}", logger.getClass());
            logger.initialize(conf);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to initialize " + logger.getClass().getName(), ex);
        }
    }
}