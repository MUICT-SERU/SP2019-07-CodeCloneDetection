public void unauthorized(UserGroupInformation user, KeyOpType op, String key) {
    op(OpStatus.UNAUTHORIZED, op, user, key, "Unknown", "");
}