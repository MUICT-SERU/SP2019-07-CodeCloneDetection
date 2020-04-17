@GET
@Path(KMSRESTConstants.KEY_RESOURCE + "/{name:.*}/" + KMSRESTConstants.CURRENT_VERSION_SUB_RESOURCE)
@Produces(MediaType.APPLICATION_JSON + "; " + JettyUtils.UTF_8)
public Response getCurrentVersion(@PathParam("name") final String name) throws Exception {
    try {
        LOG.trace("Entering getCurrentVersion method.");
        UserGroupInformation user = HttpUserGroupInformation.get();
        checkNotEmpty(name, "name");
        KMSWebApp.getKeyCallsMeter().mark();
        assertAccess(KMSACLs.Type.GET, user, KMSOp.GET_CURRENT_KEY, name);
        LOG.debug("Getting key version for key with name {}.", name);
        KeyVersion keyVersion = user.doAs(new PrivilegedExceptionAction<KeyVersion>() {

            @Override
            public KeyVersion run() throws Exception {
                return provider.getCurrentKey(name);
            }
        });
        Object json = KMSUtil.toJSON(keyVersion);
        kmsAudit.ok(user, KMSOp.GET_CURRENT_KEY, name, "");
        LOG.trace("Exiting getCurrentVersion method.");
        return Response.ok().type(MediaType.APPLICATION_JSON).entity(json).build();
    } catch (Exception e) {
        LOG.debug("Exception in getCurrentVersion.", e);
        throw e;
    }
}