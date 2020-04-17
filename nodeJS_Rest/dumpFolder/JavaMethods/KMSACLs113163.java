@VisibleForTesting
void setKeyACLs(Configuration conf) {
    Map<String, HashMap<KeyOpType, AccessControlList>> tempKeyAcls = new HashMap<String, HashMap<KeyOpType, AccessControlList>>();
    Map<String, String> allKeyACLS = conf.getValByRegex(KMSConfiguration.KEY_ACL_PREFIX_REGEX);
    for (Map.Entry<String, String> keyAcl : allKeyACLS.entrySet()) {
        String k = keyAcl.getKey();
        int keyNameStarts = KMSConfiguration.KEY_ACL_PREFIX.length();
        int keyNameEnds = k.lastIndexOf(".");
        if (keyNameStarts >= keyNameEnds) {
            LOG.warn("Invalid key name '{}'", k);
        } else {
            String aclStr = keyAcl.getValue();
            String keyName = k.substring(keyNameStarts, keyNameEnds);
            String keyOp = k.substring(keyNameEnds + 1);
            KeyOpType aclType = null;
            try {
                aclType = KeyOpType.valueOf(keyOp);
            } catch (IllegalArgumentException e) {
                LOG.warn("Invalid key Operation '{}'", keyOp);
            }
            if (aclType != null) {
                HashMap<KeyOpType, AccessControlList> aclMap = tempKeyAcls.get(keyName);
                if (aclMap == null) {
                    aclMap = new HashMap<KeyOpType, AccessControlList>();
                    tempKeyAcls.put(keyName, aclMap);
                }
                aclMap.put(aclType, new AccessControlList(aclStr));
                LOG.info("KEY_NAME '{}' KEY_OP '{}' ACL '{}'", keyName, aclType, aclStr);
            }
        }
    }
    keyAcls = tempKeyAcls;
    final Map<KeyOpType, AccessControlList> tempDefaults = new HashMap<>();
    final Map<KeyOpType, AccessControlList> tempWhitelists = new HashMap<>();
    for (KeyOpType keyOp : KeyOpType.values()) {
        parseAclsWithPrefix(conf, KMSConfiguration.DEFAULT_KEY_ACL_PREFIX, keyOp, tempDefaults);
        parseAclsWithPrefix(conf, KMSConfiguration.WHITELIST_KEY_ACL_PREFIX, keyOp, tempWhitelists);
    }
    defaultKeyAcls = tempDefaults;
    whitelistKeyAcls = tempWhitelists;
}