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
import cn.edu.seu.cose.jellyjolly.dao.CategoryDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.DataAccessException;
import cn.edu.seu.cose.jellyjolly.model.BlogPost;
import cn.edu.seu.cose.jellyjolly.model.session.UserAuthorization;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
public class BlogPostController {

    private BlogPostDataAccess blogPostDataAccess;
    private CategoryDataAccess categoryDataAccess;
    private FrameBuilder frameBuilder;

    public BlogPostController(BlogPostDataAccess blogPostDataAccess,
            CategoryDataAccess categoryDataAccess, FrameBuilder frameBuilder) {
        this.blogPostDataAccess = blogPostDataAccess;
        this.categoryDataAccess = categoryDataAccess;
        this.frameBuilder = frameBuilder;
    }

    @RequestMapping(value = "/post/{postId}", method = RequestMethod.GET)
    public String getPostById(@PathVariable long postId, Model model,
            HttpServletRequest request) throws DataAccessException {
        BlogPost post = blogPostDataAccess.getPostById(postId);
        if (post == null) {
            return "redirect:/404";
        }
        model.addAttribute("blogpost", post);
        model.addAllAttributes(frameBuilder.getFrameObjects(request));
        return "post";
    }

    @RequestMapping(value = "/admin/post/simple", method = RequestMethod.POST)
    public String createNewPost(@RequestParam int categoryId,
            @RequestParam String title, @RequestParam String content,
            HttpServletRequest request) throws DataAccessException {
        return createNewPost(categoryId, null, title, content, request);
    }

    @RequestMapping(value = "/admin/post", method = RequestMethod.POST)
    public String createNewPost(@RequestParam int categoryId,
            @RequestParam String newCategoryName, @RequestParam String title,
            @RequestParam String content, HttpServletRequest request)
            throws DataAccessException {
        boolean createNewCategory = (newCategoryName != null) &&
                (!newCategoryName.trim().equals(""));
        int categoryIdToBePost = createNewCategory
                ? categoryDataAccess.createNewCategory(newCategoryName)
                .getCategoryId()
                : categoryId;
        HttpSession currentSession = request.getSession();
        UserAuthorization userAuth = (UserAuthorization) currentSession
                .getAttribute(AdminUserController.SESSION_ATTRI_AUTH);
        long authorUserId = userAuth.getUser().getUserId();
        Date currentTime = new Date();
        long postId = blogPostDataAccess.createNewPost(authorUserId,
                categoryIdToBePost, currentTime, title, content);
        return "redirect:/post/" + postId;
    }

    @RequestMapping(value = "/admin/post",
    method = RequestMethod.DELETE)
    public String deletePost(@RequestParam List<Long> postIds,
            @RequestParam String redirect) throws DataAccessException {
        for (long postId : postIds) {
            blogPostDataAccess.deletePost(postId);
        }
        return "redirect:" + redirect;
    }

    @RequestMapping(value = "/admin/post/{postId}",
    method = RequestMethod.DELETE)
    public String deletePost(@PathVariable long postId,
            @RequestParam String redirect) throws DataAccessException {
        blogPostDataAccess.deletePost(postId);
        return "redirect:" + redirect;
    }

    @RequestMapping(value = "/admin/post/{postId}", method = RequestMethod.PUT)
    public String changePost(@PathVariable long postId,
            @RequestParam String newCategoryName,
            @RequestParam String title, @RequestParam String content,
            @RequestParam int categoryId) throws DataAccessException {
        boolean createNewCategory = (newCategoryName != null &&
                !newCategoryName.trim().equals(""));
        int categoryIdToBePost = createNewCategory
                ? categoryDataAccess.createNewCategory(newCategoryName)
                .getCategoryId()
                : categoryId;
        blogPostDataAccess.updatePostCategory(postId, categoryIdToBePost);
        blogPostDataAccess.updatePostContent(postId, content);
        blogPostDataAccess.updatePostTitle(postId, title);
        return "redirect:/post/" + postId;
    }

    @RequestMapping(value = "/admin/post/{postId}/title",
    method = RequestMethod.PUT)
    public String changePostTitle(@PathVariable long postId,
            @RequestParam String title, @RequestParam String redirect)
            throws DataAccessException {
        blogPostDataAccess.updatePostTitle(postId, title);
        return "redirect:" + redirect;
    }

    @RequestMapping(value = "/admin/post/{postId}/content",
    method = RequestMethod.PUT)
    public String changePostContent(@PathVariable long postId,
            @RequestParam String content, @RequestParam String redirect)
            throws DataAccessException {
        blogPostDataAccess.updatePostContent(postId, content);
        return "redirect:" + redirect;
    }

    @RequestMapping(value = "/admin/post/{postId}/category",
    method = RequestMethod.PUT)
    public String changePostCategory(@PathVariable long postId,
            @RequestParam int category, @RequestParam String redirect)
            throws DataAccessException {
        blogPostDataAccess.updatePostCategory(postId, category);
        return "redirect:" + redirect;
    }
}
