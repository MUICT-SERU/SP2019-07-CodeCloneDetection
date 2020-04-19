public KeyAuthorizationKeyProvider(KeyProviderCryptoExtension keyProvider, KeyACLs acls) {
    super(keyProvider, null);
    this.provider = keyProvider;
    this.acls = acls;
    ReadWriteLock lock = new ReentrantReadWriteLock(true);
    readLock = lock.readLock();
    writeLock = lock.writeLock();
}