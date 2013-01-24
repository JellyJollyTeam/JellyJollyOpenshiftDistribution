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

import cn.edu.seu.cose.jellyjolly.model.AdminUser;
import cn.edu.seu.cose.jellyjolly.model.Comment;
import cn.edu.seu.cose.jellyjolly.model.Category;
import cn.edu.seu.cose.jellyjolly.model.BlogPost;
import cn.edu.seu.cose.jellyjolly.model.BlogInfo;
import cn.edu.seu.cose.jellyjolly.model.Link;
import cn.edu.seu.cose.jellyjolly.model.BlogPage;
import cn.edu.seu.cose.jellyjolly.dao.*;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author rAy
 */
@Controller
public class AdminControlPanelController {

    private AdminUserDataAccess adminUserDataAccess;
    private BlogInfoDataAccess blogInfoDataAccess;
    private BlogPageDataAccess blogPageDataAccess;
    private BlogPostDataAccess blogPostDataAccess;
    private CategoryDataAccess categoryDataAccess;
    private CommentDataAccess commentDataAccess;
    private LinkDataAccess linkDataAccess;
    private AdminFrameBuilder adminFrameBuilder;
    private long postPerPage = 10;
    private long commentPerPage = 10;
    private long recentCommentNumber = 5;

    public AdminControlPanelController(
            AdminUserDataAccess adminUserDataAccess,
            BlogInfoDataAccess blogInfoDataAccess,
            BlogPageDataAccess blogPageDataAccess,
            BlogPostDataAccess blogPostDataAccess,
            CategoryDataAccess categoryDataAccess,
            CommentDataAccess commentDataAccess,
            LinkDataAccess linkDataAccess,
            AdminFrameBuilder adminFrameBuilder) {
        this.adminUserDataAccess = adminUserDataAccess;
        this.blogInfoDataAccess = blogInfoDataAccess;
        this.blogPageDataAccess = blogPageDataAccess;
        this.blogPostDataAccess = blogPostDataAccess;
        this.categoryDataAccess = categoryDataAccess;
        this.commentDataAccess = commentDataAccess;
        this.linkDataAccess = linkDataAccess;
        this.adminFrameBuilder = adminFrameBuilder;
    }

    public void setPostPerPage(long postPerPage) {
        this.postPerPage = postPerPage;
    }

    public void setCommentPerPage(long commentPerPage) {
        this.commentPerPage = commentPerPage;
    }

