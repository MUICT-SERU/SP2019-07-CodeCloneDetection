public void unauthorized(UserGroupInformation user, KMS.KMSOp op, String key) {
    op(OpStatus.UNAUTHORIZED, op, user, key, "Unknown", "");
}