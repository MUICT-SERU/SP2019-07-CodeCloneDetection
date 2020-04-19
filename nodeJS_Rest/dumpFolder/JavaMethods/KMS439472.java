@GET
@Path(KMSRESTConstants.KEY_VERSION_RESOURCE + "/{versionName:.*}")
@Produces(MediaType.APPLICATION_JSON + "; " + JettyUtils.UTF_8)
public Response getKeyVersion(@PathParam("versionName") final String versionName) throws Exception {
    try {
        LOG.trace("Entering getKeyVersion method.");
        UserGroupInformation user = HttpUserGroupInformation.get();
        checkNotEmpty(versionName, "versionName");
        KMSWebApp.getKeyCallsMeter().mark();
        assertAccess(KMSACLs.Type.GET, user, KMSOp.GET_KEY_VERSION);
        LOG.debug("Getting key with version name {}.", versionName);
        KeyVersion keyVersion = user.doAs(new PrivilegedExceptionAction<KeyVersion>() {

            @Override
            public KeyVersion run() throws Exception {
                return provider.getKeyVersion(versionName);
            }
        });
        if (keyVersion != null) {
            kmsAudit.ok(user, KMSOp.GET_KEY_VERSION, keyVersion.getName(), "");
        }
        Object json = KMSUtil.toJSON(keyVersion);
        LOG.trace("Exiting getKeyVersion method.");
        return Response.ok().type(MediaType.APPLICATION_JSON).entity(json).build();
    } catch (Exception e) {
        LOG.debug("Exception in getKeyVersion.", e);
        throw e;
    }
}