    public void setRecentCommentNumber(long recentCommentNumber) {
        this.recentCommentNumber = recentCommentNumber;
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String getPanelMainPage(Model model) throws DataAccessException {
        long postNumber = blogPostDataAccess.getPostNumber();
        long commentNumber = commentDataAccess.getCommentNumber();
        long pageNumber = blogPageDataAccess.getPageCount();
        List<Comment> recentComments = commentDataAccess.getRecentComments(
                recentCommentNumber);
        List<Category> categoryList = categoryDataAccess.getAllCategories();
        model.addAttribute("postNumber", postNumber);
        model.addAttribute("commentNumber", commentNumber);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("recentComments", recentComments);
        model.addAttribute("categoryList", categoryList);
        addFrameModels(model);
        return "admin/admin";
    }

    @RequestMapping(value = "/admin/settings", method = RequestMethod.GET)
    public String getSettings(Model model) throws DataAccessException {
        BlogInfo blogInfo = blogInfoDataAccess.getBlogInfoInstance();
        model.addAttribute("blogInfo", blogInfo);
        addFrameModels(model);
        return "admin/settings";
    }

    @RequestMapping(value = "/admin/settings", method = RequestMethod.POST)
    public String editSettings(@RequestParam String title,
            @RequestParam String subtitle) throws DataAccessException {
        blogInfoDataAccess.setBlogTitle(title);
        blogInfoDataAccess.setBlogSubTitle(subtitle);
        return "redirect:/admin/settings";
    }

    @RequestMapping(value = "/admin/posts", method = RequestMethod.GET,
            params = "!page")
    public String getPosts(Model model) throws DataAccessException {
        return getPosts(1, model);
    }

    @RequestMapping(value = "/admin/posts", method = RequestMethod.GET,
            params = "page")
    public String getPosts(@RequestParam long page, Model model)
            throws DataAccessException {
        long offset = getOffset(page);
        long limit = postPerPage;
        List<BlogPost> postList = blogPostDataAccess.getPosts(offset, limit);
        List<Category> categoryList = categoryDataAccess.getAllCategories();
        model.addAttribute("postList", postList);
        model.addAttribute("categoryList", categoryList);
        addFrameModels(model);
        long maxPostNumber = blogPostDataAccess.getPostNumber();
        buildPageCounterModel(page, maxPostNumber, postPerPage, model);
        return "admin/posts";
    }

    @RequestMapping(value = "/admin/posts/category/{categoryId}",
            method = RequestMethod.GET, params = "!page")
    public String getPostsByCategory(@PathVariable int categoryId,
            Model model) throws DataAccessException {
        return getPosts(categoryId, 1, model);
    }

    @RequestMapping(value = "/admin/posts/category/{categoryId}",
            method = RequestMethod.GET, params = "page")
    public String getPosts(@PathVariable int categoryId,
            @RequestParam int page, Model model)
            throws DataAccessException {
        long offset = getOffset(page);
        long limit = postPerPage;
        List<BlogPost> postList = blogPostDataAccess.getPostsByCategoryId(
                categoryId, offset, limit);
        List<Category> categoryList = categoryDataAccess.getAllCategories();
        model.addAttribute("postList", postList);
        model.addAttribute("categoryList", categoryList);
        addFrameModels(model);
        long maxPostNumber = blogPostDataAccess.getPostNumber(categoryId);
        buildPageCounterModel(page, maxPostNumber, postPerPage, model);
        return "admin/posts";
    }

    @RequestMapping(value = "/admin/posts/new", method = RequestMethod.GET)
    public String createNewPostPage(Model model) throws DataAccessException {
        List<Category> categoryList = categoryDataAccess.getAllCategories();
        model.addAttribute("categoryList", categoryList);
        addFrameModels(model);
        return "admin/postEditor";
    }

    @RequestMapping(value = "/admin/posts/{postId}/editor",
            method = RequestMethod.GET)
    public String editPost(@PathVariable long postId, Model model)
            throws DataAccessException {
        BlogPost post = blogPostDataAccess.getPostById(postId);
        List<Category> categoryList = categoryDataAccess.getAllCategories();
        model.addAttribute("blogpost", post);
        model.addAttribute("categoryList", categoryList);
        if (post == null) {
            return "redirect:/404";
        }
        addFrameModels(model);
        return "admin/postEditor";
    }

    @RequestMapping(value = "/admin/pages", method = RequestMethod.GET)
    public String getPages(Model model) throws DataAccessException {
        List<BlogPage> pageList = blogPageDataAccess.getAllPages();
        model.addAttribute("pageList", pageList);
        addFrameModels(model);
        return "admin/pages";
    }

    @RequestMapping(value = "/admin/pages/new", method = RequestMethod.GET)
    public String createNewPage(Model model) throws DataAccessException {
        addFrameModels(model);
        return "admin/pageEditor";
    }

    @RequestMapping(value = "/admin/pages/{pageId}/editor",
            method = RequestMethod.GET)
    public String editPage(@PathVariable int pageId, Model model)
            throws DataAccessException {
        BlogPage page = blogPageDataAccess.getPage(pageId);
        model.addAttribute("blogpage", page);
        addFrameModels(model);
        return "admin/pageEditor";
    }

    @RequestMapping(value = "/admin/comments", method = RequestMethod.GET)
    public String getComments(Model model) throws DataAccessException {
        return getComments(1, model);
    }

    @RequestMapping(value = "/admin/comments/page/{page}",
            method = RequestMethod.GET)
    public String getComments(@PathVariable long page, Model model)
            throws DataAccessException {
        long offset = getOffset(page);
        long limit = commentPerPage;
        List<Comment> commentList = commentDataAccess.getComments(offset,
                limit);
        long maxCommentNumber = commentDataAccess.getCommentNumber();
        model.addAttribute("commentList", commentList);
        addFrameModels(model);
        buildPageCounterModel(page, maxCommentNumber, commentPerPage, model);
        return "admin/comments";
    }

    @RequestMapping(value = "/admin/users", method = RequestMethod.GET)
    public String getAdminUsers(Model model) throws DataAccessException {
        List<AdminUser> adminUserList = adminUserDataAccess.getAllUsers();
        model.addAttribute("adminUserList", adminUserList);
        addFrameModels(model);
        return "admin/users";
    }

    @RequestMapping(value = "/admin/users/new", method = RequestMethod.GET)
    public String addNewAdminUsers(Model model) throws DataAccessException {
        addFrameModels(model);
        return "admin/userEditor";
    }

    @RequestMapping(value = "/admin/users/{userId}/password", method = RequestMethod.GET)
    public String changePassword(@PathVariable long userId, Model model)
            throws DataAccessException {
        AdminUser adminUser = adminUserDataAccess.getUser(userId);
        model.addAttribute("adminUser", adminUser);
        addFrameModels(model);
        return "admin/changePassword";
    }

    @RequestMapping(value = "/admin/users/{userId}", method = RequestMethod.GET)
    public String editAdminUser(@PathVariable long userId, Model model)
            throws DataAccessException {
        AdminUser adminUser = adminUserDataAccess.getUser(userId);
        model.addAttribute("adminUser", adminUser);
        addFrameModels(model);
        return "admin/userEditor";
    }

    @RequestMapping(value = "/admin/links", method = RequestMethod.GET)
    public String getLinks(Model model) throws DataAccessException {
        List<Link> linkList = linkDataAccess.getAllLinks();
        model.addAttribute("linkList", linkList);
        addFrameModels(model);
        return "admin/links";
    }

    @RequestMapping(value = "/admin/links/new", method = RequestMethod.GET)
    public String addNewLink(Model model) throws DataAccessException {
        addFrameModels(model);
        return "admin/linkEditor";
    }

    @RequestMapping(value = "/admin/links/{linkId}", method = RequestMethod.GET)
    public String editLink(@PathVariable long linkId, Model model)
            throws DataAccessException {
        Link link = linkDataAccess.getLinkById(linkId);
        model.addAttribute("link", link);
        addFrameModels(model);
        return "admin/linkEditor";
    }

    @RequestMapping(value = "/admin", method = RequestMethod.PUT)
    public String addNewAdminUser(@RequestParam String username,
                                  @RequestParam String password, @RequestParam String email,
                                  @RequestParam String homePage) throws DataAccessException {
        Date currentDate = new Date();
        adminUserDataAccess.addNewUser(username, password, email, username,
                homePage, currentDate);
        return "redirect:/admin/admins";
    }

    @RequestMapping(value = "/admin/{userId}", method = RequestMethod.DELETE)
    public String deleteAdminUser(@PathVariable long userId)
            throws DataAccessException {
        adminUserDataAccess.deleteUser(userId);
        return "redirect:/admin/admins";
    }

    @RequestMapping(value = "/admin/{userId}/username",
            method = RequestMethod.PUT)
    public String changeUsername(@PathVariable long userId,
            @RequestParam String username) throws DataAccessException {
        adminUserDataAccess.changeUserName(userId, username);
        return "redirect:/admin/admins";
    }

    @RequestMapping(value = "/admin/{userId}/password",
            method = RequestMethod.PUT)
    public String changePassword(@PathVariable long userId,
                                 @RequestParam String oldPassword, @RequestParam String newPassword,
                                 @RequestParam String confirmPassword) throws DataAccessException {
        AdminUser userToBeConfirmed = adminUserDataAccess.getUser(userId);
        String username = userToBeConfirmed.getUsername();
        boolean confirmed = adminUserDataAccess.confirm(username, oldPassword);
        if (!confirmed) {
            // indicate not authorized
            return "redirect:/admin/admins/" + userId + "?err=1";
        }
        if (!newPassword.equals(confirmPassword)) {
            // indicate passwords not same
            return "redirect:/admin/admins/" + userId + "?err=2";
        }
        adminUserDataAccess.changePassword(userId, newPassword);
        return "redirect:/admin/admins" + userId;
    }

    @RequestMapping(value = "/admin/{userId}/displayName",
            method = RequestMethod.PUT)
    public String changeDisplayName(@PathVariable long userId,
                                    @RequestParam String displayName) throws DataAccessException {
        AdminUser userToBeUpdated = adminUserDataAccess.getUser(userId);
        userToBeUpdated.setDisplayName(displayName);
        adminUserDataAccess.updateUserExceptPassword(userToBeUpdated);
        return "redirect:/admin/admins" + userId;
    }

    @RequestMapping(value = "/admin/{userId}/email",
            method = RequestMethod.PUT)
    public String changeEmail(@PathVariable long userId,
                              @RequestParam String email) throws DataAccessException {
        AdminUser userToBeUpdated = adminUserDataAccess.getUser(userId);
        userToBeUpdated.setEmail(email);
        adminUserDataAccess.updateUserExceptPassword(userToBeUpdated);
        return "redirect:/admin/admins" + userId;
    }

    @RequestMapping(value = "/admin/{userId}/homePage",
            method = RequestMethod.PUT)
    public String changeHomePage(@PathVariable long userId,
                                 @RequestParam String homePage) throws DataAccessException {
        AdminUser userToBeUpdated = adminUserDataAccess.getUser(userId);
        userToBeUpdated.setHomePageUrl(homePage);
        adminUserDataAccess.updateUserExceptPassword(userToBeUpdated);
        return "redirect:/admin/admins" + userId;
    }

    private void addFrameModels(Model model) throws DataAccessException {
        model.addAllAttributes(adminFrameBuilder.getFrameObjects());
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

    private long getOffset(long page) {
        return (page >= 1) ? (page - 1) * postPerPage : 0;
    }
}
