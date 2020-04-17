public void ok(UserGroupInformation user, KMS.KMSOp op, String key, String extraMsg) {
    op(OpStatus.OK, op, user, key, "Unknown", extraMsg);
}