private void logEvent(final OpStatus status, AuditEvent event) {
    event.setEndTime(Time.now());
    for (KMSAuditLogger logger : auditLoggers) {
        logger.logAuditEvent(status, event);
    }
}