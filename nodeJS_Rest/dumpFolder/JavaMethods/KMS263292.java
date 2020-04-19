@POST
@Path(KMSRESTConstants.KEY_RESOURCE + "/{name:.*}/" + KMSRESTConstants.INVALIDATECACHE_RESOURCE)
public Response invalidateCache(@PathParam("name") final String name) throws Exception {
    try {
        LOG.trace("Entering invalidateCache Method.");
        KMSWebApp.getAdminCallsMeter().mark();
        checkNotEmpty(name, "name");
        UserGroupInformation user = HttpUserGroupInformation.get();
        assertAccess(KMSACLs.Type.ROLLOVER, user, KMSOp.INVALIDATE_CACHE, name);
        LOG.debug("Invalidating cache with key name {}.", name);
        user.doAs(new PrivilegedExceptionAction<Void>() {

            @Override
            public Void run() throws Exception {
                provider.invalidateCache(name);
                provider.flush();
                return null;
            }
        });
        kmsAudit.ok(user, KMSOp.INVALIDATE_CACHE, name, "");
        LOG.trace("Exiting invalidateCache for key name {}.", name);
        return Response.ok().build();
    } catch (Exception e) {
        LOG.debug("Exception in invalidateCache for key name {}.", name, e);
        throw e;
    }
}