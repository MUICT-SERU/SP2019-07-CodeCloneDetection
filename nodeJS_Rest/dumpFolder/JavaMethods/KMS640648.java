@Override
public KeyVersion run() throws Exception {
    return provider.decryptEncryptedKey(new KMSClientProvider.KMSEncryptedKeyVersion(keyName, versionName, iv, KeyProviderCryptoExtension.EEK, encMaterial));
}