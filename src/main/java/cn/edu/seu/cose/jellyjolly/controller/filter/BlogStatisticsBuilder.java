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
import cn.edu.seu.cose.jellyjolly.dao.BlogPostDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.CategoryDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.CommentDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.DataAccessException;
import cn.edu.seu.cose.jellyjolly.dto.BlogStatistics;
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
public class BlogStatisticsBuilder extends HttpFilter {

    private static final String ATTR_BLOG_STATISTICS = "blogstat";
    private BlogPageDataAccess blogPageDataAccess;
    private BlogPostDataAccess blogPostDataAccess;
    private CategoryDataAccess categoryDataAccess;
    private CommentDataAccess commentDataAccess;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext ctx = filterConfig.getServletContext();
        blogPageDataAccess = (BlogPageDataAccess) ctx.getAttribute(
                "cn.edu.seu.cose.jellyjolly.blogPageDataAccess");
        blogPostDataAccess = (BlogPostDataAccess) ctx.getAttribute(
                "cn.edu.seu.cose.jellyjolly.blogPostDataAccess");
        categoryDataAccess = (CategoryDataAccess) ctx.getAttribute(
                "cn.edu.seu.cose.jellyjolly.categoryDataAccess");
        commentDataAccess = (CommentDataAccess) ctx.getAttribute(
                "cn.edu.seu.cose.jellyjolly.commentDataAccess");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        try {
            long postCount = blogPostDataAccess.getPostNumber();

            int pageCount = blogPageDataAccess.getPageCount();

            int categoryCount = categoryDataAccess.getCategoryNumber();

            long commentCount = commentDataAccess.getCommentNumber();

            BlogStatistics statistics = new BlogStatistics();
            statistics.setCategoryCount(categoryCount);
            statistics.setCommentCount(commentCount);
            statistics.setPageCount(pageCount);
            statistics.setPostCount(postCount);

            request.setAttribute(ATTR_BLOG_STATISTICS, statistics);
            chain.doFilter(request, response);
        } catch (DataAccessException ex) {
            Logger.getLogger(BlogStatisticsBuilder.class.getName())
                    .log(Level.SEVERE, ex.getMessage(), ex);
            response.sendError(500, ex.getMessage());
        }
    }
}
