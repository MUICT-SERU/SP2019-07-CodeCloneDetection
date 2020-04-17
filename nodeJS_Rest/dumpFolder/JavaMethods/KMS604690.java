@SuppressWarnings("rawtypes")
@POST
@Path(KMSRESTConstants.KEY_VERSION_RESOURCE + "/{versionName:.*}/" + KMSRESTConstants.EEK_SUB_RESOURCE)
@Produces(MediaType.APPLICATION_JSON + "; " + JettyUtils.UTF_8)
public Response handleEncryptedKeyOp(@PathParam("versionName") final String versionName, @QueryParam(KMSRESTConstants.EEK_OP) String eekOp, Map jsonPayload) throws Exception {
    try {
        LOG.trace("Entering decryptEncryptedKey method.");
        UserGroupInformation user = HttpUserGroupInformation.get();
        checkNotEmpty(versionName, "versionName");
        checkNotNull(eekOp, "eekOp");
        LOG.debug("Decrypting key for {}, the edek Operation is {}.", versionName, eekOp);
        final String keyName = (String) jsonPayload.get(KMSRESTConstants.NAME_FIELD);
        String ivStr = (String) jsonPayload.get(KMSRESTConstants.IV_FIELD);
        String encMaterialStr = (String) jsonPayload.get(KMSRESTConstants.MATERIAL_FIELD);
        checkNotNull(ivStr, KMSRESTConstants.IV_FIELD);
        final byte[] iv = Base64.decodeBase64(ivStr);
        checkNotNull(encMaterialStr, KMSRESTConstants.MATERIAL_FIELD);
        final byte[] encMaterial = Base64.decodeBase64(encMaterialStr);
        Object retJSON;
        if (eekOp.equals(KMSRESTConstants.EEK_DECRYPT)) {
            KMSWebApp.getDecryptEEKCallsMeter().mark();
            assertAccess(KMSACLs.Type.DECRYPT_EEK, user, KMSOp.DECRYPT_EEK, keyName);
            KeyProvider.KeyVersion retKeyVersion = user.doAs(new PrivilegedExceptionAction<KeyVersion>() {

                @Override
                public KeyVersion run() throws Exception {
                    return provider.decryptEncryptedKey(new KMSClientProvider.KMSEncryptedKeyVersion(keyName, versionName, iv, KeyProviderCryptoExtension.EEK, encMaterial));
                }
            });
            retJSON = KMSUtil.toJSON(retKeyVersion);
            kmsAudit.ok(user, KMSOp.DECRYPT_EEK, keyName, "");
        } else if (eekOp.equals(KMSRESTConstants.EEK_REENCRYPT)) {
            KMSWebApp.getReencryptEEKCallsMeter().mark();
            assertAccess(KMSACLs.Type.GENERATE_EEK, user, KMSOp.REENCRYPT_EEK, keyName);
            EncryptedKeyVersion retEncryptedKeyVersion = user.doAs(new PrivilegedExceptionAction<EncryptedKeyVersion>() {

                @Override
                public EncryptedKeyVersion run() throws Exception {
                    return provider.reencryptEncryptedKey(new KMSClientProvider.KMSEncryptedKeyVersion(keyName, versionName, iv, KeyProviderCryptoExtension.EEK, encMaterial));
                }
            });
            retJSON = KMSUtil.toJSON(retEncryptedKeyVersion);
            kmsAudit.ok(user, KMSOp.REENCRYPT_EEK, keyName, "");
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
        LOG.trace("Exiting handleEncryptedKeyOp method.");
        return Response.ok().type(MediaType.APPLICATION_JSON).entity(retJSON).build();
    } catch (Exception e) {
        LOG.debug("Exception in handleEncryptedKeyOp.", e);
        throw e;
    }
}