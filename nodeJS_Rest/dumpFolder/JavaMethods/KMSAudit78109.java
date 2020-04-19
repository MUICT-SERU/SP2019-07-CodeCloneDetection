KMSAudit(Configuration conf) {
    long windowMs = conf.getLong(KMSConfiguration.KMS_AUDIT_AGGREGATION_WINDOW, KMSConfiguration.KMS_AUDIT_AGGREGATION_WINDOW_DEFAULT);
    cache = CacheBuilder.newBuilder().expireAfterWrite(windowMs, TimeUnit.MILLISECONDS).removalListener(new RemovalListener<String, AuditEvent>() {

        @Override
        public void onRemoval(RemovalNotification<String, AuditEvent> entry) {
            AuditEvent event = entry.getValue();
            if (event.getAccessCount().get() > 0) {
                KMSAudit.this.logEvent(OpStatus.OK, event);
                event.getAccessCount().set(0);
                KMSAudit.this.cache.put(entry.getKey(), event);
            }
        }
    }).build();
    executor = Executors.newScheduledThreadPool(1, new ThreadFactoryBuilder().setDaemon(true).setNameFormat(KMS_LOGGER_NAME + "_thread").build());
    executor.scheduleAtFixedRate(new Runnable() {

        @Override
        public void run() {
            cache.cleanUp();
        }
    }, windowMs / 10, windowMs / 10, TimeUnit.MILLISECONDS);
    initializeAuditLoggers(conf);
}