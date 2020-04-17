@Override
public EncryptedKeyVersion reencryptEncryptedKey(EncryptedKeyVersion ekv) throws IOException, GeneralSecurityException {
    readLock.lock();
    try {
        verifyKeyVersionBelongsToKey(ekv);
        doAccessCheck(ekv.getEncryptionKeyName(), KeyOpType.GENERATE_EEK);
        return provider.reencryptEncryptedKey(ekv);
    } finally {
        readLock.unlock();
    }
}