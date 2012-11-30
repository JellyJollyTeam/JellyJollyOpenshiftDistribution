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

import cn.edu.seu.cose.jellyjolly.dao.CommentDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.DataAccessException;
import cn.edu.seu.cose.jellyjolly.dto.AdminUser;
import cn.edu.seu.cose.jellyjolly.dto.Comment;
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
@WebServlet(name = "AdminCommentOperation", urlPatterns = {"/admin/comment"})
public class AdminCommentOperation extends HttpServlet
        implements ICommentOperation {

    private static final String ADMIN_COMMENT_URL = "./comments.jsp";
    private static final String INFO_INVALID_INPUT =
            "AdminCommentOperation: invalid user input";
    private static final Logger logger = Logger.getLogger(
            AdminCommentOperation.class.getName());
    private CommentDataAccess commentDataAccess;

    private static String getRedirectUrl(long postId) {
        return new StringBuilder().append(POST_URL).append(postId).toString();
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext ctx = config.getServletContext();
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
        if (OP_ADD.equalsIgnoreCase(operation)) {
            add(request, response);
            return;
        }
        if (OP_DEL.equalsIgnoreCase(operation)) {
            delete(request, response);
            return;
        }

        response.sendError(400);
    }

    private void add(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        AdminUser user = getUser(request);
        // not authorzied
        if (user == null) {
            response.sendError(401);
            return;
        }

        long userId = user.getUserId();
        String parentIdParam = request.getParameter(PARAM_PARENT_COMMENT_ID);
        String postIdParam = request.getParameter(PARAM_BLOG_POST_ID);
        String content = request.getParameter(PARAM_CONTENT);

        // invalid user input
        if (postIdParam == null || content == null) {
            response.sendError(400, INFO_INVALID_INPUT);
            return;
        }

        try {
            long postId = Long.valueOf(postIdParam);
            // without parent comment
            if (parentIdParam == null) {
                commentDataAccess.addNewComment(postId, userId, content,
                        new Date());
                response.sendRedirect(getRedirectUrl(postId));
                return;
            }

            // with parent comment
            // invalid user input
            if (!Utils.isNumeric(parentIdParam)) {
                response.sendError(400);
                return;
            }

            long parentCommentId = Long.valueOf(parentIdParam);
            commentDataAccess.addNewComment(postId, userId, content,
                    parentCommentId, new Date());
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
        AdminUser user = getUser(request);
        // not authorzied
        if (user == null) {
            response.sendError(401);
            return;
        }

        String commentIdParam = request.getParameter(PARAM_COMMENT_ID);
        // invalid user input
        if (commentIdParam == null || !Utils.isNumeric(commentIdParam)) {
            response.sendError(400);
            return;
        }

        try {
            long commentId = Long.valueOf(commentIdParam);

            Comment comment = commentDataAccess.getCommentById(commentId);
            commentDataAccess.deleteCommentById(commentId);
            response.sendRedirect(ADMIN_COMMENT_URL);
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
