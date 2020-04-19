@GET
@Path(KMSRESTConstants.KEY_RESOURCE + "/{name:.*}/" + KMSRESTConstants.METADATA_SUB_RESOURCE)
@Produces(MediaType.APPLICATION_JSON + "; " + JettyUtils.UTF_8)
public Response getMetadata(@PathParam("name") final String name) throws Exception {
    try {
        LOG.trace("Entering getMetadata method.");
        UserGroupInformation user = HttpUserGroupInformation.get();
        checkNotEmpty(name, "name");
        KMSWebApp.getAdminCallsMeter().mark();
        assertAccess(KMSACLs.Type.GET_METADATA, user, KMSOp.GET_METADATA, name);
        LOG.debug("Getting metadata for key with name {}.", name);
        KeyProvider.Metadata metadata = user.doAs(new PrivilegedExceptionAction<KeyProvider.Metadata>() {

            @Override
            public KeyProvider.Metadata run() throws Exception {
                return provider.getMetadata(name);
            }
        });
        Object json = KMSServerJSONUtils.toJSON(name, metadata);
        kmsAudit.ok(user, KMSOp.GET_METADATA, name, "");
        LOG.trace("Exiting getMetadata method.");
        return Response.ok().type(MediaType.APPLICATION_JSON).entity(json).build();
    } catch (Exception e) {
        LOG.debug("Exception in getMetadata.", e);
        throw e;
    }
}