@GET
@Path(KMSRESTConstants.KEYS_NAMES_RESOURCE)
@Produces(MediaType.APPLICATION_JSON + "; " + JettyUtils.UTF_8)
public Response getKeyNames() throws Exception {
    try {
        LOG.trace("Entering getKeyNames method.");
        KMSWebApp.getAdminCallsMeter().mark();
        UserGroupInformation user = HttpUserGroupInformation.get();
        assertAccess(KMSACLs.Type.GET_KEYS, user, KMSOp.GET_KEYS);
        List<String> json = user.doAs(new PrivilegedExceptionAction<List<String>>() {

            @Override
            public List<String> run() throws Exception {
                return provider.getKeys();
            }
        });
        kmsAudit.ok(user, KMSOp.GET_KEYS, "");
        LOG.trace("Exiting getKeyNames method.");
        return Response.ok().type(MediaType.APPLICATION_JSON).entity(json).build();
    } catch (Exception e) {
        LOG.debug("Exception in getkeyNames.", e);
        throw e;
    }
}