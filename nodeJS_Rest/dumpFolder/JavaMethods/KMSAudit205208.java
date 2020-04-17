@Override
public AuditEvent call() throws Exception {
    return new AuditEvent(op, ugi, key, remoteHost, extraMsg);
}