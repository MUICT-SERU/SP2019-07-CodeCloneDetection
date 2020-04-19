@SuppressWarnings("unchecked")
public static List toJSON(String[] keyNames, KeyProvider.Metadata[] metas) {
    List json = new ArrayList();
    for (int i = 0; i < keyNames.length; i++) {
        json.add(toJSON(keyNames[i], metas[i]));
    }
    return json;
}