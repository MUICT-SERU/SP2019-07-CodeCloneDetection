public static String getMethod() {
    Data data = DATA_TL.get();
    return data != null ? data.method : null;
}