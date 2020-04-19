private Data(UserGroupInformation ugi, String method, String url, String remoteClientAddress) {
    this.ugi = ugi;
    this.method = method;
    this.url = url;
    this.remoteClientAddress = remoteClientAddress;
}