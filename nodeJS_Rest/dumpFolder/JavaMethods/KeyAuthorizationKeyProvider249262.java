private void verifyKeyVersionBelongsToKey(EncryptedKeyVersion ekv) throws IOException {
    String kn = ekv.getEncryptionKeyName();
    String kvn = ekv.getEncryptionKeyVersionName();
    KeyVersion kv = provider.getKeyVersion(kvn);
    if (kv == null) {
        throw new IllegalArgumentException(String.format("'%s' not found", kvn));
    }
    if (!kv.getName().equals(kn)) {
        throw new IllegalArgumentException(String.format("KeyVersion '%s' does not belong to the key '%s'", kvn, kn));
    }
}