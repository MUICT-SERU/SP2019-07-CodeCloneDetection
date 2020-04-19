@Override
public KeyVersion run() throws Exception {
    return provider.getKeyVersion(versionName);
}