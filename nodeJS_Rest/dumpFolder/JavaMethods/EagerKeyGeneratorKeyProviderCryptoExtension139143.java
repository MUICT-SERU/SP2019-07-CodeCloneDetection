@Override
public EncryptedKeyVersion reencryptEncryptedKey(EncryptedKeyVersion ekv) throws IOException, GeneralSecurityException {
    return keyProviderCryptoExtension.reencryptEncryptedKey(ekv);
}