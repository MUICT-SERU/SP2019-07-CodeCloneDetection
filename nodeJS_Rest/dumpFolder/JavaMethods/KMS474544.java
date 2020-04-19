@SuppressWarnings({ "rawtypes", "unchecked" })
@GET
@Path(KMSRESTConstants.KEY_RESOURCE + "/{name:.*}/" + KMSRESTConstants.EEK_SUB_RESOURCE)
@Produces(MediaType.APPLICATION_JSON + "; " + JettyUtils.UTF_8)
public Response generateEncryptedKeys(@PathParam("name") final String name, @QueryParam(KMSRESTConstants.EEK_OP) String edekOp, @DefaultValue("1") @QueryParam(KMSRESTConstants.EEK_NUM_KEYS) final int numKeys) throws Exception {
    try {
        LOG.trace("Entering generateEncryptedKeys method.");
        UserGroupInformation user = HttpUserGroupInformation.get();
        checkNotEmpty(name, "name");
        checkNotNull(edekOp, "eekOp");
        LOG.debug("Generating encrypted key with name {}," + " the edek Operation is {}.", name, edekOp);
        Object retJSON;
        if (edekOp.equals(KMSRESTConstants.EEK_GENERATE)) {
            LOG.debug("edek Operation is Generate.");
            assertAccess(KMSACLs.Type.GENERATE_EEK, user, KMSOp.GENERATE_EEK, name);
            final List<EncryptedKeyVersion> retEdeks = new LinkedList<EncryptedKeyVersion>();
            try {
                user.doAs(new PrivilegedExceptionAction<Void>() {

                    @Override
                    public Void run() throws Exception {
                        LOG.debug("Generated Encrypted key for {} number of " + "keys.", numKeys);
                        for (int i = 0; i < numKeys; i++) {
                            retEdeks.add(provider.generateEncryptedKey(name));
                        }
                        return null;
                    }
                });
            } catch (Exception e) {
                LOG.error("Exception in generateEncryptedKeys:", e);
                throw new IOException(e);
            }
            kmsAudit.ok(user, KMSOp.GENERATE_EEK, name, "");
            retJSON = new ArrayList();
            for (EncryptedKeyVersion edek : retEdeks) {
                ((ArrayList) retJSON).add(KMSUtil.toJSON(edek));
            }
        } else {
            StringBuilder error;
            error = new StringBuilder("IllegalArgumentException Wrong ");
            error.append(KMSRESTConstants.EEK_OP);
            error.append(" value, it must be ");
            error.append(KMSRESTConstants.EEK_GENERATE);
            error.append(" or ");
            error.append(KMSRESTConstants.EEK_DECRYPT);
            LOG.error(error.toString());
            throw new IllegalArgumentException(error.toString());
        }
        KMSWebApp.getGenerateEEKCallsMeter().mark();
        LOG.trace("Exiting generateEncryptedKeys method.");
        return Response.ok().type(MediaType.APPLICATION_JSON).entity(retJSON).build();
    } catch (Exception e) {
        LOG.debug("Exception in generateEncryptedKeys.", e);
        throw e;
    }
}