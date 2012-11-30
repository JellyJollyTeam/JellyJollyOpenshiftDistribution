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

import cn.edu.seu.cose.jellyjolly.dao.BlogPageDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.DataAccessException;
import cn.edu.seu.cose.jellyjolly.dto.BlogPage;
import cn.edu.seu.cose.jellyjolly.util.Utils;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
public class PageBuilder extends HttpFilter {

    private static final String INFO_INVALID_INPUT =
            "PageBuilder: invalid input";
    private static final String PARAM_PAGE_ID = "pageid";
    private static final String ATTR_PAGE = "blogpage";
    private BlogPageDataAccess blogPageDataAccess;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext ctx = filterConfig.getServletContext();
        blogPageDataAccess = (BlogPageDataAccess) ctx.getAttribute(
                "cn.edu.seu.cose.jellyjolly.blogPageDataAccess");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        String pageIdParam = request.getParameter(PARAM_PAGE_ID);
        // invalid user input
        if (pageIdParam == null || !Utils.isNumeric(pageIdParam)) {
            response.sendError(400, INFO_INVALID_INPUT);
            return;
        }
        try {
            int blogPageId = Integer.valueOf(pageIdParam);
            BlogPage page = blogPageDataAccess.getPage(blogPageId);
            request.setAttribute(ATTR_PAGE, page);
        } catch (DataAccessException ex) {
            Logger.getLogger(PageBuilder.class.getName())
                    .log(Level.SEVERE, ex.getMessage(), ex);
            response.sendError(500, ex.getMessage());
            return;
        }
        chain.doFilter(request, response);
    }
}
