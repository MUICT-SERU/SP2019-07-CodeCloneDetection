public EagerKeyGeneratorKeyProviderCryptoExtension(Configuration conf, KeyProviderCryptoExtension keyProviderCryptoExtension) {
    super(keyProviderCryptoExtension, new CryptoExtension(conf, keyProviderCryptoExtension));
}