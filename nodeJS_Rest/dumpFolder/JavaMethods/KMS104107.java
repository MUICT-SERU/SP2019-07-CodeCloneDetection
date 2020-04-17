private static URI getKeyURI(String domain, String keyName) {
    return UriBuilder.fromPath("{a}/{b}/{c}").build(domain, KMSRESTConstants.KEY_RESOURCE, keyName);
}