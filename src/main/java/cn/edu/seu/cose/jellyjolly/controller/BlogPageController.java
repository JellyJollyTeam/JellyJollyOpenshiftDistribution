/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.seu.cose.jellyjolly.controller;

import cn.edu.seu.cose.jellyjolly.dao.BlogPageDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.DataAccessException;
import cn.edu.seu.cose.jellyjolly.dto.BlogPage;
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
public class BlogPageController {

    private BlogPageDataAccess blogPageDataAccess;
    private FrameBuilder frameBuilder;

    public BlogPageController(BlogPageDataAccess blogPageDataAccess,
            FrameBuilder frameBuilder) {
        this.blogPageDataAccess = blogPageDataAccess;
        this.frameBuilder = frameBuilder;
    }

    @RequestMapping(value = "/page/{pageId}", method = RequestMethod.GET)
    public String getPageById(@PathVariable int pageId, Model model)
            throws DataAccessException {
        BlogPage page = blogPageDataAccess.getPage(pageId);
        model.addAttribute("page", page);
        model.addAllAttributes(frameBuilder.getFrameObjects());
        return "page";
    }

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public String createNewPage(@RequestParam String title,
            @RequestParam String content, @RequestParam String redirect)
            throws DataAccessException {
        int pageId = blogPageDataAccess.addNewPage(title, content);
        return (redirect == null)
                ? "redirect:/page/" + pageId
                : "redirect:" + redirect;
    }

    @RequestMapping(value = "/page/{pageId}", method = RequestMethod.DELETE)
    public String deletePage(@PathVariable int pageId,
            @RequestParam String redirect) throws DataAccessException {
        blogPageDataAccess.deletePage(pageId);
        return "redirect:";
    }

    @RequestMapping(value = "/page/{pageId}", method = RequestMethod.PUT)
    public String changePage(@PathVariable int pageId,
            @RequestParam String title, @RequestParam String content,
            @RequestParam String redirect) throws DataAccessException {
        if (title != null) {
            blogPageDataAccess.changeTitle(pageId, title);
        }
        if (content != null) {
            blogPageDataAccess.changeContent(pageId, content);
        }
        return "redirect:" + redirect;
    }

    @RequestMapping(value = "/page/{pageId}/title",
    method = RequestMethod.PUT)
    public String changePageTitle(@PathVariable int pageId,
            @RequestParam String title, @RequestParam String redirect)
            throws DataAccessException {
        blogPageDataAccess.changeTitle(pageId, title);
        return "redirect:" + redirect;
    }

    @RequestMapping(value = "/page/{pageId}/content",
    method = RequestMethod.PUT)
    public String changePageContent(@PathVariable int pageId,
            @RequestParam String content, @RequestParam String redirect)
            throws DataAccessException {
        blogPageDataAccess.changeContent(pageId, content);
        return "redirect:" + redirect;
    }
}
