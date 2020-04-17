@Override
public KeyVersion createKey(String name, byte[] material, Options options) throws IOException {
    writeLock.lock();
    try {
        authorizeCreateKey(name, options, getUser());
        return provider.createKey(name, material, options);
    } finally {
        writeLock.unlock();
    }
}