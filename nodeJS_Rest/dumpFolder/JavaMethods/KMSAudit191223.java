private void op(final OpStatus opStatus, final Object op, final UserGroupInformation ugi, final String key, final String remoteHost, final String extraMsg) {
    final String user = ugi == null ? null : ugi.getShortUserName();
    if (!Strings.isNullOrEmpty(user) && !Strings.isNullOrEmpty(key) && (op != null) && AGGREGATE_OPS_WHITELIST.contains(op)) {
        String cacheKey = createCacheKey(user, key, op);
        if (opStatus == OpStatus.UNAUTHORIZED) {
            cache.invalidate(cacheKey);
            logEvent(opStatus, new AuditEvent(op, ugi, key, remoteHost, extraMsg));
        } else {
            try {
                AuditEvent event = cache.get(cacheKey, new Callable<AuditEvent>() {

                    @Override
                    public AuditEvent call() throws Exception {
                        return new AuditEvent(op, ugi, key, remoteHost, extraMsg);
                    }
                });
                if (event.getAccessCount().incrementAndGet() == 0) {
                    event.getAccessCount().incrementAndGet();
                    logEvent(opStatus, event);
                }
            } catch (ExecutionException ex) {
                throw new RuntimeException(ex);
            }
        }
    } else {
        logEvent(opStatus, new AuditEvent(op, ugi, key, remoteHost, extraMsg));
    }
}