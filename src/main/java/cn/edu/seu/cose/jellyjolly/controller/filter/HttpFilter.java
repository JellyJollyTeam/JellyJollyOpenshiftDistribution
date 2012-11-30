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

package cn.edu.seu.cose.jellyjolly.controller.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
public abstract class HttpFilter implements Filter {
    
    private static final String METHOD_DELETE = "DELETE";
    
    private static final String METHOD_HEAD = "HEAD";
    
    private static final String METHOD_GET = "GET";
    
    private static final String METHOD_OPTIONS = "OPTIONS";
    
    private static final String METHOD_POST = "POST";
    
    private static final String METHOD_PUT = "PUT";
    
    private static final String METHOD_TRACE = "TRACE";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        String protocol = request.getProtocol();
        if (!isHttpProtocol(protocol)
                || !(request instanceof HttpServletRequest)
                || !(response instanceof HttpServletResponse)) {
            chain.doFilter(request, response);
        }
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        doHttpFilter(httpRequest, httpResponse, chain);
    }
    
    private boolean isHttpProtocol(String protocol) {
        return "HTTP/1.0".equals(protocol) || "HTTP/1.1".equals(protocol);
    }
    
    public void doHttpFilter(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        final String method = request.getMethod();
        
        if (METHOD_DELETE.equals(method)) {
            doDelete(request, response, chain);
            return;
        }
        
        if (METHOD_HEAD.equals(method)) {
            doHead(request, response, chain);
            return;
        }
        
        if (METHOD_GET.equals(method)) {
            doGet(request, response, chain);
            return;
        }
        
        if (METHOD_OPTIONS.equals(method)) {
            doOptions(request, response, chain);
            return;
        }
        
        if (METHOD_POST.equals(method)) {
            doPost(request, response, chain);
            return;
        }
        
        if (METHOD_PUT.equals(method)) {
            doPut(request, response, chain);
            return;
        }
        
        if (METHOD_TRACE.equals(method)) {
            doTrace(request, response, chain);
        }
    }
    
    public void doGet(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        chain.doFilter(request, response);
    }
    
    public void doPost(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        chain.doFilter(request, response);
    }
    
    public void doPut(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        chain.doFilter(request, response);
    }
    
    public void doDelete(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        chain.doFilter(request, response);
    }
    
    public void doHead(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        chain.doFilter(request, response);
    }
    
    public void doOptions(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        chain.doFilter(request, response);
    }
    
    public void doTrace(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}

}
