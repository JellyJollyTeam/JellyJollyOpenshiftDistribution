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

import cn.edu.seu.cose.jellyjolly.dao.BlogPostDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.CategoryDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.CommentDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.DataAccessException;
import cn.edu.seu.cose.jellyjolly.dto.AdminUser;
import cn.edu.seu.cose.jellyjolly.dto.Category;
import cn.edu.seu.cose.jellyjolly.model.session.UserAuthorization;
import cn.edu.seu.cose.jellyjolly.util.Utils;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
@WebServlet(name = "BlogPostOperation", urlPatterns = {"/admin/post"})
public class BlogPostOperation extends HttpServlet {

    private static final String PARAM_OPERATION = "op";
    private static final String PARAM_POST_ID = "postid";
    private static final String PARAM_TITLE = "title";
    private static final String PARAM_CONTENT = "content";
    private static final String PARAM_CATEGORY_ID = "categoryid";
    private static final String PARAM_CATEGORY_NAME = "categoryname";
    private static final String OP_POST = "post";
    private static final String OP_EDIT = "edit";
    private static final String OP_DELETE = "del";
    private static final String HOME_URL = "../home.jsp";
    private static final String BLOG_POST_URL = "../post.jsp?postid=";
    private static final Logger logger = Logger.getLogger(
            BlogPostOperation.class.getName());
    private BlogPostDataAccess blogPostDataAccess;
    private CategoryDataAccess categoryDataAccess;
    private CommentDataAccess commentDataAccess;

    private static String getRedirectUrl(long postId) {
        return new StringBuilder().append(BLOG_POST_URL).append(postId)
                .toString();
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext ctx = config.getServletContext();
        blogPostDataAccess = (BlogPostDataAccess) ctx.getAttribute(
                "cn.edu.seu.cose.jellyjolly.blogPostDataAccess");
        categoryDataAccess = (CategoryDataAccess) ctx.getAttribute(
                "cn.edu.seu.cose.jellyjolly.categoryDataAccess");
        commentDataAccess = (CommentDataAccess) ctx.getAttribute(
                "cn.edu.seu.cose.jellyjolly.commentDataAccess");
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

    private void post(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        String title = request.getParameter(PARAM_TITLE);
        String content = request.getParameter(PARAM_CONTENT);
        String categoryIdParam = request.getParameter(PARAM_CATEGORY_ID);
        String categoryName = request.getParameter(PARAM_CATEGORY_NAME);

        // invalid user input
        if (title == null || content == null
                || (categoryIdParam == null && categoryName == null)
                || !Utils.isNumeric(categoryIdParam)) {
            response.sendError(400);
            return;
        }

        AdminUser user = getUser(request);
        // cannot get user object
        if (user == null) {
            return;
        }

        try {
            Category newCategory = (categoryName != null)
                    ? categoryDataAccess.createNewCategory(categoryName)
                    : null;
            int catetoryId = (categoryIdParam != null)
                    ? Integer.valueOf(categoryIdParam)
                    : newCategory.getCategoryId();
            // String[] tags = tagsParam.split(",\\s*");
            // TIP: tags not supported yet
            long userId = user.getUserId();

            Date currentDate = new Date();
            long postId = blogPostDataAccess.createNewPost(userId, catetoryId,
                    currentDate, title, content);
            response.sendRedirect(getRedirectUrl(postId));
        } catch (NumberFormatException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            response.sendError(400, ex.getMessage());
        } catch (DataAccessException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            response.sendError(500, ex.getMessage());
        }
    }

    private void edit(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        String postIdParam = request.getParameter(PARAM_POST_ID);
        String title = request.getParameter(PARAM_TITLE);
        String content = request.getParameter(PARAM_CONTENT);
        String categoryIdParam = request.getParameter(PARAM_CATEGORY_ID);

        // invalid user input
        if (!Utils.isNumeric(postIdParam)) {
            response.sendError(400);
            return;
        }

        try {
            long postId = Long.valueOf(postIdParam);

            if (title != null) {
                blogPostDataAccess.updatePostTitle(postId, title);
            }
            if (content != null) {
                blogPostDataAccess.updatePostContent(postId, content);
            }
            if (categoryIdParam != null) {
                int categoryId = Integer.valueOf(categoryIdParam);
                blogPostDataAccess.updatePostCategory(postId, categoryId);
            }
            response.sendRedirect(getRedirectUrl(postId));
        } catch (NumberFormatException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            response.sendError(400, ex.getMessage());
        } catch (DataAccessException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            response.sendError(500, ex.getMessage());
        }
    }

    private void delete(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        String postIdParam = request.getParameter(PARAM_POST_ID);
        // invalid user input
        if (postIdParam == null || !Utils.isNumeric(postIdParam)) {
            response.sendError(400);
            return;
        }

        try {
            long postId = Long.valueOf(postIdParam);
            blogPostDataAccess.deletePost(postId);
            commentDataAccess.deleteCommentsByPostId(postId);
            response.sendRedirect(HOME_URL);
        } catch (NumberFormatException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            response.sendError(400, ex.getMessage());
        } catch (DataAccessException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            response.sendError(500, ex.getMessage());
        }
    }

    private AdminUser getUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserAuthorization userAuth = (UserAuthorization) session.getAttribute(
                AdminUserLogin.SESSION_ATTRI_AUTH);
        return (userAuth == null) ? null : userAuth.getUser();
    }
}
