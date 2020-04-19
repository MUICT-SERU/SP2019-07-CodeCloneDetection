protected Configuration getProxyuserConfiguration(FilterConfig filterConfig) {
    Map<String, String> proxyuserConf = KMSWebApp.getConfiguration().getValByRegex("hadoop\\.kms\\.proxyuser\\.");
    Configuration conf = new Configuration(false);
    for (Map.Entry<String, String> entry : proxyuserConf.entrySet()) {
        conf.set(entry.getKey().substring("hadoop.kms.".length()), entry.getValue());
    }
    return conf;
}