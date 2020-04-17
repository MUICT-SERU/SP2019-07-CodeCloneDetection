@POST
@Path(KMSRESTConstants.KEYS_RESOURCE)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON + "; " + JettyUtils.UTF_8)
@SuppressWarnings("unchecked")
public Response createKey(Map jsonKey) throws Exception {
    try {
        LOG.trace("Entering createKey Method.");
        KMSWebApp.getAdminCallsMeter().mark();
        UserGroupInformation user = HttpUserGroupInformation.get();
        final String name = (String) jsonKey.get(KMSRESTConstants.NAME_FIELD);
        checkNotEmpty(name, KMSRESTConstants.NAME_FIELD);
        assertAccess(KMSACLs.Type.CREATE, user, KMSOp.CREATE_KEY, name);
        String cipher = (String) jsonKey.get(KMSRESTConstants.CIPHER_FIELD);
        final String material;
        material = (String) jsonKey.get(KMSRESTConstants.MATERIAL_FIELD);
        int length = (jsonKey.containsKey(KMSRESTConstants.LENGTH_FIELD)) ? (Integer) jsonKey.get(KMSRESTConstants.LENGTH_FIELD) : 0;
        String description = (String) jsonKey.get(KMSRESTConstants.DESCRIPTION_FIELD);
        LOG.debug("Creating key with name {}, cipher being used{}, " + "length of key {}, description of key {}", name, cipher, length, description);
        Map<String, String> attributes = (Map<String, String>) jsonKey.get(KMSRESTConstants.ATTRIBUTES_FIELD);
        if (material != null) {
            assertAccess(KMSACLs.Type.SET_KEY_MATERIAL, user, KMSOp.CREATE_KEY, name);
        }
        final KeyProvider.Options options = new KeyProvider.Options(KMSWebApp.getConfiguration());
        if (cipher != null) {
            options.setCipher(cipher);
        }
        if (length != 0) {
            options.setBitLength(length);
        }
        options.setDescription(description);
        options.setAttributes(attributes);
        KeyProvider.KeyVersion keyVersion = user.doAs(new PrivilegedExceptionAction<KeyVersion>() {

            @Override
            public KeyVersion run() throws Exception {
                KeyProvider.KeyVersion keyVersion = (material != null) ? provider.createKey(name, Base64.decodeBase64(material), options) : provider.createKey(name, options);
                provider.flush();
                return keyVersion;
            }
        });
        kmsAudit.ok(user, KMSOp.CREATE_KEY, name, "UserProvidedMaterial:" + (material != null) + " Description:" + description);
        if (!KMSWebApp.getACLs().hasAccess(KMSACLs.Type.GET, user)) {
            keyVersion = removeKeyMaterial(keyVersion);
        }
        Map json = KMSUtil.toJSON(keyVersion);
        String requestURL = KMSMDCFilter.getURL();
        int idx = requestURL.lastIndexOf(KMSRESTConstants.KEYS_RESOURCE);
        requestURL = requestURL.substring(0, idx);
        LOG.trace("Exiting createKey Method.");
        return Response.created(getKeyURI(KMSRESTConstants.SERVICE_VERSION, name)).type(MediaType.APPLICATION_JSON).header("Location", getKeyURI(requestURL, name)).entity(json).build();
    } catch (Exception e) {
        LOG.debug("Exception in createKey.", e);
        throw e;
    }
}