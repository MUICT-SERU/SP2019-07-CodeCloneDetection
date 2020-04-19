@Override
public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
    KMSResponse kmsResponse = new KMSResponse(response);
    super.doFilter(request, kmsResponse, filterChain);
    if (kmsResponse.statusCode != HttpServletResponse.SC_OK && kmsResponse.statusCode != HttpServletResponse.SC_CREATED && kmsResponse.statusCode != HttpServletResponse.SC_UNAUTHORIZED) {
        KMSWebApp.getInvalidCallsMeter().mark();
    }
    if (kmsResponse.statusCode == HttpServletResponse.SC_UNAUTHORIZED) {
        KMSWebApp.getUnauthenticatedCallsMeter().mark();
        String method = ((HttpServletRequest) request).getMethod();
        StringBuffer requestURL = ((HttpServletRequest) request).getRequestURL();
        String queryString = ((HttpServletRequest) request).getQueryString();
        if (queryString != null) {
            requestURL.append("?").append(queryString);
        }
        if (!method.equals("OPTIONS")) {
            KMSWebApp.getKMSAudit().unauthenticated(request.getRemoteHost(), method, requestURL.toString(), kmsResponse.msg);
        }
    }
}