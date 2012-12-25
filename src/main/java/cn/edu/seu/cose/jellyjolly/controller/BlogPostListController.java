/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.seu.cose.jellyjolly.controller;

import cn.edu.seu.cose.jellyjolly.dao.BlogPostDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.DataAccessException;
import cn.edu.seu.cose.jellyjolly.dto.BlogPost;
import cn.edu.seu.cose.jellyjolly.util.Utils;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
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

    @RequestMapping(value = "/posts/page/{page}", method = RequestMethod.GET)
    public String getPostsByPage(@PathVariable long page, Model model,
            HttpServletRequest request) throws DataAccessException {
        long offset = getOffset(page);
        long limit = postNumberPerPage;
        List<BlogPost> postList = blogPostDataAccess.getPosts(offset, limit);
        return getPostListView(postList, (offset == 0), model, request);
    }

    @RequestMapping(value = "/category/{categoryId}",
    method = RequestMethod.GET)
    public String getPostsByCategory(@PathVariable int categoryId,
            Model model, HttpServletRequest request)
            throws DataAccessException {
        return getPostsByCategory(categoryId, 1, model, request);
    }

    @RequestMapping(value = "/category/{categoryId}/page/{page}",
    method = RequestMethod.GET)
    public String getPostsByCategory(@PathVariable int categoryId,
            @PathVariable long page, Model model, HttpServletRequest request)
            throws DataAccessException {
        long offset = getOffset(page);
        long limit = postNumberPerPage;
        List<BlogPost> postList = blogPostDataAccess.getPostsByCategoryId(
                categoryId, offset, limit);
        return getPostListView(postList, (offset == 0), model, request);
    }

    @RequestMapping(value = "/archive/{year}/{month}",
    method = RequestMethod.GET)
    public String getArchive(@PathVariable int year,
            @PathVariable int month, Model model, HttpServletRequest request)
            throws DataAccessException {
        return getArchive(year, month, 1, model, request);
    }

    @RequestMapping(value = "/archive/{year}/{month}/page/{page}",
    method = RequestMethod.GET)
    public String getArchive(@PathVariable int year,
            @PathVariable int month, @PathVariable long page, Model model,
            HttpServletRequest request) throws DataAccessException {
        long offset = getOffset(page);
        long limit = postNumberPerPage;
        List<BlogPost> postList = blogPostDataAccess.getPostsByMonthlyArchive(
                year, month, offset, limit);
        return getPostListView(postList, (offset == 0), model, request);
    }

    @RequestMapping(value = "/search/{keyword}", method = RequestMethod.GET)
    public String search(@PathVariable String keyword, Model model,
            HttpServletRequest request) throws DataAccessException {
        return search(keyword, 1, model, request);
    }

    @RequestMapping(value = "/search/{keyword}/page/{page}",
    method = RequestMethod.GET)
    public String search(@PathVariable String keyword, @PathVariable long page,
            Model model, HttpServletRequest request)
            throws DataAccessException {
        long offset = getOffset(page);
        long limit = postNumberPerPage;
        List<BlogPost> postList = blogPostDataAccess.getPostsByKeyword(keyword,
                offset, limit);
        return getPostListView(postList, (offset == 0), model, request);
    }

    private String getPostListView(List<BlogPost> postList, boolean firstPage,
                                   Model model, HttpServletRequest request)
            throws DataAccessException {
        if (!firstPage && (postList == null || postList.size() <= 0)) {
            return "redirect:/404";
        }
        truncatePosts(postList);
        model.addAttribute("postList", postList);
        model.addAllAttributes(frameBuilder.getFrameObjects(request));
        return "home";
    }

    private long getOffset(long page) {
        return (page >= 1) ? (page - 1) * postNumberPerPage : 0;
    }

    private void truncatePosts(Collection<BlogPost> posts) {
        for (BlogPost post : posts) {
            String content = post.getContent();
            String truncated = Utils.truncateHtml(content, postContentMaxLength);
            post.setContent(truncated);
        }
    }
}
