@GET
@Path(KMSRESTConstants.KEY_RESOURCE + "/{name:.*}/" + KMSRESTConstants.VERSIONS_SUB_RESOURCE)
@Produces(MediaType.APPLICATION_JSON + "; " + JettyUtils.UTF_8)
public Response getKeyVersions(@PathParam("name") final String name) throws Exception {
    try {
        LOG.trace("Entering getKeyVersions method.");
        UserGroupInformation user = HttpUserGroupInformation.get();
        checkNotEmpty(name, "name");
        KMSWebApp.getKeyCallsMeter().mark();
        assertAccess(KMSACLs.Type.GET, user, KMSOp.GET_KEY_VERSIONS, name);
        LOG.debug("Getting key versions for key {}", name);
        List<KeyVersion> ret = user.doAs(new PrivilegedExceptionAction<List<KeyVersion>>() {

            @Override
            public List<KeyVersion> run() throws Exception {
                return provider.getKeyVersions(name);
            }
        });
        Object json = KMSServerJSONUtils.toJSON(ret);
        kmsAudit.ok(user, KMSOp.GET_KEY_VERSIONS, name, "");
        LOG.trace("Exiting getKeyVersions method.");
        return Response.ok().type(MediaType.APPLICATION_JSON).entity(json).build();
    } catch (Exception e) {
        LOG.debug("Exception in getKeyVersions.", e);
        throw e;
    }
}