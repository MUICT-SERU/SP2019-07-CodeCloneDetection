@Override
public Void run() throws Exception {
    provider.invalidateCache(name);
    provider.flush();
    return null;
}