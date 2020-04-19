@SuppressWarnings("unchecked")
public static List toJSON(List<KeyProvider.KeyVersion> keyVersions) {
    List json = new ArrayList();
    if (keyVersions != null) {
        for (KeyProvider.KeyVersion version : keyVersions) {
            json.add(KMSUtil.toJSON(version));
        }
    }
    return json;
}