@Override
public void run() {
    try {
        if (KMSConfiguration.isACLsFileNewer(lastReload)) {
            setKMSACLs(loadACLs());
            setKeyACLs(loadACLs());
        }
    } catch (Exception ex) {
        LOG.warn(String.format("Could not reload ACLs file: '%s'", ex.toString()), ex);
    }
}