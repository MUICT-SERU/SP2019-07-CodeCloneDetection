@Override
public Metadata getMetadata(String name) throws IOException {
    readLock.lock();
    try {
        doAccessCheck(name, KeyOpType.READ);
        return provider.getMetadata(name);
    } finally {
        readLock.unlock();
    }
}