protected void log(Response.Status status, Throwable ex) {
    UserGroupInformation ugi = KMSMDCFilter.getUgi();
    String method = KMSMDCFilter.getMethod();
    String url = KMSMDCFilter.getURL();
    String remoteClientAddress = KMSMDCFilter.getRemoteClientAddress();
    String msg = getOneLineMessage(ex);
    LOG.warn("User:'{}' Method:{} URL:{} From:{} Response:{}-{}", ugi, method, url, remoteClientAddress, status, msg, ex);
}