@Override
public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    try {
        clearContext();
        UserGroupInformation ugi = HttpUserGroupInformation.get();
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String method = httpServletRequest.getMethod();
        StringBuffer requestURL = httpServletRequest.getRequestURL();
        String queryString = httpServletRequest.getQueryString();
        if (queryString != null) {
            requestURL.append("?").append(queryString);
        }
        setContext(ugi, method, requestURL.toString(), request.getRemoteAddr());
        chain.doFilter(request, response);
    } finally {
        clearContext();
    }
}