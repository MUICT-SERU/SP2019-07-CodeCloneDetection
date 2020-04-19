private void authorizeCreateKey(String keyName, Options options, UserGroupInformation ugi) throws IOException {
    Preconditions.checkNotNull(ugi, "UserGroupInformation cannot be null");
    Map<String, String> attributes = options.getAttributes();
    String aclName = attributes.get(KEY_ACL_NAME);
    boolean success = false;
    if (Strings.isNullOrEmpty(aclName)) {
        if (acls.isACLPresent(keyName, KeyOpType.MANAGEMENT)) {
            options.setAttributes(ImmutableMap.<String, String>builder().putAll(attributes).put(KEY_ACL_NAME, keyName).build());
            success = acls.hasAccessToKey(keyName, ugi, KeyOpType.MANAGEMENT) || acls.hasAccessToKey(keyName, ugi, KeyOpType.ALL);
        } else {
            success = false;
        }
    } else {
        success = acls.isACLPresent(aclName, KeyOpType.MANAGEMENT) && (acls.hasAccessToKey(aclName, ugi, KeyOpType.MANAGEMENT) || acls.hasAccessToKey(aclName, ugi, KeyOpType.ALL));
    }
    if (!success)
        throw new AuthorizationException(String.format("User [%s] is not" + " authorized to create key !!", ugi.getShortUserName()));
}