/*
 * Copyright (C) 2013 College of Software Engineering, Southeast University
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

import cn.edu.seu.cose.jellyjolly.dao.AdminUserDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.BlogInfoDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.BlogPostDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.DataAccessException;
import cn.edu.seu.cose.jellyjolly.rss.RssBuilder;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedOutput;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Ray
 */
@Controller
public class RssController {

    private AdminUserDataAccess adminUserDataAccess;
    private BlogInfoDataAccess blogInfoDataAccess;
    private BlogPostDataAccess blogPostDataAccess;
    
    public RssController(AdminUserDataAccess adminUserDataAccess,
            BlogInfoDataAccess blogInfoDataAccess,
            BlogPostDataAccess blogPostDataAccess) {
        this.adminUserDataAccess = adminUserDataAccess;
        this.blogInfoDataAccess = blogInfoDataAccess;
        this.blogPostDataAccess = blogPostDataAccess;
    }
    
    private RssBuilder getRssBuilder() {
        RssBuilder rssBuilder = new RssBuilder(adminUserDataAccess,
                    blogInfoDataAccess, blogPostDataAccess);
        return rssBuilder;
    }
    
    @RequestMapping(value = "/rss", method = RequestMethod.GET, params = "type")
    public void getFeed(@RequestParam String type,
            HttpServletResponse response) throws FeedException,
            DataAccessException,
            IOException {
        RssBuilder rssBuilder = getRssBuilder();
        rssBuilder.buildSyndFeed(type);
        buildRssResponse(rssBuilder, response);
    }
    
    @RequestMapping(value = "/rss", method = RequestMethod.GET,
            params = "!type")
    public void getFeed(HttpServletResponse response) throws FeedException,
            DataAccessException, IOException {
        RssBuilder rssBuilder = getRssBuilder();
        rssBuilder.buildSyndFeed();
        buildRssResponse(rssBuilder, response);
    }
    
    private void buildRssResponse(RssBuilder rssBuilder,
            HttpServletResponse response) throws IOException, FeedException {
        PrintWriter out = response.getWriter();
        SyndFeed feed = rssBuilder.getSyndFeed();
        SyndFeedOutput output = new SyndFeedOutput();
        output.output(feed, out);
    }
}
