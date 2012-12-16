/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.seu.cose.jellyjolly.controller;

import cn.edu.seu.cose.jellyjolly.dao.BlogInfoDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.DataAccessException;
import cn.edu.seu.cose.jellyjolly.dto.BlogInfo;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
@Controller
public class AdminFrameBuilder {

    private BlogInfoDataAccess blogInfoDataAccess;

    public AdminFrameBuilder(BlogInfoDataAccess blogInfoDataAccess) {
        this.blogInfoDataAccess = blogInfoDataAccess;
    }

    public Map<String, ?> getFrameObjects(HttpServletRequest request)
            throws DataAccessException {
        BlogInfo blogInfo = blogInfoDataAccess.getBlogInfoInstance();
        Map<String, Object> frameObjs = new HashMap<String, Object>();
        frameObjs.put("blogInfo", blogInfo);
        return null;
    }
}
