@Override
public void invalidateCache(String name) throws IOException {
    super.invalidateCache(name);
    getExtension().drain(name);
}