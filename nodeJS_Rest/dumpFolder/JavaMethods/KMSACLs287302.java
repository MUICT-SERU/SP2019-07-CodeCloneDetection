private boolean checkKeyAccess(String keyName, UserGroupInformation ugi, KeyOpType opType) {
    Map<KeyOpType, AccessControlList> keyAcl = keyAcls.get(keyName);
    if (keyAcl == null) {
        LOG.debug("Key: {} has no ACLs defined, using defaults.", keyName);
        keyAcl = defaultKeyAcls;
    }
    boolean access = checkKeyAccess(keyAcl, ugi, opType);
    if (LOG.isDebugEnabled()) {
        LOG.debug("User: [{}], OpType: {}, KeyName: {} Result: {}", ugi.getShortUserName(), opType.toString(), keyName, access);
    }
    return access;
}