/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.seu.cose.jellyjolly.controller;

import cn.edu.seu.cose.jellyjolly.dao.BlogPostDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.DataAccessException;
import cn.edu.seu.cose.jellyjolly.dto.BlogPost;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
@Controller
public class BlogPostListController {

    private BlogPostDataAccess blogPostDataAccess;
    private FrameBuilder frameBuilder;
    private int postNumberPerPage = 5;

    public BlogPostListController(BlogPostDataAccess blogPostDataAccess,
            FrameBuilder frameBuilder) {
        this.blogPostDataAccess = blogPostDataAccess;
        this.frameBuilder = frameBuilder;
    }

    public void setPostNumberPerPage(int postNumberPerPage) {
        this.postNumberPerPage = postNumberPerPage;
    }

    @RequestMapping(value = {"/", "/posts"}, method = RequestMethod.GET)
    public String getNewestPosts(Model model) throws DataAccessException {
        return getPostsByPage(1, model);
    }

    @RequestMapping(value = "/posts/page/{page}", method = RequestMethod.GET)
    public String getPostsByPage(@PathVariable long page, Model model)
            throws DataAccessException {
        long offset = getOffset(page);
        long limit = postNumberPerPage;
        List<BlogPost> postList = blogPostDataAccess.getPosts(offset, limit);
        return getPostListView(postList, model);
    }

    @RequestMapping(value = "/category/{categoryId}",
    method = RequestMethod.GET)
    public String getPostsByCategory(@PathVariable int categoryId,
            Model model) throws DataAccessException {
        return getPostsByCategory(categoryId, 1, model);
    }

    @RequestMapping(value = "/category/{categoryId}/page/{page}",
    method = RequestMethod.GET)
    public String getPostsByCategory(@PathVariable int categoryId,
            @PathVariable long page, Model model) throws DataAccessException {
        long offset = getOffset(page);
        long limit = postNumberPerPage;
        List<BlogPost> postList = blogPostDataAccess.getPostsByCategoryId(
                categoryId, offset, limit);
        return getPostListView(postList, model);
    }

    @RequestMapping(value = "/archive/{year}/{month}",
    method = RequestMethod.GET)
    public String getArchive(@PathVariable int year,
            @PathVariable int month, Model model) throws DataAccessException {
        return getArchive(year, month, 1, model);
    }

    @RequestMapping(value = "/archive/{year}/{month}/page/{page}",
    method = RequestMethod.GET)
    public String getArchive(@PathVariable int year,
            @PathVariable int month, @PathVariable long page, Model model)
            throws DataAccessException {
        long offset = getOffset(page);
        long limit = postNumberPerPage;
        List<BlogPost> postList = blogPostDataAccess.getPostsByMonthlyArchive(
                year, month, offset, limit);
        return getPostListView(postList, model);
    }

    @RequestMapping(value = "/search/{keyword}", method = RequestMethod.GET)
    public String search(@PathVariable String keyword, Model model)
            throws DataAccessException {
        return search(keyword, 1, model);
    }

    @RequestMapping(value = "/search/{keyword}/page/{page}",
    method = RequestMethod.GET)
    public String search(@PathVariable String keyword, @PathVariable long page,
            Model model) throws DataAccessException {
        long offset = getOffset(page);
        long limit = postNumberPerPage;
        List<BlogPost> postList = blogPostDataAccess.getPostsByKeyword(keyword,
                offset, limit);
        return getPostListView(postList, model);
    }

    private String getPostListView(List<BlogPost> postList, Model model)
            throws DataAccessException {
        if (postList == null || postList.size() <= 0) {
            return "redirect:/404";
        }
        model.addAttribute("postList", postList);
        model.addAllAttributes(frameBuilder.getFrameObjects());
        return "home";
    }

    private long getOffset(long page) {
        return (page >= 1) ? (page - 1) * postNumberPerPage : 0;
    }
}
