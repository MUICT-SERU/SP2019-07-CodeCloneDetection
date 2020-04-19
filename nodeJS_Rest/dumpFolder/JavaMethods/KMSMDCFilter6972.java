public static String getURL() {
    Data data = DATA_TL.get();
    return data != null ? data.url : null;
}