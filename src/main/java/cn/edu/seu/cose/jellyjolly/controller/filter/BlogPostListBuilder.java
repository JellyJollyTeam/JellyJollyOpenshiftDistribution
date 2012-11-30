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
import java.util.List;
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
public class BlogPostListBuilder extends HttpFilter {

    private static final String INFO_INVALID_INPUT =
            "BlogPostListBuilder: invalid input";
    public static final String ATTRI_POST_LIST = "postList";
    public static final String ATTRI_POST_COUNT = "postCount";
    private static final String PARAM_MAX_PER_PAGE = "max";
    private static final String PARAM_PAGE_NUM = "page";
    private static final String PARAM_YEAR = "year";
    private static final String PARAM_MONTH = "month";
    private static final String PARAM_CATEGORY_ID = "categoryid";
    private static final String PARAM_KEYWORD = "keyword";
    private static final long DEFAULT_PAGE = 1;
    private static final long DEFAULT_MAX = 5;
    private static final long DEFAULT_OFFSET = 0;
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
        String pageParam = request.getParameter(PARAM_PAGE_NUM);
        String categoryIdParam = request.getParameter(PARAM_CATEGORY_ID);
        String maxParam = request.getParameter(PARAM_MAX_PER_PAGE);
        String yearParam = request.getParameter(PARAM_YEAR);
        String monthParam = request.getParameter(PARAM_MONTH);
        String keyword = request.getParameter(PARAM_KEYWORD);

        // invalid user input
        if (!Utils.isNumericOrNull(pageParam)
                || !Utils.isNumericOrNull(categoryIdParam)
                || !Utils.isNumericOrNull(maxParam)
                || !Utils.isNumericOrNull(yearParam)
                || !Utils.isNumericOrNull(monthParam)) {
            response.sendError(400, INFO_INVALID_INPUT);
            return;
        }

        // get post list by page number
        if ((yearParam == null || monthParam == null) && keyword == null) {
            doGetPostList(request, response, chain,
                    pageParam, maxParam, categoryIdParam);
            return;
        }

        // get post list by keyword
        if (keyword != null) {
            doGetSearchResult(request, response, chain, keyword, pageParam, maxParam);
            return;
        }

        // get post list by monthly archive
        doMontlyArchive(request, response, chain,
                yearParam, monthParam, pageParam, maxParam);
    }

    private void doGetPostList(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain,
            String pageParam, String maxParam, String categoryIdParam)
            throws IOException, ServletException {
        try {
            long page = (pageParam == null) ? DEFAULT_PAGE : Long.valueOf(pageParam);
            long max = (maxParam == null) ? DEFAULT_MAX : Long.valueOf(maxParam);

            long offset = ((page - 1) < 0)
                    ? DEFAULT_OFFSET
                    : (page - 1) * max;
            long limit = max;

            // get posts of a category
            if (categoryIdParam != null) {
                int categoryId = Integer.valueOf(categoryIdParam);
                List<BlogPost> postList = blogPostDataAccess.getPostsByCategoryId(categoryId,
                        offset, limit,
                        BlogPostDataAccess.BlogPostOrderStrategy.ORDERED_BY_DATE_DESC);
                long postCount = blogPostDataAccess.getPostNumber(categoryId);
                buildBeans(request, postList, postCount);
            }

            // get all posts
            if (categoryIdParam == null) {
                List<BlogPost> postList = blogPostDataAccess.getPosts(offset, limit,
                        BlogPostDataAccess.BlogPostOrderStrategy.ORDERED_BY_DATE_DESC);
                request.setAttribute(ATTRI_POST_LIST, postList);
                long postCount = blogPostDataAccess.getPostNumber();
                buildBeans(request, postList, postCount);
            }
        } catch (NumberFormatException ex) {
            response.sendError(400, ex.getMessage());
            return;
        } catch (DataAccessException ex) {
            Logger.getLogger(BlogPostListBuilder.class.getName())
                    .log(Level.SEVERE, ex.getMessage(), ex);
            response.sendError(500, ex.getMessage());
            return;
        }

        chain.doFilter(request, response);
    }

    private void doMontlyArchive(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain,
            String yearParam, String monthParam, String pageParam, String maxParam)
            throws IOException, ServletException {
        // invalid user input
        if (yearParam == null || !Utils.isNumeric(yearParam)
                || monthParam == null || !Utils.isNumeric(monthParam)
                || maxParam == null || !Utils.isNumeric(maxParam)) {
            response.sendError(400, INFO_INVALID_INPUT);
            return;
        }

        try {
            int year = Integer.valueOf(yearParam);
            int month = Integer.valueOf(monthParam);
            long max = Long.valueOf(maxParam);

            long page = (pageParam == null) ? 1 : Long.valueOf(pageParam);

            long offset = (page - 1) * max;
            long limit = max;

            List<BlogPost> postList = blogPostDataAccess.getPostsByMonthlyArchive(year,
                    month, offset, limit,
                    BlogPostDataAccess.BlogPostOrderStrategy.ORDERED_BY_DATE_DESC);
            long postCount = blogPostDataAccess.getPostNumber(year, month);
            buildBeans(request, postList, postCount);
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

    private void doGetSearchResult(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain,
            String keyword, String pageParam, String maxParam)
            throws IOException, ServletException {
        try {
            long page = (pageParam == null) ? DEFAULT_PAGE : Long.valueOf(pageParam);
            long max = (maxParam == null) ? DEFAULT_MAX : Long.valueOf(maxParam);

            long offset = ((page - 1) < 0)
                    ? DEFAULT_OFFSET
                    : (page - 1) * max;
            long limit = max;
            List<BlogPost> postList =
                    blogPostDataAccess.getPostsByKeyword(keyword, offset, limit);
            long postCount = blogPostDataAccess.getPostNumber(keyword);
            buildBeans(request, postList, postCount);
            chain.doFilter(request, response);
        } catch (NumberFormatException ex) {
            response.sendError(400, ex.getMessage());
        } catch (DataAccessException ex) {
            Logger.getLogger(CommentListBuilder.class.getName())
                    .log(Level.SEVERE, ex.getMessage(), ex);
            response.sendError(500, ex.getMessage());
        }
    }

    private void buildBeans(HttpServletRequest request, List<BlogPost> postList, long postCount) {
        request.setAttribute(ATTRI_POST_LIST, postList);
        request.setAttribute(ATTRI_POST_COUNT, postCount);
    }
}
