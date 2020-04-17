@Override
public boolean hasAccessToKey(String keyName, UserGroupInformation ugi, KeyOpType opType) {
    boolean access = checkKeyAccess(keyName, ugi, opType) || checkKeyAccess(whitelistKeyAcls, ugi, opType);
    if (!access) {
        KMSWebApp.getKMSAudit().unauthorized(ugi, opType, keyName);
    }
    return access;
}