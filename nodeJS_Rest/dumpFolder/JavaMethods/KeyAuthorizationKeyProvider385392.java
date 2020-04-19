private void doAccessCheck(String keyName, KeyOpType opType) throws IOException {
    Metadata metadata = provider.getMetadata(keyName);
    if (metadata != null) {
        String aclName = metadata.getAttributes().get(KEY_ACL_NAME);
        checkAccess((aclName == null) ? keyName : aclName, getUser(), opType);
    }
}