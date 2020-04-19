private void parseAclsWithPrefix(final Configuration conf, final String prefix, final KeyOpType keyOp, Map<KeyOpType, AccessControlList> results) {
    String confKey = prefix + keyOp;
    String aclStr = conf.get(confKey);
    if (aclStr != null) {
        if (keyOp == KeyOpType.ALL) {
            LOG.warn("Invalid KEY_OP '{}' for {}, ignoring", keyOp, prefix);
        } else {
            if (aclStr.equals("*")) {
                LOG.info("{} for KEY_OP '{}' is set to '*'", prefix, keyOp);
            }
            results.put(keyOp, new AccessControlList(aclStr));
        }
    }
}