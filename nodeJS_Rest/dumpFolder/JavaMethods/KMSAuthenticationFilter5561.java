@Override
protected Properties getConfiguration(String configPrefix, FilterConfig filterConfig) {
    Configuration conf = KMSWebApp.getConfiguration();
    return getKMSConfiguration(conf);
}