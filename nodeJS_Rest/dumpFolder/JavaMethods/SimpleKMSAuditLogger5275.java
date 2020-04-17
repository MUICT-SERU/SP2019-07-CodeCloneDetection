@Override
public void logAuditEvent(final OpStatus status, final AuditEvent event) {
    if (!Strings.isNullOrEmpty(event.getUser()) && !Strings.isNullOrEmpty(event.getKeyName()) && (event.getOp() != null) && KMSAudit.AGGREGATE_OPS_WHITELIST.contains(event.getOp())) {
        switch(status) {
            case OK:
                auditLog.info("{}[op={}, key={}, user={}, accessCount={}, interval={}ms] {}", status, event.getOp(), event.getKeyName(), event.getUser(), event.getAccessCount().get(), (event.getEndTime() - event.getStartTime()), event.getExtraMsg());
                break;
            case UNAUTHORIZED:
                logAuditSimpleFormat(status, event);
                break;
            default:
                logAuditSimpleFormat(status, event);
                break;
        }
    } else {
        logAuditSimpleFormat(status, event);
    }
}