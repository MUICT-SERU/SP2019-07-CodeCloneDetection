@Override
public KeyVersion rollNewVersion(String name, byte[] material) throws IOException {
    KeyVersion keyVersion = super.rollNewVersion(name, material);
    getExtension().drain(name);
    return keyVersion;
}