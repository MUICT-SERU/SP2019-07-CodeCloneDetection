@Override
public void reencryptEncryptedKeys(List<EncryptedKeyVersion> ekvs) throws IOException, GeneralSecurityException {
    if (ekvs.isEmpty()) {
        return;
    }
    readLock.lock();
    try {
        for (EncryptedKeyVersion ekv : ekvs) {
            verifyKeyVersionBelongsToKey(ekv);
        }
        final String keyName = ekvs.get(0).getEncryptionKeyName();
        doAccessCheck(keyName, KeyOpType.GENERATE_EEK);
        provider.reencryptEncryptedKeys(ekvs);
    } finally {
        readLock.unlock();
    }
}