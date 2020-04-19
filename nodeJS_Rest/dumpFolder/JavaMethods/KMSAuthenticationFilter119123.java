@Override
public void sendError(int sc) throws IOException {
    statusCode = sc;
    super.sendError(sc);
}