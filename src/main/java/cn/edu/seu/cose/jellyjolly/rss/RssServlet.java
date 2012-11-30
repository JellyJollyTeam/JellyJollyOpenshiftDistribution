/*
 * Copyright (C) 2012 Colleage of Software Engineering, Southeast University
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
package cn.edu.seu.cose.jellyjolly.rss;

import cn.edu.seu.cose.jellyjolly.dao.AdminUserDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.BlogInfoDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.BlogPostDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.DataAccessException;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedOutput;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
public class RssServlet extends HttpServlet {

    private static final String CONTENT_TYPE = "application/xml; charset=UTF-8";
    private static final String PARAM_FEED_TYPE = "type";
    private static final Logger logger = Logger.getLogger(
            RssServlet.class.getName());
    private AdminUserDataAccess adminUserDataAccess;
    private BlogInfoDataAccess blogInfoDataAccess;
    private BlogPostDataAccess blogPostDataAccess;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext ctx = config.getServletContext();
        adminUserDataAccess = (AdminUserDataAccess) ctx.getAttribute(
                "cn.edu.seu.cose.jellyjolly.adminUserDataAccess");
        blogInfoDataAccess = (BlogInfoDataAccess) ctx.getAttribute(
                "cn.edu.seu.cose.jellyjolly.blogInfoDataAccess");
        blogPostDataAccess = (BlogPostDataAccess) ctx.getAttribute(
                "cn.edu.seu.cose.jellyjolly.blogPostDataAccess");
    }

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException {
        String feedType = request.getParameter(PARAM_FEED_TYPE);
        response.setContentType(CONTENT_TYPE);
        PrintWriter out = response.getWriter();
        try {
            RssBuilder rssBuilder = new RssBuilder(adminUserDataAccess,
                    blogInfoDataAccess, blogPostDataAccess);
            if (feedType == null) {
                rssBuilder.buildSyndFeed();
            } else {
                rssBuilder.buildSyndFeed(feedType);
            }
            SyndFeed feed = rssBuilder.getSyndFeed();
            SyndFeedOutput output = new SyndFeedOutput();
            output.output(feed, out);
        } catch (FeedException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            response.sendError(500, ex.getMessage());
        } catch (DataAccessException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            response.sendError(500, ex.getMessage());
        } finally {
            out.close();
        }
    }
}
