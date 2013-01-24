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

import cn.edu.seu.cose.jellyjolly.dao.BlogInfoDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.BlogPageDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.BlogPostDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.CategoryDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.DataAccessException;
import cn.edu.seu.cose.jellyjolly.dao.LinkDataAccess;
import cn.edu.seu.cose.jellyjolly.model.BlogInfo;
import cn.edu.seu.cose.jellyjolly.model.BlogPageBar;
import cn.edu.seu.cose.jellyjolly.model.Category;
import cn.edu.seu.cose.jellyjolly.model.Link;
import cn.edu.seu.cose.jellyjolly.model.MonthArchive;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
public class FrameBuilder {

    private BlogInfoDataAccess blogInfoDataAccess;
    private BlogPageDataAccess blogPageDataAccess;
    private BlogPostDataAccess blogPostDataAccess;
    private CategoryDataAccess categoryDataAccess;
    private LinkDataAccess linkDataAccess;

    public FrameBuilder(BlogInfoDataAccess blogInfoDataAccess,
            BlogPageDataAccess blogPageDataAccess,
            BlogPostDataAccess blogPostDataAccess,
            CategoryDataAccess categoryDataAccess,
            LinkDataAccess linkDataAccess) {
        this.blogInfoDataAccess = blogInfoDataAccess;
        this.blogPageDataAccess = blogPageDataAccess;
        this.blogPostDataAccess = blogPostDataAccess;
        this.categoryDataAccess = categoryDataAccess;
        this.linkDataAccess = linkDataAccess;
    }

    public Map<String, ?> getFrameObjects(HttpServletRequest request)
            throws DataAccessException {
        BlogInfo blogInfo = blogInfoDataAccess.getBlogInfoInstance();
        List<BlogPageBar> pageList = blogPageDataAccess.getPageBarList();
        List<Category> categoryList = categoryDataAccess.getAllCategories();
        List<MonthArchive> archiveList = blogPostDataAccess.getMonthlyArchiveList();
        List<Link> linkList = linkDataAccess.getAllLinks();
        Map<String, Object> frameObjs = new HashMap<String, Object>();
        Object userAuth = request.getSession().getAttribute(
                AdminUserController.SESSION_ATTRI_AUTH);
        frameObjs.put("blogInfo", blogInfo);
        frameObjs.put("pageList", pageList);
        frameObjs.put("categoryList", categoryList);
        frameObjs.put("archivelist", archiveList);
        frameObjs.put("linkList", linkList);
        frameObjs.put("userAuth", userAuth);
        return frameObjs;
    }
}
