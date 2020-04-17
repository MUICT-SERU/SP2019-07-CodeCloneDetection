@GET
@Path(KMSRESTConstants.KEYS_METADATA_RESOURCE)
@Produces(MediaType.APPLICATION_JSON + "; " + JettyUtils.UTF_8)
public Response getKeysMetadata(@QueryParam(KMSRESTConstants.KEY) List<String> keyNamesList) throws Exception {
    try {
        LOG.trace("Entering getKeysMetadata method.");
        KMSWebApp.getAdminCallsMeter().mark();
        UserGroupInformation user = HttpUserGroupInformation.get();
        final String[] keyNames = keyNamesList.toArray(new String[keyNamesList.size()]);
        assertAccess(KMSACLs.Type.GET_METADATA, user, KMSOp.GET_KEYS_METADATA);
        KeyProvider.Metadata[] keysMeta = user.doAs(new PrivilegedExceptionAction<KeyProvider.Metadata[]>() {

            @Override
            public KeyProvider.Metadata[] run() throws Exception {
                return provider.getKeysMetadata(keyNames);
            }
        });
        Object json = KMSServerJSONUtils.toJSON(keyNames, keysMeta);
        kmsAudit.ok(user, KMSOp.GET_KEYS_METADATA, "");
        LOG.trace("Exiting getKeysMetadata method.");
        return Response.ok().type(MediaType.APPLICATION_JSON).entity(json).build();
    } catch (Exception e) {
        LOG.debug("Exception in getKeysmetadata.", e);
        throw e;
    }
}