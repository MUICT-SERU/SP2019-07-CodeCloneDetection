@Override
public KeyVersion getKeyVersion(String versionName) throws IOException {
    readLock.lock();
    try {
        KeyVersion keyVersion = provider.getKeyVersion(versionName);
        if (keyVersion != null) {
            doAccessCheck(keyVersion.getName(), KeyOpType.READ);
        }
        return keyVersion;
    } finally {
        readLock.unlock();
    }
}