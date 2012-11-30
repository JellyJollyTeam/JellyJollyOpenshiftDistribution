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
import cn.edu.seu.cose.jellyjolly.dto.BlogPost;
import cn.edu.seu.cose.jellyjolly.util.Utils;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
public class BlogPostInstanceBuilder extends HttpFilter {

    private static final String INFO_INVALID_INPUT =
            "BlogPostInstanceBuilder: invalid input";
    public static final String ATTRI_BLOG_POST = "blogpost";
    public static final String PARAM_BLOG_POST_ID = "postid";
    private BlogPostDataAccess blogPostDataAccess;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);
        blogPostDataAccess = (BlogPostDataAccess) filterConfig
                .getServletContext().getAttribute(
                "cn.edu.seu.cose.jellyjolly.blogPostDataAccess");
    }

    @Override
    public void doGet(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String blogPostIdParam = request.getParameter(PARAM_BLOG_POST_ID);

        // invalid user input
        if (blogPostIdParam == null || !Utils.isNumeric(blogPostIdParam)) {
            response.sendError(400, INFO_INVALID_INPUT);
            return;
        }

        try {
            long blogPostId = Long.valueOf(blogPostIdParam);
            BlogPost post = blogPostDataAccess.getPostById(blogPostId);
            request.setAttribute(ATTRI_BLOG_POST, post);
        } catch (NumberFormatException ex) {
            response.sendError(400, ex.getMessage());
        } catch (DataAccessException ex) {
            Logger.getLogger(BlogPostInstanceBuilder.class.getName())
                    .log(Level.SEVERE, ex.getMessage(), ex);
            response.sendError(500, ex.getMessage());
        }

        chain.doFilter(request, response);
    }
}
