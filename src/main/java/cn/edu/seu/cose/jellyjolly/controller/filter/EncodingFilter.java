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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
public class EncodingFilter implements Filter {
    
    private static final String INFO_ENCODING = "encoding=";
    
    private static final String PARAM_ENCODING = "encoding";
    
    private static final String DEFAULT_ENCODING = "UTF-8";
    
    private String encoding;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String encodingParam = filterConfig.getInitParameter(PARAM_ENCODING);
        encoding = (encodingParam == null) ? DEFAULT_ENCODING : encodingParam;
        Logger.getLogger(CommentListBuilder.class.getName())
                .log(Level.INFO, getEncodingInfo());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding(encoding);
        response.setCharacterEncoding(encoding);
        chain.doFilter(request, response);
    }
    
    private String getEncodingInfo() {
        StringBuilder builder = new StringBuilder();
        builder.append(INFO_ENCODING).append(encoding);
        return builder.toString();
    }

    @Override
    public void destroy() {}
    
    
    
}
