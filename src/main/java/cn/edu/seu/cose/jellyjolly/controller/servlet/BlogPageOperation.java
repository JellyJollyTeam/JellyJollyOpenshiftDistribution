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
package cn.edu.seu.cose.jellyjolly.controller.servlet;

import cn.edu.seu.cose.jellyjolly.dao.BlogPageDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.DataAccessException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
@WebServlet(name = "BlogPgeOperation", urlPatterns = {"/admin/page"})
public class BlogPageOperation extends HttpServlet {

    private static final String PARAM_OPERATION = "op";
    private static final String PARAM_PAGE_ID = "pageid";
    private static final String PARAM_TITLE = "title";
    private static final String PARAM_CONTENT = "content";
    private static final String OP_POST = "post";
    private static final String OP_EDIT = "edit";
    private static final String OP_DELETE = "del";
    private static final String HOME_URL = "../home.jsp";
    private static final String BLOG_PAGE_URL = "../page.jsp?pageid=";
    private static final Logger logger = Logger.getLogger(
            BlogPageOperation.class.getName());
    private BlogPageDataAccess blogPageDataAccess;

    private static String getRedirectUrl(long postId) {
        return new StringBuilder().append(BLOG_PAGE_URL).append(postId)
                .toString();
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext ctx = config.getServletContext();
        blogPageDataAccess = (BlogPageDataAccess) ctx.getAttribute(
                "cn.edu.seu.cose.jellyjolly.blogPageDataAccess");
    }

    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        String operation = request.getParameter(PARAM_OPERATION);

        if (OP_POST.equalsIgnoreCase(operation)) {
            post(request, response);
            return;
        }
        if (OP_EDIT.equalsIgnoreCase(operation)) {
            edit(request, response);
            return;
        }
        if (OP_DELETE.equalsIgnoreCase(operation)) {
            delete(request, response);
            return;
        }

        response.sendError(400);
    }

    private void post(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String title = request.getParameter(PARAM_TITLE);
        String content = request.getParameter(PARAM_CONTENT);
        if (title == null || content == null) {
            response.sendError(400);
            return;
        }

        try {
            int pageId = blogPageDataAccess.addNewPage(title, content);
            response.sendRedirect(getRedirectUrl(pageId));
        } catch (DataAccessException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            response.sendError(500, ex.getMessage());
        }
    }

    private void edit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pageIdParam = request.getParameter(PARAM_PAGE_ID);
        String title = request.getParameter(PARAM_TITLE);
        String content = request.getParameter(PARAM_CONTENT);
        if (pageIdParam == null) {
            response.sendError(400);
        }

        try {
            int pageId = Integer.valueOf(pageIdParam);
            if (title != null) {
                blogPageDataAccess.changeTitle(pageId, title);
            }
            if (content != null) {
                blogPageDataAccess.changeContent(pageId, content);
            }
            response.sendRedirect(getRedirectUrl(pageId));
        } catch (NumberFormatException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            response.sendError(400, ex.getMessage());
        } catch (DataAccessException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            response.sendError(500, ex.getMessage());
        }
    }

    private void delete(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        String pageIdParam = request.getParameter(PARAM_PAGE_ID);
        if (pageIdParam == null) {
            response.sendError(400);
        }

        try {
            int pageId = Integer.valueOf(pageIdParam);
            blogPageDataAccess.deletePage(pageId);
            response.sendRedirect(HOME_URL);
        } catch (NumberFormatException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            response.sendError(400, ex.getMessage());
        } catch (DataAccessException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            response.sendError(500, ex.getMessage());
        }
    }
}
