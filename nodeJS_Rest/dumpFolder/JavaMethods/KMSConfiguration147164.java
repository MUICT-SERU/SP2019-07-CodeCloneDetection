public static boolean isACLsFileNewer(long time) {
    boolean newer = false;
    String confDir = System.getProperty(KMS_CONFIG_DIR);
    if (confDir != null) {
        Path confPath = new Path(confDir);
        if (!confPath.isUriPathAbsolute()) {
            throw new RuntimeException("System property '" + KMS_CONFIG_DIR + "' must be an absolute path: " + confDir);
        }
        File f = new File(confDir, KMS_ACLS_XML);
        LOG.trace("Checking file {}, modification time is {}, last reload time is" + " {}", f.getPath(), f.lastModified(), time);
        newer = f.lastModified() - time > 100;
    }
    return newer;
}