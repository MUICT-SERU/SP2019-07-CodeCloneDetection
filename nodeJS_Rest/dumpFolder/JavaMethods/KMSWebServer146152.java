public void stop() throws Exception {
    httpServer.stop();
    pauseMonitor.stop();
    JvmMetrics.shutdownSingleton();
    DefaultMetricsSystem.shutdown();
}