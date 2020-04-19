@DELETE
@Path(KMSRESTConstants.KEY_RESOURCE + "/{name:.*}")
public Response deleteKey(@PathParam("name") final String name) throws Exception {
    try {
        LOG.trace("Entering deleteKey method.");
        KMSWebApp.getAdminCallsMeter().mark();
        UserGroupInformation user = HttpUserGroupInformation.get();
        assertAccess(KMSACLs.Type.DELETE, user, KMSOp.DELETE_KEY, name);
        checkNotEmpty(name, "name");
        LOG.debug("Deleting key with name {}.", name);
        user.doAs(new PrivilegedExceptionAction<Void>() {

            @Override
            public Void run() throws Exception {
                provider.deleteKey(name);
                provider.flush();
                return null;
            }
        });
        kmsAudit.ok(user, KMSOp.DELETE_KEY, name, "");
        LOG.trace("Exiting deleteKey method.");
        return Response.ok().build();
    } catch (Exception e) {
        LOG.debug("Exception in deleteKey.", e);
        throw e;
    }
}