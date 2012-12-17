/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.seu.cose.jellyjolly.controller;

import cn.edu.seu.cose.jellyjolly.dao.BlogInfoDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.BlogPageDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.DataAccessException;
import cn.edu.seu.cose.jellyjolly.dto.BlogInfo;
import cn.edu.seu.cose.jellyjolly.dto.BlogPageBar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
public class AdminFrameBuilder {

    private BlogInfoDataAccess blogInfoDataAccess;
    private BlogPageDataAccess blogPageDataAccess;

    public AdminFrameBuilder(BlogPageDataAccess blogPageDataAccess,
            BlogInfoDataAccess blogInfoDataAccess) {
        this.blogInfoDataAccess = blogInfoDataAccess;
        this.blogPageDataAccess = blogPageDataAccess;
    }

    public Map<String, ?> getFrameObjects(HttpServletRequest request)
            throws DataAccessException {
        BlogInfo blogInfo = blogInfoDataAccess.getBlogInfoInstance();
        List<BlogPageBar> pageList = blogPageDataAccess.getPageBarList();
        Map<String, Object> frameObjs = new HashMap<String, Object>();
        frameObjs.put("blogInfo", blogInfo);
        frameObjs.put("pageList", pageList);
        return null;
    }
}
