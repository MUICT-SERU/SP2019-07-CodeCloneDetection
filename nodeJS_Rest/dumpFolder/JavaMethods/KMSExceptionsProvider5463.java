protected String getOneLineMessage(Throwable exception) {
    String message = exception.getMessage();
    if (message != null) {
        int i = message.indexOf(ENTER);
        if (i > -1) {
            message = message.substring(0, i);
        }
    }
    return message;
}