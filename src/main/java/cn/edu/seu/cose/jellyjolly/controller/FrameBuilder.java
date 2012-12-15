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
import cn.edu.seu.cose.jellyjolly.dao.LinkDataAccess;
import cn.edu.seu.cose.jellyjolly.dto.BlogInfo;
import cn.edu.seu.cose.jellyjolly.dto.BlogPageBar;
import cn.edu.seu.cose.jellyjolly.dto.Category;
import cn.edu.seu.cose.jellyjolly.dto.Link;
import cn.edu.seu.cose.jellyjolly.dto.MonthArchive;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<String, ?> getFrameObjects() throws DataAccessException {
        BlogInfo blogInfo = blogInfoDataAccess.getBlogInfoInstance();
        List<BlogPageBar> pageList = blogPageDataAccess.getPageBarList();
        List<Category> categoryList = categoryDataAccess.getAllCategories();
        List<MonthArchive> archiveList = blogPostDataAccess.getMonthlyArchiveList();
        List<Link> linkList = linkDataAccess.getAllLinks();
        Map<String, Object> frameObjs = new HashMap<String, Object>();
        frameObjs.put("blogInfo", blogInfo);
        frameObjs.put("pageList", pageList);
        frameObjs.put("categoryList", categoryList);
        frameObjs.put("archivelist", archiveList);
        frameObjs.put("linkList", linkList);
        return frameObjs;
    }
}
