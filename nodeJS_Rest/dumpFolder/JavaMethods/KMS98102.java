private static KeyProvider.KeyVersion removeKeyMaterial(KeyProvider.KeyVersion keyVersion) {
    return new KMSClientProvider.KMSKeyVersion(keyVersion.getName(), keyVersion.getVersionName(), null);
}