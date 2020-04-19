@Override
public KeyVersion rollNewVersion(String name) throws NoSuchAlgorithmException, IOException {
    KeyVersion keyVersion = super.rollNewVersion(name);
    getExtension().drain(name);
    return keyVersion;
}