private void assertAccess(KMSACLs.Type aclType, UserGroupInformation ugi, KMSOp operation, String key) throws AccessControlException {
    KMSWebApp.getACLs().assertAccess(aclType, ugi, operation, key);
}