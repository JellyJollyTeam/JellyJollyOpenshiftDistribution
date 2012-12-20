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
package cn.edu.seu.cose.jellyjolly.controller.tag;

import cn.edu.seu.cose.jellyjolly.dao.*;
import cn.edu.seu.cose.jellyjolly.dto.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author rAy
 */
@Controller
public class AdminControlPanelController {

    private AdminUserDataAccess adminUserDataAccess;
    private BlogPageDataAccess blogPageDataAccess;
    private BlogPostDataAccess blogPostDataAccess;
    private CommentDataAccess commentDataAccess;
    private LinkDataAccess linkDataAccess;
    private long postPerPage = 10;
    private long commentPerPage = 10;
    private long userPerPage = 10;

    public AdminControlPanelController(
            AdminUserDataAccess adminUserDataAccess,
            BlogPageDataAccess blogPageDataAccess,
            BlogPostDataAccess blogPostDataAccess,
            CommentDataAccess commentDataAccess,
            LinkDataAccess linkDataAccess) {
        this.adminUserDataAccess = adminUserDataAccess;
        this.blogPageDataAccess = blogPageDataAccess;
        this.blogPostDataAccess = blogPostDataAccess;
        this.commentDataAccess = commentDataAccess;
        this.linkDataAccess = linkDataAccess;
    }

    public void setPostPerPage(long postPerPage) {
        this.postPerPage = postPerPage;
    }

    public void setCommentPerPage(long commentPerPage) {
        this.commentPerPage = commentPerPage;
    }

    public void setUserPerPage(long userPerPage) {
        this.userPerPage = userPerPage;
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String getPanelMainPage() {
        return "admin/admin";
    }

    @RequestMapping(value = "/admin/settings", method = RequestMethod.GET)
    public String getSettings() {
        return "admin/settings";
    }

    @RequestMapping(value = "/admin/posts", method = RequestMethod.GET)
    public String getPosts(Model model) throws DataAccessException {
        return getPosts(1, model);
    }

    @RequestMapping(value = "/admin/posts/page/{page}",
            method = RequestMethod.GET)
    public String getPosts(@PathVariable long page, Model model)
            throws DataAccessException {
        long offset = getOffset(page);
        long limit = postPerPage;
        List<BlogPost> postList = blogPostDataAccess.getPosts(offset, limit);
        model.addAttribute("postList", postList);
        return "admin/posts";
    }

    @RequestMapping(value = "/admin/posts/new", method = RequestMethod.GET)
    public String createNewPostPage() {
        return "admin/postEditor";
    }

    @RequestMapping(value = "/admin/posts/{postId}", method = RequestMethod.GET)
    public String editPost(@PathVariable long postId, Model model)
            throws DataAccessException {
        BlogPost post = blogPostDataAccess.getPostById(postId);
        model.addAttribute("blogPost", post);
        return (post == null) ? "redirect:/404" : "admin/postEditor";
    }

    @RequestMapping(value = "/admin/pages", method = RequestMethod.GET)
    public String getPages(Model model) throws DataAccessException {
        List<BlogPage> pageList = blogPageDataAccess.getAllPages();
        model.addAttribute("pageList", pageList);
        return "admin/pages";
    }

    @RequestMapping(value = "/admin/pages/new", method = RequestMethod.GET)
    public String createNewPage() {
        return "admin/pageEditor";
    }

    @RequestMapping(value = "/admin/pages/{pageId}", method = RequestMethod.GET)
    public String editPage(@PathVariable int pageId, Model model)
            throws DataAccessException {
        BlogPage page = blogPageDataAccess.getPage(pageId);
        model.addAttribute("blogPage", page);
        return "admin/pageEditor";
    }

    @RequestMapping(value = "/admin/comments", method = RequestMethod.GET)
    public String getComments(Model model) throws DataAccessException {
        return getComments(1, model);
    }

    public String getComments(long page, Model model)
            throws DataAccessException {
        long offset = getOffset(page);
        long limit = commentPerPage;
        List<Comment> commentList = commentDataAccess.getComments(offset,
                limit);
        model.addAttribute("commentList", commentList);
        return "admin/comments";
    }

    @RequestMapping(value = "/admin/users", method = RequestMethod.GET)
    public String getAdminUsers(Model model) throws DataAccessException {
        List<AdminUser> adminUserList = adminUserDataAccess.getAllUsers();
        model.addAttribute("adminUserList", adminUserList);
        return "admin/users";
    }

    @RequestMapping(value = "/admin/users/new", method = RequestMethod.GET)
    public String addNewAdminUsers() {
        return "admin/userEditor";
    }

    @RequestMapping(value = "/admin/users/{userId}", method = RequestMethod.GET)
    public String editAdminUser(@PathVariable long userId, Model model)
            throws DataAccessException {
        AdminUser adminUser = adminUserDataAccess.getUser(userId);
        model.addAttribute("adminUser", adminUser);
        return "admin/userEditor";
    }

    @RequestMapping(value = "/admin/links", method = RequestMethod.GET)
    public String getLinks(Model model) throws DataAccessException {
        List<Link> linkList = linkDataAccess.getAllLinks();
        model.addAttribute("linkList", linkList);
        return "admin/links";
    }

    @RequestMapping(value = "/admin/links/new", method = RequestMethod.GET)
    public String addNewLink() {
        return "admin/linkEditor";
    }

    @RequestMapping(value = "/admin/links/{linkId}", method = RequestMethod.GET)
    public String editLink(@PathVariable long linkId, Model model)
            throws DataAccessException {
        Link link = linkDataAccess.getLinkById(linkId);
        model.addAttribute("link", link);
        return "admin/linkEditor";
    }

    private long getOffset(long page) {
        return (page >= 1) ? (page - 1) * postPerPage : 0;
    }
}
