private void logAuditSimpleFormat(final OpStatus status, final AuditEvent event) {
    final List<String> kvs = new LinkedList<>();
    if (event.getOp() != null) {
        kvs.add("op=" + event.getOp());
    }
    if (!Strings.isNullOrEmpty(event.getKeyName())) {
        kvs.add("key=" + event.getKeyName());
    }
    if (!Strings.isNullOrEmpty(event.getUser())) {
        kvs.add("user=" + event.getUser());
    }
    if (kvs.isEmpty()) {
        auditLog.info("{} {}", status, event.getExtraMsg());
    } else {
        final String join = Joiner.on(", ").join(kvs);
        auditLog.info("{}[{}] {}", status, join, event.getExtraMsg());
    }
}