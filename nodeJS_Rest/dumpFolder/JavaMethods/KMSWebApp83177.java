@Override
public void contextInitialized(ServletContextEvent sce) {
    try {
        kmsConf = KMSConfiguration.getKMSConf();
        UserGroupInformation.setConfiguration(kmsConf);
        LOG.info("-------------------------------------------------------------");
        LOG.info("  Java runtime version : {}", System.getProperty("java.runtime.version"));
        LOG.info("  User: {}", System.getProperty("user.name"));
        LOG.info("  KMS Hadoop Version: " + VersionInfo.getVersion());
        LOG.info("-------------------------------------------------------------");
        kmsAcls = new KMSACLs();
        kmsAcls.startReloader();
        metricRegistry = new MetricRegistry();
        jmxReporter = JmxReporter.forRegistry(metricRegistry).build();
        jmxReporter.start();
        generateEEKCallsMeter = metricRegistry.register(GENERATE_EEK_METER, new Meter());
        decryptEEKCallsMeter = metricRegistry.register(DECRYPT_EEK_METER, new Meter());
        reencryptEEKCallsMeter = metricRegistry.register(REENCRYPT_EEK_METER, new Meter());
        reencryptEEKBatchCallsMeter = metricRegistry.register(REENCRYPT_EEK_BATCH_METER, new Meter());
        adminCallsMeter = metricRegistry.register(ADMIN_CALLS_METER, new Meter());
        keyCallsMeter = metricRegistry.register(KEY_CALLS_METER, new Meter());
        invalidCallsMeter = metricRegistry.register(INVALID_CALLS_METER, new Meter());
        unauthorizedCallsMeter = metricRegistry.register(UNAUTHORIZED_CALLS_METER, new Meter());
        unauthenticatedCallsMeter = metricRegistry.register(UNAUTHENTICATED_CALLS_METER, new Meter());
        kmsAudit = new KMSAudit(kmsConf);
        String providerString = kmsConf.get(KMSConfiguration.KEY_PROVIDER_URI);
        if (providerString == null) {
            throw new IllegalStateException("No KeyProvider has been defined");
        }
        KeyProvider keyProvider = KeyProviderFactory.get(new URI(providerString), kmsConf);
        Preconditions.checkNotNull(keyProvider, String.format("No" + " KeyProvider has been initialized, please" + " check whether %s '%s' is configured correctly in" + " kms-site.xml.", KMSConfiguration.KEY_PROVIDER_URI, providerString));
        if (kmsConf.getBoolean(KMSConfiguration.KEY_CACHE_ENABLE, KMSConfiguration.KEY_CACHE_ENABLE_DEFAULT)) {
            long keyTimeOutMillis = kmsConf.getLong(KMSConfiguration.KEY_CACHE_TIMEOUT_KEY, KMSConfiguration.KEY_CACHE_TIMEOUT_DEFAULT);
            long currKeyTimeOutMillis = kmsConf.getLong(KMSConfiguration.CURR_KEY_CACHE_TIMEOUT_KEY, KMSConfiguration.CURR_KEY_CACHE_TIMEOUT_DEFAULT);
            keyProvider = new CachingKeyProvider(keyProvider, keyTimeOutMillis, currKeyTimeOutMillis);
        }
        LOG.info("Initialized KeyProvider " + keyProvider);
        keyProviderCryptoExtension = KeyProviderCryptoExtension.createKeyProviderCryptoExtension(keyProvider);
        keyProviderCryptoExtension = new EagerKeyGeneratorKeyProviderCryptoExtension(kmsConf, keyProviderCryptoExtension);
        if (kmsConf.getBoolean(KMSConfiguration.KEY_AUTHORIZATION_ENABLE, KMSConfiguration.KEY_AUTHORIZATION_ENABLE_DEFAULT)) {
            keyProviderCryptoExtension = new KeyAuthorizationKeyProvider(keyProviderCryptoExtension, kmsAcls);
        }
        LOG.info("Initialized KeyProviderCryptoExtension " + keyProviderCryptoExtension);
        final int defaultBitlength = kmsConf.getInt(KeyProvider.DEFAULT_BITLENGTH_NAME, KeyProvider.DEFAULT_BITLENGTH);
        LOG.info("Default key bitlength is {}", defaultBitlength);
        LOG.info("KMS Started");
    } catch (Throwable ex) {
        System.out.println();
        System.out.println("ERROR: Hadoop KMS could not be started");
        System.out.println();
        System.out.println("REASON: " + ex.toString());
        System.out.println();
        System.out.println("Stacktrace:");
        System.out.println("---------------------------------------------------");
        ex.printStackTrace(System.out);
        System.out.println("---------------------------------------------------");
        System.out.println();
        System.exit(1);
    }
}