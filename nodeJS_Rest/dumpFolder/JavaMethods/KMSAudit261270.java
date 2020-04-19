public void shutdown() {
    executor.shutdownNow();
    for (KMSAuditLogger logger : auditLoggers) {
        try {
            logger.cleanup();
        } catch (Exception ex) {
            LOG.error("Failed to cleanup logger {}", logger.getClass(), ex);
        }
    }
}