@SuppressWarnings("rawtypes")
@POST
@Path(KMSRESTConstants.KEY_RESOURCE + "/{name:.*}/" + KMSRESTConstants.REENCRYPT_BATCH_SUB_RESOURCE)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON + "; " + JettyUtils.UTF_8)
public Response reencryptEncryptedKeys(@PathParam("name") final String name, final List<Map> jsonPayload) throws Exception {
    LOG.trace("Entering reencryptEncryptedKeys method.");
    try {
        final StopWatch sw = new StopWatch().start();
        checkNotEmpty(name, "name");
        checkNotNull(jsonPayload, "jsonPayload");
        final UserGroupInformation user = HttpUserGroupInformation.get();
        KMSWebApp.getReencryptEEKBatchCallsMeter().mark();
        if (jsonPayload.size() > MAX_NUM_PER_BATCH) {
            LOG.warn("Payload size {} too big for reencryptEncryptedKeys from" + " user {}.", jsonPayload.size(), user);
        }
        assertAccess(KMSACLs.Type.GENERATE_EEK, user, KMSOp.REENCRYPT_EEK_BATCH, name);
        LOG.debug("Batch reencrypting {} Encrypted Keys for key name {}", jsonPayload.size(), name);
        final List<EncryptedKeyVersion> ekvs = KMSUtil.parseJSONEncKeyVersions(name, jsonPayload);
        Preconditions.checkArgument(ekvs.size() == jsonPayload.size(), "EncryptedKey size mismatch after parsing from json");
        for (EncryptedKeyVersion ekv : ekvs) {
            Preconditions.checkArgument(name.equals(ekv.getEncryptionKeyName()), "All EncryptedKeys must be under the given key name " + name);
        }
        user.doAs(new PrivilegedExceptionAction<Void>() {

            @Override
            public Void run() throws Exception {
                provider.reencryptEncryptedKeys(ekvs);
                return null;
            }
        });
        List retJSON = new ArrayList<>(ekvs.size());
        for (EncryptedKeyVersion ekv : ekvs) {
            retJSON.add(KMSUtil.toJSON(ekv));
        }
        kmsAudit.ok(user, KMSOp.REENCRYPT_EEK_BATCH, name, "reencrypted " + ekvs.size() + " keys");
        LOG.info("reencryptEncryptedKeys {} keys for key {} took {}", jsonPayload.size(), name, sw.stop());
        LOG.trace("Exiting reencryptEncryptedKeys method.");
        return Response.ok().type(MediaType.APPLICATION_JSON).entity(retJSON).build();
    } catch (Exception e) {
        LOG.debug("Exception in reencryptEncryptedKeys.", e);
        throw e;
    }
}