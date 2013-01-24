/*
 * Copyright (C) 2012 College of Software Engineering, Southeast University
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
package cn.edu.seu.cose.jellyjolly.controller;

import cn.edu.seu.cose.jellyjolly.dao.BlogPostDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.DataAccessException;
import cn.edu.seu.cose.jellyjolly.model.BlogPost;
import cn.edu.seu.cose.jellyjolly.util.Utils;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
@Controller
public class BlogPostListController {

    private BlogPostDataAccess blogPostDataAccess;
    private FrameBuilder frameBuilder;
    private int postNumberPerPage = 5;
    private int postContentMaxLength = 300;

    public BlogPostListController(BlogPostDataAccess blogPostDataAccess,
            FrameBuilder frameBuilder) {
        this.blogPostDataAccess = blogPostDataAccess;
        this.frameBuilder = frameBuilder;
    }

    public void setPostNumberPerPage(int postNumberPerPage) {
        this.postNumberPerPage = postNumberPerPage;
    }

    public void setPostContentMaxLength(int postContentMaxLength) {
        this.postContentMaxLength = postContentMaxLength;
    }

    @RequestMapping(value = {"/", "/posts"}, method = RequestMethod.GET)
    public String getNewestPosts(Model model, HttpServletRequest request)
            throws DataAccessException {
        return getPostsByPage(1, model, request);
    }

    @RequestMapping(value = "/posts", method = RequestMethod.GET,
            params = "page")
    public String getPostsByPage(@RequestParam long page, Model model,
            HttpServletRequest request) throws DataAccessException {
        long offset = getOffset(page);
        long limit = postNumberPerPage;
        List<BlogPost> postList = blogPostDataAccess.getPosts(offset, limit);
        long maxNumber = blogPostDataAccess.getPostNumber();
        return getPostListView(postList, page, maxNumber, model, request);
    }

    @RequestMapping(value = "/category/{categoryId}",
            method = RequestMethod.GET)
    public String getPostsByCategory(@PathVariable int categoryId,
            Model model, HttpServletRequest request)
            throws DataAccessException {
        return getPostsByCategory(categoryId, 1, model, request);
    }

    @RequestMapping(value = "/category/{categoryId}",
            method = RequestMethod.GET, params = "page")
    public String getPostsByCategory(@PathVariable int categoryId,
            @RequestParam long page, Model model, HttpServletRequest request)
            throws DataAccessException {
        long offset = getOffset(page);
        long limit = postNumberPerPage;
        List<BlogPost> postList = blogPostDataAccess.getPostsByCategoryId(
                categoryId, offset, limit);
        long maxNumber = blogPostDataAccess.getPostNumber(categoryId);
        return getPostListView(postList, page, maxNumber, model, request);
    }

    @RequestMapping(value = "/archive/{year}/{month}",
            method = RequestMethod.GET)
    public String getArchive(@PathVariable int year,
            @PathVariable int month, Model model, HttpServletRequest request)
            throws DataAccessException {
        return getArchive(year, month, 1, model, request);
    }

    @RequestMapping(value = "/archive/{year}/{month}",
            method = RequestMethod.GET, params = "page")
    public String getArchive(@PathVariable int year,
            @PathVariable int month, @RequestParam long page, Model model,
            HttpServletRequest request) throws DataAccessException {
        long offset = getOffset(page);
        long limit = postNumberPerPage;
        List<BlogPost> postList = blogPostDataAccess.getPostsByMonthlyArchive(
                year, month, offset, limit);
        long maxNumber = blogPostDataAccess.getPostNumber(year, month);
        return getPostListView(postList, page, maxNumber, model, request);
    }

    @RequestMapping(value = "/search/{keyword}", method = RequestMethod.GET)
    public String search(@PathVariable String keyword, Model model,
            HttpServletRequest request) throws DataAccessException {
        return search(keyword, 1, model, request);
    }

    @RequestMapping(value = "/search/{keyword}",
            method = RequestMethod.GET, params = "page")
    public String search(@PathVariable String keyword, @RequestParam long page,
            Model model, HttpServletRequest request)
            throws DataAccessException {
        long offset = getOffset(page);
        long limit = postNumberPerPage;
        List<BlogPost> postList = blogPostDataAccess.getPostsByKeyword(keyword,
                offset, limit);
        long maxNumber = blogPostDataAccess.getPostNumber(keyword);
        return getPostListView(postList, page, maxNumber, model, request);
    }

    private String getPostListView(List<BlogPost> postList, long page,
            long maxNumber, Model model, HttpServletRequest request)
            throws DataAccessException {
        boolean firstPage = page == 1;
        if (!firstPage && (postList == null || postList.size() <= 0)) {
            return "redirect:/404";
        }
        buildPageCounterModel(page, maxNumber, postNumberPerPage, model);
        truncatePosts(postList);
        model.addAttribute("postList", postList);
        model.addAllAttributes(frameBuilder.getFrameObjects(request));
        return "home";
    }

    private long getOffset(long page) {
        return (page >= 1) ? (page - 1) * postNumberPerPage : 0;
    }

    private void buildPageCounterModel(long page, long maxNumber,
            long numberPerPage, Model model) {
        long maxPage = (maxNumber - 1) / numberPerPage + 1;
        boolean hasPrev = (page > 1);
        boolean hasNext = (page < maxPage);
        model.addAttribute("hasPrev", hasPrev);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("pageNum", page);
    }

    private void truncatePosts(Collection<BlogPost> posts) {
        for (BlogPost post : posts) {
            String content = post.getContent();
            String truncated = Utils.truncateHtml(content,
                    postContentMaxLength);
            post.setContent(truncated);
        }
    }
}
