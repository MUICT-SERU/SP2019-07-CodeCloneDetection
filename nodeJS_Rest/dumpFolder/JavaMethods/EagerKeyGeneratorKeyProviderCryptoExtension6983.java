@Override
public void fillQueueForKey(String keyName, Queue<EncryptedKeyVersion> keyQueue, int numKeys) throws IOException {
    List<EncryptedKeyVersion> retEdeks = new LinkedList<EncryptedKeyVersion>();
    for (int i = 0; i < numKeys; i++) {
        try {
            retEdeks.add(keyProviderCryptoExtension.generateEncryptedKey(keyName));
        } catch (GeneralSecurityException e) {
            throw new IOException(e);
        }
    }
    keyQueue.addAll(retEdeks);
}