protected Response createResponse(Response.Status status, Throwable ex) {
    return HttpExceptionUtils.createJerseyExceptionResponse(status, ex);
}