@Override
public EncryptedKeyVersion generateEncryptedKey(String encryptionKeyName) throws IOException, GeneralSecurityException {
    try {
        return encKeyVersionQueue.getNext(encryptionKeyName);
    } catch (ExecutionException e) {
        throw new IOException(e);
    }
}