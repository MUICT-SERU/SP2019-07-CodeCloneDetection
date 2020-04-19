public static UserGroupInformation getUgi() {
    Data data = DATA_TL.get();
    return data != null ? data.ugi : null;
}