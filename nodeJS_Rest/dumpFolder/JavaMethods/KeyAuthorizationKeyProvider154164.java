@Override
public KeyVersion createKey(String name, Options options) throws NoSuchAlgorithmException, IOException {
    writeLock.lock();
    try {
        authorizeCreateKey(name, options, getUser());
        return provider.createKey(name, options);
    } finally {
        writeLock.unlock();
    }
}