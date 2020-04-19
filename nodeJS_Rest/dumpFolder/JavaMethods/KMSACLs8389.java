KMSACLs(Configuration conf) {
    if (conf == null) {
        conf = loadACLs();
    }
    setKMSACLs(conf);
    setKeyACLs(conf);
}