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
import cn.edu.seu.cose.jellyjolly.dao.BlogPostDataAccess.BlogPostOrderStrategy;
import cn.edu.seu.cose.jellyjolly.dao.DataAccessException;
import cn.edu.seu.cose.jellyjolly.dao.DataAccessFactory;
import cn.edu.seu.cose.jellyjolly.dto.AdminUser;
import cn.edu.seu.cose.jellyjolly.dto.BlogInfo;
import cn.edu.seu.cose.jellyjolly.dto.BlogPost;
import com.sun.syndication.feed.synd.SyndCategory;
import com.sun.syndication.feed.synd.SyndCategoryImpl;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.feed.synd.SyndPerson;
import com.sun.syndication.feed.synd.SyndPersonImpl;
import com.sun.syndication.io.FeedException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
public class RssBuilder {

    private static final String DEFAULT_FEED_TYPE = "rss_2.0";
    private static final int SUMMARY_LENGTH = 300;
    private AdminUserDataAccess adminUserDataAccess;
    private BlogInfoDataAccess blogInfoDataAccess;
    private BlogPostDataAccess blogPostDataAccess;
    private SyndFeed feedProduct;

    private static SyndEntry parsePost(BlogPost post) {
        SyndEntry entry = new SyndEntryImpl();

        // author
        String author = post.getAuthor().getDisplayName();
        entry.setAuthor(author);

        // category
        String category = post.getCategory().getName();
        List<SyndCategory> categories = new LinkedList<SyndCategory>();
        SyndCategory syndCategory = new SyndCategoryImpl();
        syndCategory.setName(category);
        categories.add(syndCategory);
        entry.setCategories(categories);

        // discription
        SyndContent content = new SyndContentImpl();
        String postContent = post.getContent();
        String text = (postContent.length() < SUMMARY_LENGTH)
                ? postContent
                : postContent.substring(0, SUMMARY_LENGTH);
        content.setType("text/html");
        content.setValue(text);
        entry.setDescription(content);

        // date
        entry.setPublishedDate(post.getDate());
        return entry;
    }

    public RssBuilder(AdminUserDataAccess adminUserDataAccess,
            BlogInfoDataAccess blogInfoDataAccess,
            BlogPostDataAccess blogPostDataAccess) {
        this.adminUserDataAccess = adminUserDataAccess;
        this.blogInfoDataAccess = blogInfoDataAccess;
        this.blogPostDataAccess = blogPostDataAccess;
    }

    public SyndFeed getSyndFeed() {
        return feedProduct;
    }

    public void buildSyndFeed() throws FeedException, DataAccessException {
        buildFeedInfo();
        buildEntries();
    }

    public void buildSyndFeed(String feedType) throws FeedException,
            DataAccessException {
        buildFeedInfo(feedType);
        buildEntries();
    }

    private void buildFeedInfo() throws DataAccessException {
        buildFeedInfo(DEFAULT_FEED_TYPE);
    }

    private void buildFeedInfo(String feedType) throws DataAccessException {
        feedProduct = new SyndFeedImpl();
        feedProduct.setFeedType(feedType);

        BlogInfo blogInfo = blogInfoDataAccess.getBlogInfoInstance();
        // description
        feedProduct.setDescription(blogInfo.getBlogTitle());
        // title
        feedProduct.setTitle(blogInfo.getBlogTitle());
        // link
        feedProduct.setLink(blogInfo.getBlogUrl());

        // url
        String link = blogInfo.getBlogUrl();
        feedProduct.setLink((link == null) ? "" : link);

        // pubDate
        Date pubDate = blogPostDataAccess.getLatestPubDate();
        feedProduct.setPublishedDate(pubDate);

        // authors
        List<AdminUser> usrLst = adminUserDataAccess.getAllUsers();
        List<SyndPerson> nameList = new LinkedList<SyndPerson>();
        for (AdminUser usr : usrLst) {
            String authorName = usr.getDisplayName();
            String email = usr.getEmail();
            SyndPerson person = new SyndPersonImpl();
            person.setName(authorName);
            person.setEmail(email);
            nameList.add(person);
        }
        feedProduct.setAuthors(nameList);
    }

    private void buildEntries() throws DataAccessException {
        List<SyndEntry> entries = new LinkedList<SyndEntry>();

        List<BlogPost> postList = blogPostDataAccess.getPosts(0, 100,
                BlogPostOrderStrategy.ORDERED_BY_DATE_DESC);
        for (BlogPost post : postList) {
            SyndEntry entry = parsePost(post);
            entries.add(entry);
        }

        feedProduct.setEntries(entries);
    }
}
