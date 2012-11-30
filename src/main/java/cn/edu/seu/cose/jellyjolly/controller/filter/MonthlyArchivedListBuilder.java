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

import cn.edu.seu.cose.jellyjolly.dao.BlogPostDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.DataAccessException;
import cn.edu.seu.cose.jellyjolly.dto.MonthArchive;
import java.io.IOException;
import java.util.List;
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
public class MonthlyArchivedListBuilder extends HttpFilter {

    public static final String ATTRI_ARCHIVE_LIST = "archivelist";
    private BlogPostDataAccess blogPostDataAccess;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext ctx = filterConfig.getServletContext();
        blogPostDataAccess = (BlogPostDataAccess) ctx.getAttribute(
                "cn.edu.seu.cose.jellyjolly.blogPostDataAccess");
    }

    @Override
    public void doGet(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            List<MonthArchive> archiveList = blogPostDataAccess
                    .getMonthlyArchiveList();
            request.setAttribute(ATTRI_ARCHIVE_LIST, archiveList);
        } catch (NumberFormatException ex) {
            response.sendError(400, ex.getMessage());
            return;
        } catch (DataAccessException ex) {
            Logger.getLogger(CommentListBuilder.class.getName())
                    .log(Level.SEVERE, ex.getMessage(), ex);
            response.sendError(500, ex.getMessage());
            return;
        }

        chain.doFilter(request, response);
    }
}
