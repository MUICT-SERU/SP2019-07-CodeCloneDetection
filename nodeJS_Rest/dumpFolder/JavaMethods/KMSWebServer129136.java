public void start() throws IOException {
    httpServer.start();
    DefaultMetricsSystem.initialize(processName);
    final JvmMetrics jm = JvmMetrics.initSingleton(processName, sessionId);
    jm.setPauseMonitor(pauseMonitor);
    pauseMonitor.start();
}