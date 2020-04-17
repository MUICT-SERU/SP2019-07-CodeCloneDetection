private void setKMSACLs(Configuration conf) {
    Map<Type, AccessControlList> tempAcls = new HashMap<Type, AccessControlList>();
    Map<Type, AccessControlList> tempBlacklist = new HashMap<Type, AccessControlList>();
    for (Type aclType : Type.values()) {
        String aclStr = conf.get(aclType.getAclConfigKey(), ACL_DEFAULT);
        tempAcls.put(aclType, new AccessControlList(aclStr));
        String blacklistStr = conf.get(aclType.getBlacklistConfigKey());
        if (blacklistStr != null) {
            tempBlacklist.put(aclType, new AccessControlList(blacklistStr));
            LOG.info("'{}' Blacklist '{}'", aclType, blacklistStr);
        }
        LOG.info("'{}' ACL '{}'", aclType, aclStr);
    }
    acls = tempAcls;
    blacklistedAcls = tempBlacklist;
}