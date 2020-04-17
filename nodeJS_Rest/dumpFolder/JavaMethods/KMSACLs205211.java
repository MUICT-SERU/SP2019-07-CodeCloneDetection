public synchronized void startReloader() {
    if (executorService == null) {
        executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(this, RELOADER_SLEEP_MILLIS, RELOADER_SLEEP_MILLIS, TimeUnit.MILLISECONDS);
    }
}