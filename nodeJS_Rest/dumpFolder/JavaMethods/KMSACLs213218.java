public synchronized void stopReloader() {
    if (executorService != null) {
        executorService.shutdownNow();
        executorService = null;
    }
}