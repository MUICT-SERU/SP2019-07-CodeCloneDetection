@Override
public KeyVersion decryptEncryptedKey(EncryptedKeyVersion encryptedKeyVersion) throws IOException, GeneralSecurityException {
    return keyProviderCryptoExtension.decryptEncryptedKey(encryptedKeyVersion);
}