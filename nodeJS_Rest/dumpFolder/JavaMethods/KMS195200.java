@Override
public Void run() throws Exception {
    provider.deleteKey(name);
    provider.flush();
    return null;
}