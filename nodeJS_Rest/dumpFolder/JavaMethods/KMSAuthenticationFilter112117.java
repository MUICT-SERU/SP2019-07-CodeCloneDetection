@Override
public void sendError(int sc, String msg) throws IOException {
    statusCode = sc;
    this.msg = msg;
    super.sendError(sc, HtmlQuoting.quoteHtmlChars(msg));
}