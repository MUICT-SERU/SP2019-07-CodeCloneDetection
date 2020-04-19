@Override
public Metadata[] getKeysMetadata(String... names) throws IOException {
    readLock.lock();
    try {
        for (String name : names) {
            doAccessCheck(name, KeyOpType.READ);
        }
        return provider.getKeysMetadata(names);
    } finally {
        readLock.unlock();
    }
}