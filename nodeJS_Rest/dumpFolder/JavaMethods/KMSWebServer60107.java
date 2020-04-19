KMSWebServer(Configuration conf, Configuration sslConf) throws Exception {
    deprecateEnv("KMS_TEMP", conf, HttpServer2.HTTP_TEMP_DIR_KEY, KMSConfiguration.KMS_SITE_XML);
    deprecateEnv("KMS_HTTP_PORT", conf, KMSConfiguration.HTTP_PORT_KEY, KMSConfiguration.KMS_SITE_XML);
    deprecateEnv("KMS_MAX_THREADS", conf, HttpServer2.HTTP_MAX_THREADS_KEY, KMSConfiguration.KMS_SITE_XML);
    deprecateEnv("KMS_MAX_HTTP_HEADER_SIZE", conf, HttpServer2.HTTP_MAX_REQUEST_HEADER_SIZE_KEY, KMSConfiguration.KMS_SITE_XML);
    deprecateEnv("KMS_MAX_HTTP_HEADER_SIZE", conf, HttpServer2.HTTP_MAX_RESPONSE_HEADER_SIZE_KEY, KMSConfiguration.KMS_SITE_XML);
    deprecateEnv("KMS_SSL_ENABLED", conf, KMSConfiguration.SSL_ENABLED_KEY, KMSConfiguration.KMS_SITE_XML);
    deprecateEnv("KMS_SSL_KEYSTORE_FILE", sslConf, SSLFactory.SSL_SERVER_KEYSTORE_LOCATION, SSLFactory.SSL_SERVER_CONF_DEFAULT);
    deprecateEnv("KMS_SSL_KEYSTORE_PASS", sslConf, SSLFactory.SSL_SERVER_KEYSTORE_PASSWORD, SSLFactory.SSL_SERVER_CONF_DEFAULT);
    boolean sslEnabled = conf.getBoolean(KMSConfiguration.SSL_ENABLED_KEY, KMSConfiguration.SSL_ENABLED_DEFAULT);
    scheme = sslEnabled ? HttpServer2.HTTPS_SCHEME : HttpServer2.HTTP_SCHEME;
    processName = conf.get(METRICS_PROCESS_NAME_KEY, METRICS_PROCESS_NAME_DEFAULT);
    sessionId = conf.get(METRICS_SESSION_ID_KEY);
    pauseMonitor = new JvmPauseMonitor();
    pauseMonitor.init(conf);
    String host = conf.get(KMSConfiguration.HTTP_HOST_KEY, KMSConfiguration.HTTP_HOST_DEFAULT);
    int port = conf.getInt(KMSConfiguration.HTTP_PORT_KEY, KMSConfiguration.HTTP_PORT_DEFAULT);
    URI endpoint = new URI(scheme, null, host, port, null, null, null);
    httpServer = new HttpServer2.Builder().setName(NAME).setConf(conf).setSSLConf(sslConf).authFilterConfigurationPrefix(KMSAuthenticationFilter.CONFIG_PREFIX).setACL(new AccessControlList(conf.get(KMSConfiguration.HTTP_ADMINS_KEY, " "))).addEndpoint(endpoint).build();
}