@Override
public KeyVersion run() throws Exception {
    KeyVersion keyVersion = (material != null) ? provider.rollNewVersion(name, Base64.decodeBase64(material)) : provider.rollNewVersion(name);
    provider.flush();
    return keyVersion;
}