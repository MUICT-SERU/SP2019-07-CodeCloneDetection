private void assertAccess(KMSACLs.Type aclType, UserGroupInformation ugi, KMSOp operation) throws AccessControlException {
    KMSWebApp.getACLs().assertAccess(aclType, ugi, operation, null);
}