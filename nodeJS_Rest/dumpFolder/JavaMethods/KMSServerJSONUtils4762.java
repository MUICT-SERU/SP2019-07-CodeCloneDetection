@SuppressWarnings("unchecked")
public static Map toJSON(String keyName, KeyProvider.Metadata meta) {
    Map json = new LinkedHashMap();
    if (meta != null) {
        json.put(KMSRESTConstants.NAME_FIELD, keyName);
        json.put(KMSRESTConstants.CIPHER_FIELD, meta.getCipher());
        json.put(KMSRESTConstants.LENGTH_FIELD, meta.getBitLength());
        json.put(KMSRESTConstants.DESCRIPTION_FIELD, meta.getDescription());
        json.put(KMSRESTConstants.ATTRIBUTES_FIELD, meta.getAttributes());
        json.put(KMSRESTConstants.CREATED_FIELD, meta.getCreated().getTime());
        json.put(KMSRESTConstants.VERSIONS_FIELD, (long) meta.getVersions());
    }
    return json;
}