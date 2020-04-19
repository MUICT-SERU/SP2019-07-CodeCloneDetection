@Override
public Response toResponse(Exception exception) {
    Response.Status status;
    boolean doAudit = true;
    Throwable throwable = exception;
    if (exception instanceof ContainerException) {
        throwable = exception.getCause();
    }
    if (throwable instanceof SecurityException) {
        status = Response.Status.FORBIDDEN;
    } else if (throwable instanceof AuthenticationException) {
        status = Response.Status.FORBIDDEN;
        doAudit = false;
    } else if (throwable instanceof AuthorizationException) {
        status = Response.Status.FORBIDDEN;
        doAudit = false;
    } else if (throwable instanceof AccessControlException) {
        status = Response.Status.FORBIDDEN;
    } else if (exception instanceof IOException) {
        status = Response.Status.INTERNAL_SERVER_ERROR;
        log(status, throwable);
    } else if (exception instanceof UnsupportedOperationException) {
        status = Response.Status.BAD_REQUEST;
    } else if (exception instanceof IllegalArgumentException) {
        status = Response.Status.BAD_REQUEST;
    } else {
        status = Response.Status.INTERNAL_SERVER_ERROR;
        log(status, throwable);
    }
    if (doAudit) {
        KMSWebApp.getKMSAudit().error(KMSMDCFilter.getUgi(), KMSMDCFilter.getMethod(), KMSMDCFilter.getURL(), getOneLineMessage(exception));
    }
    EXCEPTION_LOG.warn("User {} request {} {} caused exception.", KMSMDCFilter.getUgi(), KMSMDCFilter.getMethod(), KMSMDCFilter.getURL(), exception);
    return createResponse(status, throwable);
}