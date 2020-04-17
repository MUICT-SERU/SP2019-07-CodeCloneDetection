@Override
public List<KeyVersion> getKeyVersions(String name) throws IOException {
    readLock.lock();
    try {
        doAccessCheck(name, KeyOpType.READ);
        return provider.getKeyVersions(name);
    } finally {
        readLock.unlock();
    }
}