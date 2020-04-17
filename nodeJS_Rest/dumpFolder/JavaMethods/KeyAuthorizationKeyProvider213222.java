@Override
public void invalidateCache(String name) throws IOException {
    writeLock.lock();
    try {
        doAccessCheck(name, KeyOpType.MANAGEMENT);
        provider.invalidateCache(name);
    } finally {
        writeLock.unlock();
    }
}