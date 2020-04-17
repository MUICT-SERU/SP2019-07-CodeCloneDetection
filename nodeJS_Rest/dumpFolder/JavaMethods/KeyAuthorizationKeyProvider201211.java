@Override
public KeyVersion rollNewVersion(String name, byte[] material) throws IOException {
    writeLock.lock();
    try {
        doAccessCheck(name, KeyOpType.MANAGEMENT);
        return provider.rollNewVersion(name, material);
    } finally {
        writeLock.unlock();
    }
}