@Override
public void deleteKey(String name) throws IOException {
    writeLock.lock();
    try {
        doAccessCheck(name, KeyOpType.MANAGEMENT);
        provider.deleteKey(name);
    } finally {
        writeLock.unlock();
    }
}