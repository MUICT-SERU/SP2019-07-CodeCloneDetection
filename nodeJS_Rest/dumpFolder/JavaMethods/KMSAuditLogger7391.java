AuditEvent(Object op, UserGroupInformation ugi, String keyName, String remoteHost, String msg) {
    this.keyName = keyName;
    if (ugi == null) {
        this.user = null;
        this.impersonator = null;
    } else {
        this.user = ugi.getShortUserName();
        if (ugi.getAuthenticationMethod() == UserGroupInformation.AuthenticationMethod.PROXY) {
            this.impersonator = ugi.getRealUser().getUserName();
        } else {
            this.impersonator = null;
        }
    }
    this.remoteHost = remoteHost;
    this.op = op;
    this.extraMsg = msg;
}