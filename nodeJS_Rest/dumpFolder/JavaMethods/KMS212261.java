@POST
@Path(KMSRESTConstants.KEY_RESOURCE + "/{name:.*}")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON + "; " + JettyUtils.UTF_8)
public Response rolloverKey(@PathParam("name") final String name, Map jsonMaterial) throws Exception {
    try {
        LOG.trace("Entering rolloverKey Method.");
        KMSWebApp.getAdminCallsMeter().mark();
        UserGroupInformation user = HttpUserGroupInformation.get();
        assertAccess(KMSACLs.Type.ROLLOVER, user, KMSOp.ROLL_NEW_VERSION, name);
        checkNotEmpty(name, "name");
        LOG.debug("Rolling key with name {}.", name);
        final String material = (String) jsonMaterial.get(KMSRESTConstants.MATERIAL_FIELD);
        if (material != null) {
            assertAccess(KMSACLs.Type.SET_KEY_MATERIAL, user, KMSOp.ROLL_NEW_VERSION, name);
        }
        KeyProvider.KeyVersion keyVersion = user.doAs(new PrivilegedExceptionAction<KeyVersion>() {

            @Override
            public KeyVersion run() throws Exception {
                KeyVersion keyVersion = (material != null) ? provider.rollNewVersion(name, Base64.decodeBase64(material)) : provider.rollNewVersion(name);
                provider.flush();
                return keyVersion;
            }
        });
        kmsAudit.ok(user, KMSOp.ROLL_NEW_VERSION, name, "UserProvidedMaterial:" + (material != null) + " NewVersion:" + keyVersion.getVersionName());
        if (!KMSWebApp.getACLs().hasAccess(KMSACLs.Type.GET, user)) {
            keyVersion = removeKeyMaterial(keyVersion);
        }
        Map json = KMSUtil.toJSON(keyVersion);
        LOG.trace("Exiting rolloverKey Method.");
        return Response.ok().type(MediaType.APPLICATION_JSON).entity(json).build();
    } catch (Exception e) {
        LOG.debug("Exception in rolloverKey.", e);
        throw e;
    }
}