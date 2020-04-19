@Override
public boolean isACLPresent(String keyName, KeyOpType opType) {
    return (keyAcls.containsKey(keyName) || defaultKeyAcls.containsKey(opType) || whitelistKeyAcls.containsKey(opType));
}