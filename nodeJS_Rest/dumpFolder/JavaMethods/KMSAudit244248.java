public void error(UserGroupInformation user, String method, String url, String extraMsg) {
    op(OpStatus.ERROR, null, user, null, "Unknown", "Method:'" + method + "' Exception:'" + extraMsg + "'");
}