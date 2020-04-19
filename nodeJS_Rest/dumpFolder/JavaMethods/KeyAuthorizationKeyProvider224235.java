@Override
public void warmUpEncryptedKeys(String... names) throws IOException {
    readLock.lock();
    try {
        for (String name : names) {
            doAccessCheck(name, KeyOpType.GENERATE_EEK);
        }
        provider.warmUpEncryptedKeys(names);
    } finally {
        readLock.unlock();
    }
}