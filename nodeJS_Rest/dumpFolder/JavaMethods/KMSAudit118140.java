private Set<Class<? extends KMSAuditLogger>> getAuditLoggerClasses(final Configuration conf) {
    Set<Class<? extends KMSAuditLogger>> result = new HashSet<>();
    Collection<String> classes = conf.getTrimmedStringCollection(KMSConfiguration.KMS_AUDIT_LOGGER_KEY);
    if (classes.isEmpty()) {
        LOG.info("No audit logger configured, using default.");
        result.add(SimpleKMSAuditLogger.class);
        return result;
    }
    for (String c : classes) {
        try {
            Class<?> cls = conf.getClassByName(c);
            result.add(cls.asSubclass(KMSAuditLogger.class));
        } catch (ClassNotFoundException cnfe) {
            throw new RuntimeException("Failed to load " + c + ", please check " + "configuration " + KMSConfiguration.KMS_AUDIT_LOGGER_KEY, cnfe);
        }
    }
    return result;
}