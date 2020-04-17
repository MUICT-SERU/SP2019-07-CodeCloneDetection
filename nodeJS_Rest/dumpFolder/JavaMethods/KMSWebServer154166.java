public URL getKMSUrl() {
    InetSocketAddress addr = httpServer.getConnectorAddress(0);
    if (null == addr) {
        return null;
    }
    try {
        return new URL(scheme, addr.getHostName(), addr.getPort(), SERVLET_PATH);
    } catch (MalformedURLException ex) {
        throw new RuntimeException("It should never happen: " + ex.getMessage(), ex);
    }
}