private boolean checkKeyAccess(Map<KeyOpType, AccessControlList> keyAcl, UserGroupInformation ugi, KeyOpType opType) {
    AccessControlList acl = keyAcl.get(opType);
    if (acl == null) {
        LOG.debug("No ACL available for key, denying access for {}", opType);
        return false;
    } else {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Checking user [{}] for: {}: {}", ugi.getShortUserName(), opType.toString(), acl.getAclString());
        }
        return acl.isUserAllowed(ugi);
    }
}