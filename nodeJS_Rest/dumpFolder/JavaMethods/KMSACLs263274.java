public void assertAccess(KMSACLs.Type aclType, UserGroupInformation ugi, KMSOp operation, String key) throws AccessControlException {
    if (!KMSWebApp.getACLs().hasAccess(aclType, ugi)) {
        KMSWebApp.getUnauthorizedCallsMeter().mark();
        KMSWebApp.getKMSAudit().unauthorized(ugi, operation, key);
        throw new AuthorizationException(String.format((key != null) ? UNAUTHORIZED_MSG_WITH_KEY : UNAUTHORIZED_MSG_WITHOUT_KEY, ugi.getShortUserName(), operation, key));
    }
}