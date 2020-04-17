@Override
public KeyVersion run() throws Exception {
    KeyProvider.KeyVersion keyVersion = (material != null) ? provider.createKey(name, Base64.decodeBase64(material), options) : provider.createKey(name, options);
    provider.flush();
    return keyVersion;
}