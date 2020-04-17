@Override
public KeyVersion decryptEncryptedKey(EncryptedKeyVersion encryptedKeyVersion) throws IOException, GeneralSecurityException {
    readLock.lock();
    try {
        verifyKeyVersionBelongsToKey(encryptedKeyVersion);
        doAccessCheck(encryptedKeyVersion.getEncryptionKeyName(), KeyOpType.DECRYPT_EEK);
        return provider.decryptEncryptedKey(encryptedKeyVersion);
    } finally {
        readLock.unlock();
    }
}