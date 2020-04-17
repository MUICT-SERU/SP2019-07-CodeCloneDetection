public void ok(UserGroupInformation user, KMS.KMSOp op, String extraMsg) {
    op(OpStatus.OK, op, user, null, "Unknown", extraMsg);
}