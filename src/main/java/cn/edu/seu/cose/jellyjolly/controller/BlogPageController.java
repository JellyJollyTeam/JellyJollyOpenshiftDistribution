/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.seu.cose.jellyjolly.controller;

import cn.edu.seu.cose.jellyjolly.dao.BlogPageDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.DataAccessException;
import cn.edu.seu.cose.jellyjolly.dto.BlogPage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
@Controller
public class BlogPageController {

    private BlogPageDataAccess blogPageDataAccess;
    private FrameBuilder frameBuilder;

    public BlogPageController(BlogPageDataAccess blogPageDataAccess,
            FrameBuilder frameBuilder) {
        this.blogPageDataAccess = blogPageDataAccess;
        this.frameBuilder = frameBuilder;
    }

    @RequestMapping("/page/{pageId}")
    public ModelAndView getPageById(@PathVariable int pageId)
            throws DataAccessException {
        BlogPage page = blogPageDataAccess.getPage(pageId);
        ModelAndView pageMnV = new ModelAndView("page");
        pageMnV.addObject("page", page);
        pageMnV.addAllObjects(frameBuilder.getFrameObjects());
        return pageMnV;
    }
}
