@VisibleForTesting
Properties getKMSConfiguration(Configuration conf) {
    Properties props = new Properties();
    Map<String, String> propsWithPrefixMap = conf.getPropsWithPrefix(CONFIG_PREFIX);
    for (Map.Entry<String, String> entry : propsWithPrefixMap.entrySet()) {
        props.setProperty(entry.getKey(), entry.getValue());
    }
    String authType = props.getProperty(AUTH_TYPE);
    if (authType.equals(PseudoAuthenticationHandler.TYPE)) {
        props.setProperty(AUTH_TYPE, PseudoDelegationTokenAuthenticationHandler.class.getName());
    } else if (authType.equals(KerberosAuthenticationHandler.TYPE)) {
        props.setProperty(AUTH_TYPE, KerberosDelegationTokenAuthenticationHandler.class.getName());
    }
    props.setProperty(DelegationTokenAuthenticationHandler.TOKEN_KIND, KMSDelegationToken.TOKEN_KIND_STR);
    return props;
}