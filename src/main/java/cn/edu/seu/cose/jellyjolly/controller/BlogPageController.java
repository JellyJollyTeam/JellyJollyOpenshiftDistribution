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

import cn.edu.seu.cose.jellyjolly.dao.BlogPageDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.DataAccessException;
import cn.edu.seu.cose.jellyjolly.model.BlogPage;
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
public class BlogPageController {

    private BlogPageDataAccess blogPageDataAccess;
    private FrameBuilder frameBuilder;

    public BlogPageController(BlogPageDataAccess blogPageDataAccess,
            FrameBuilder frameBuilder) {
        this.blogPageDataAccess = blogPageDataAccess;
        this.frameBuilder = frameBuilder;
    }

    @RequestMapping(value = "/page/{pageId}", method = RequestMethod.GET)
    public String getPageById(@PathVariable int pageId, Model model,
            HttpServletRequest request) throws DataAccessException {
        BlogPage page = blogPageDataAccess.getPage(pageId);
        model.addAttribute("blogpage", page);
        model.addAllAttributes(frameBuilder.getFrameObjects(request));
        return "page";
    }

    @RequestMapping(value = "/admin/page", method = RequestMethod.POST)
    public String createNewPage(@RequestParam String title,
            @RequestParam String content) throws DataAccessException {
        int pageId = blogPageDataAccess.addNewPage(title, content);
        return "redirect:/page/" + pageId;
    }

    @RequestMapping(value = "/admin/page",
    method = RequestMethod.DELETE)
    public String deletePage(@RequestParam List<Integer> pageIds,
            @RequestParam String redirect) throws DataAccessException {
        for (int pageId : pageIds) {
            blogPageDataAccess.deletePage(pageId);
        }
        return "redirect:" + redirect;
    }

    @RequestMapping(value = "/admin/page/{pageId}",
    method = RequestMethod.DELETE)
    public String deletePage(@PathVariable int pageId,
            @RequestParam String redirect) throws DataAccessException {
        blogPageDataAccess.deletePage(pageId);
        return "redirect:";
    }

    @RequestMapping(value = "/admin/page/{pageId}", method = RequestMethod.PUT)
    public String changePage(@PathVariable int pageId,
            @RequestParam String title, @RequestParam String content)
            throws DataAccessException {
        if (title != null) {
            blogPageDataAccess.changeTitle(pageId, title);
        }
        if (content != null) {
            blogPageDataAccess.changeContent(pageId, content);
        }
        return "redirect:/page/" + pageId;
    }

    @RequestMapping(value = "/admin/page/{pageId}/title",
    method = RequestMethod.PUT)
    public String changePageTitle(@PathVariable int pageId,
            @RequestParam String title, @RequestParam String redirect)
            throws DataAccessException {
        blogPageDataAccess.changeTitle(pageId, title);
        return "redirect:" + redirect;
    }

    @RequestMapping(value = "/admin/page/{pageId}/content",
    method = RequestMethod.PUT)
    public String changePageContent(@PathVariable int pageId,
            @RequestParam String content, @RequestParam String redirect)
            throws DataAccessException {
        blogPageDataAccess.changeContent(pageId, content);
        return "redirect:" + redirect;
    }
}
