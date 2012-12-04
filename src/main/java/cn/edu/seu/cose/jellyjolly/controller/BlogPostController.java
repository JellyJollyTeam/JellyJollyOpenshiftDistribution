/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.seu.cose.jellyjolly.controller;

import cn.edu.seu.cose.jellyjolly.dao.BlogInfoDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.BlogPageDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.BlogPostDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.CategoryDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.DataAccessException;
import cn.edu.seu.cose.jellyjolly.dto.BlogInfo;
import cn.edu.seu.cose.jellyjolly.dto.BlogPageBar;
import cn.edu.seu.cose.jellyjolly.dto.BlogPost;
import cn.edu.seu.cose.jellyjolly.dto.Category;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
@Controller
public class BlogPostController {

    private BlogPostDataAccess blogPostDataAccess;
    private FrameBuilder frameBuilder;

    public BlogPostController(BlogPostDataAccess blogPostDataAccess,
            FrameBuilder frameBuilder) {
        this.blogPostDataAccess = blogPostDataAccess;
        this.frameBuilder = frameBuilder;
    }

    @RequestMapping("/post/{postId}")
    public ModelAndView getPostById(@PathVariable long postId)
            throws DataAccessException {
        BlogPost post = blogPostDataAccess.getPostById(postId);
        ModelAndView postMnV = new ModelAndView("post");
        postMnV.addObject("post", post);
        postMnV.addAllObjects(frameBuilder.getFrameObjects());
        return postMnV;
    }
}
