public void unauthenticated(String remoteHost, String method, String url, String extraMsg) {
    op(OpStatus.UNAUTHENTICATED, null, null, null, remoteHost, "RemoteHost:" + remoteHost + " Method:" + method + " URL:" + url + " ErrorMsg:'" + extraMsg + "'");
}