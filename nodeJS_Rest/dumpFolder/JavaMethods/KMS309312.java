@Override
public KeyProvider.Metadata[] run() throws Exception {
    return provider.getKeysMetadata(keyNames);
}