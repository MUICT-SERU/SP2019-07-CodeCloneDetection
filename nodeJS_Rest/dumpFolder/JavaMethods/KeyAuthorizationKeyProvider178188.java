@Override
public KeyVersion rollNewVersion(String name) throws NoSuchAlgorithmException, IOException {
    writeLock.lock();
    try {
        doAccessCheck(name, KeyOpType.MANAGEMENT);
        return provider.rollNewVersion(name);
    } finally {
        writeLock.unlock();
    }
}