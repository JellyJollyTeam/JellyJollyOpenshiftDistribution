/*
 * Copyright (C) 2012 Colleage of Software Engineering, Southeast University
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cn.edu.seu.cose.jellyjolly.rest.auth;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import sun.misc.BASE64Decoder;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
public abstract class AuthenticationFilter implements Filter {

    private static final String AUTH_BEAN_ATTRI = "authBean";

    private static final String AUTHORIZATION_HEADER = "authorization";

    private static final String BASIC_AUTH_REALM = "authentication required";

    private static final String CHAR_ENCODING = "utf-8";

    private static final String HTTP_METHOD_GET = "GET";

    private static final String HTTP_BASIC_AUTH = "Basic";
    
    private static final Logger logger = Logger.getLogger(
            AuthenticationFilter.class.getName());
    
    private static String getAuthenticationType(String authorization)
                    throws IOException {
            return authorization.split(" +")[0];
    }

    private static String getAuthenticationInfo(String authorization)
                    throws IOException {
            return new String(new BASE64Decoder().decodeBuffer(
                            authorization.split(" +")[1]));
    }

    private static String[] getUsernameAndPasswordArray(String authInfo) {
            return authInfo.split(":");
    }

    private static void sendNotAuthorizedError(
                    HttpServletResponse httpResponse) throws IOException {
            httpResponse.setHeader("WWW-authenticate",
                            "Basic realm=" + "\"" + BASIC_AUTH_REALM + "\"");
            httpResponse.sendError(401, "Unauthorized");
    }

    protected abstract boolean authenticate(String username, String password)
                    throws Exception;
    
    protected abstract void doAfterAuthorized(HttpServletRequest request,
            HttpServletResponse response, AuthorizationBean authorizationBean)
            throws IOException, ServletException;
    
    protected abstract boolean expired(AuthorizationBean authBean) throws Exception;

    @Override
    public void init(FilterConfig fConfig) throws ServletException {}

    @Override
    public void destroy() {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                    FilterChain chain)
                                    throws IOException, ServletException {
            request.setCharacterEncoding(CHAR_ENCODING);
            response.setCharacterEncoding(CHAR_ENCODING);

            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;

            String httpMethod = httpRequest.getMethod();
            if (HTTP_METHOD_GET.equals(httpMethod)) {
                chain.doFilter(request, response);
                return;
            }

            doAuthentication(httpRequest, httpResponse, chain);
    }

    private void doAuthentication(HttpServletRequest request,
                    HttpServletResponse response, FilterChain chain)
                                    throws IOException, ServletException {
            HttpSession session = request.getSession();

            Object authObj = session.getAttribute(AUTH_BEAN_ATTRI);
            AuthorizationBean authBean = (AuthorizationBean) authObj;

            // if authorized
            boolean expired = true;
            try {
                expired = expired(authBean);
            } catch (Exception ex) {
                logger.log(Level.SEVERE, ex.getMessage(), ex);
            }
            if (authObj != null && !expired) {
                session.setAttribute(AUTH_BEAN_ATTRI, null);
                chain.doFilter(request, response);
                return;
            }

            // if not authorized
            String authorization = request.getHeader(AUTHORIZATION_HEADER);
            // if no authentication header
            if (authorization == null || "".equals(authorization)) {
                sendNotAuthorizedError(response);
                return;
            }

            String authType = getAuthenticationType(authorization);
            // if not Basic Auth
            if (!HTTP_BASIC_AUTH.equalsIgnoreCase(authType)) {
                sendNotAuthorizedError(response);
                return;
            }

            String authenticationInfo = getAuthenticationInfo(authorization);
            String[] pair = getUsernameAndPasswordArray(authenticationInfo);
            // if authentication pair not complete
            if (pair.length < 2) {
                sendNotAuthorizedError(response);
                return;
            }

            String username = pair[0];
            String password = pair[1];
            try {
                // if invalid pair
                if (!authenticate(username, password)) {
                        sendNotAuthorizedError(response);
                        return;
                }
            } catch (Exception ex) {
                    // if datebase connection fails
                logger.log(Level.SEVERE, ex.getMessage(), ex);
                response.sendError(500, ex.getMessage());
                return;
            }

            // it works
            long current = System.currentTimeMillis();
            AuthorizationBean newAuthBean = new AuthorizationBean(username, current);
            session.setAttribute(AUTH_BEAN_ATTRI, newAuthBean);
            
            doAfterAuthorized(request, response, newAuthBean);
            chain.doFilter(request, response);
    }

}
