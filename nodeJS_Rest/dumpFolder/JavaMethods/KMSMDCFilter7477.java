public static String getRemoteClientAddress() {
    Data data = DATA_TL.get();
    return data != null ? data.remoteClientAddress : null;
}