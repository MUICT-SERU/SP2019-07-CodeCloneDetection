@Override
public void warmUpEncryptedKeys(String... keyNames) throws IOException {
    try {
        encKeyVersionQueue.initializeQueuesForKeys(keyNames);
    } catch (ExecutionException e) {
        throw new IOException(e);
    }
}