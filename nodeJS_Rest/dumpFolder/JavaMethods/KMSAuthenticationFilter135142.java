@Override
@Deprecated
@SuppressWarnings("deprecation")
public void setStatus(int sc, String sm) {
    statusCode = sc;
    msg = sm;
    super.setStatus(sc, sm);
}