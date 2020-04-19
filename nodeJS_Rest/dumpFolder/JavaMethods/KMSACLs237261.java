public boolean hasAccess(Type type, UserGroupInformation ugi) {
    boolean access = acls.get(type).isUserAllowed(ugi);
    if (LOG.isDebugEnabled()) {
        LOG.debug("Checking user [{}] for: {} {} ", ugi.getShortUserName(), type.toString(), acls.get(type).getAclString());
    }
    if (access) {
        AccessControlList blacklist = blacklistedAcls.get(type);
        access = (blacklist == null) || !blacklist.isUserInList(ugi);
        if (LOG.isDebugEnabled()) {
            if (blacklist == null) {
                LOG.debug("No blacklist for {}", type.toString());
            } else if (access) {
                LOG.debug("user is not in {}", blacklist.getAclString());
            } else {
                LOG.debug("user is in {}", blacklist.getAclString());
            }
        }
    }
    if (LOG.isDebugEnabled()) {
        LOG.debug("User: [{}], Type: {} Result: {}", ugi.getShortUserName(), type.toString(), access);
    }
    return access;
}