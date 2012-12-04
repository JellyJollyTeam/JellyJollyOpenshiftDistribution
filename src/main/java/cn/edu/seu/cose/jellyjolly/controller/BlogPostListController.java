/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.seu.cose.jellyjolly.controller;

import cn.edu.seu.cose.jellyjolly.dao.BlogPostDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.DataAccessException;
import cn.edu.seu.cose.jellyjolly.dto.BlogPost;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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

    @RequestMapping("/")
    public ModelAndView getNewestPosts() throws DataAccessException {
        return getPostsByPage(1);
    }

    @RequestMapping("/page/{page}")
    public ModelAndView getPostsByPage(@PathVariable long page)
            throws DataAccessException {
        long offset = (page >= 1) ? (page - 1) * postNumberPerPage : 0;
        long limit = postNumberPerPage;
        List<BlogPost> postList =
                blogPostDataAccess.getPosts(offset, limit);
        ModelAndView home = new ModelAndView("home");
        home.addObject("postList", postList);
        home.addAllObjects(frameBuilder.getFrameObjects());
        return home;
    }

    @RequestMapping("/category/{categoryId}")
    public ModelAndView getPostsByCategory(@PathVariable long categoryId)
            throws DataAccessException {
        return getPostsByCategory(categoryId, 1);
    }

    @RequestMapping("/category/{categoryId}/page/{page}")
    public ModelAndView getPostsByCategory(@PathVariable long categoryId,
            @PathVariable long page) throws DataAccessException {
        long offset = (page >= 1) ? (page - 1) * postNumberPerPage : 0;
        long limit = postNumberPerPage;
        List<BlogPost> postList = blogPostDataAccess.getPostsByCategoryId(
                postNumberPerPage, offset, limit);
        ModelAndView home = new ModelAndView("home");
        home.addObject("postList", postList);
        home.addAllObjects(frameBuilder.getFrameObjects());
        return home;
    }

    @RequestMapping("/archive/{year}/{month}")
    public ModelAndView getArchive(@PathVariable int year,
            @PathVariable int month) throws DataAccessException {
        return getArchive(year, month, 1);
    }

    @RequestMapping("/archive/{year}/{month}/page/{page}")
    public ModelAndView getArchive(@PathVariable int year,
            @PathVariable int month, @PathVariable long page)
            throws DataAccessException {
        long offset = (page >= 1) ? (page - 1) * postNumberPerPage : 0;
        long limit = postNumberPerPage;
        List<BlogPost> postList = blogPostDataAccess.getPostsByMonthlyArchive(
                year, month, offset, limit);
        ModelAndView home = new ModelAndView("home");
        home.addObject("postList", postList);
        home.addAllObjects(frameBuilder.getFrameObjects());
        return home;
    }
}
