private static String createCacheKey(String user, String key, Object op) {
    return user + "#" + key + "#" + op;
}