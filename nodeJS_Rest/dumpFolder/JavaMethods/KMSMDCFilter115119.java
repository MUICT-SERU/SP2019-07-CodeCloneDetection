@VisibleForTesting
public static void setContext(UserGroupInformation ugi, String method, String requestURL, String remoteAddr) {
    DATA_TL.set(new Data(ugi, method, requestURL, remoteAddr));
}