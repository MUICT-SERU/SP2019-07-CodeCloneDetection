@Override
public EncryptedKeyVersion generateEncryptedKey(String encryptionKeyName) throws IOException, GeneralSecurityException {
    readLock.lock();
    try {
        doAccessCheck(encryptionKeyName, KeyOpType.GENERATE_EEK);
        return provider.generateEncryptedKey(encryptionKeyName);
    } finally {
        readLock.unlock();
    }
}