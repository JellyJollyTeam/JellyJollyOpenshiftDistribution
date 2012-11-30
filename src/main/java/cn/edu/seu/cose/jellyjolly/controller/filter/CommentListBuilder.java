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

import cn.edu.seu.cose.jellyjolly.dao.CommentDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.DataAccessException;
import cn.edu.seu.cose.jellyjolly.dto.Comment;
import cn.edu.seu.cose.jellyjolly.util.Utils;
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
public class CommentListBuilder extends HttpFilter {

    private static final long DEFAULT_OFFSET = 0;
    private static final long DEFAULT_MAX = 0;
    private static final String INFO_INVALID_INPUT =
            "CommentListBuilder: invalid input";
    private static final String PARAM_PAGE = "page";
    private static final String PARAM_MAX = "max";
    public static final String ATTRI_COMMENT_LIST = "commentlist";
    private CommentDataAccess commentDataAccess;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext ctx = filterConfig.getServletContext();
        commentDataAccess = (CommentDataAccess) ctx.getAttribute(
                "cn.edu.seu.cose.jellyjolly.commentDataAccess");
    }

    @Override
    public void doGet(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String postIdParam = request.getParameter(
                BlogPostInstanceBuilder.PARAM_BLOG_POST_ID);
        String pageParam = request.getParameter(PARAM_PAGE);
        String maxParam = request.getParameter(PARAM_MAX);

        // invalid user input
        if (!Utils.isNumericOrNull(postIdParam)
                || !Utils.isNumericOrNull(pageParam)
                || !Utils.isNumericOrNull(maxParam)) {
            response.sendError(400, INFO_INVALID_INPUT);
            return;
        }

        try {
            if (postIdParam != null) {
                long postId = Long.valueOf(postIdParam);
                doGetCommentListOfPost(request, response, postId);
            }
            if (postIdParam == null
                    && (pageParam != null && maxParam != null)) {
                long page = Long.valueOf(pageParam);
                long max = Long.valueOf(maxParam);
                long limit = (max > 0) ? max : DEFAULT_MAX;
                long offset = (page < 1) ? DEFAULT_OFFSET : (page - 1) * max;
                doGetComments(request, response, offset, limit);
            }
            if (postIdParam == null
                    && (pageParam == null || maxParam == null)) {
                doGetAllComments(request, response);
            }

            chain.doFilter(request, response);
        } catch (NumberFormatException ex) {
            response.sendError(400, ex.getMessage());
        } catch (DataAccessException ex) {
            Logger.getLogger(CommentListBuilder.class.getName())
                    .log(Level.SEVERE, ex.getMessage(), ex);
            response.sendError(500, ex.getMessage());
        }
    }

    private void doGetAllComments(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException, DataAccessException {
        List<Comment> commentList = commentDataAccess.getComments();
        request.setAttribute(ATTRI_COMMENT_LIST, commentList);
    }

    private void doGetComments(HttpServletRequest request,
            HttpServletResponse response, long offset, long limit)
            throws IOException, ServletException, DataAccessException {
        List<Comment> commentList = commentDataAccess.getComments(offset,
                limit);
        request.setAttribute(ATTRI_COMMENT_LIST, commentList);
    }

    private void doGetCommentListOfPost(HttpServletRequest request,
            HttpServletResponse response, long postId)
            throws IOException, ServletException, DataAccessException {
        List<Comment> commentList = commentDataAccess.getCommentsByPostId(
                postId,
                CommentDataAccess.CommentOrderStrategy.ORDERED_BY_DATE_DESC);
        request.setAttribute(ATTRI_COMMENT_LIST, commentList);
    }
}
