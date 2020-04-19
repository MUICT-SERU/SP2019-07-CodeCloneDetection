@Override
public KeyVersion getCurrentKey(String name) throws IOException {
    readLock.lock();
    try {
        doAccessCheck(name, KeyOpType.READ);
        return provider.getCurrentKey(name);
    } finally {
        readLock.unlock();
    }
}