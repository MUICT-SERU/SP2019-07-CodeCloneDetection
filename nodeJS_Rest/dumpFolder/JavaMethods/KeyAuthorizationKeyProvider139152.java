private void checkAccess(String aclName, UserGroupInformation ugi, KeyOpType opType) throws AuthorizationException {
    Preconditions.checkNotNull(aclName, "Key ACL name cannot be null");
    Preconditions.checkNotNull(ugi, "UserGroupInformation cannot be null");
    if (acls.isACLPresent(aclName, opType) && (acls.hasAccessToKey(aclName, ugi, opType) || acls.hasAccessToKey(aclName, ugi, KeyOpType.ALL))) {
        return;
    } else {
        throw new AuthorizationException(String.format("User [%s] is not" + " authorized to perform [%s] on key with ACL name [%s]!!", ugi.getShortUserName(), opType, aclName));
    }
}