@Override
public void onRemoval(RemovalNotification<String, AuditEvent> entry) {
    AuditEvent event = entry.getValue();
    if (event.getAccessCount().get() > 0) {
        KMSAudit.this.logEvent(OpStatus.OK, event);
        event.getAccessCount().set(0);
        KMSAudit.this.cache.put(entry.getKey(), event);
    }
}