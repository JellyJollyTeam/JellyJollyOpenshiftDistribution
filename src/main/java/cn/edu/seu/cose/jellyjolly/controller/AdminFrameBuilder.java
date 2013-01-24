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
import cn.edu.seu.cose.jellyjolly.dao.DataAccessException;
import cn.edu.seu.cose.jellyjolly.model.BlogInfo;
import cn.edu.seu.cose.jellyjolly.model.BlogPageBar;
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

    public Map<String, ?> getFrameObjects() throws DataAccessException {
        BlogInfo blogInfo = blogInfoDataAccess.getBlogInfoInstance();
        List<BlogPageBar> pageList = blogPageDataAccess.getPageBarList();
        Map<String, Object> frameObjs = new HashMap<String, Object>();
        frameObjs.put("blogInfo", blogInfo);
        frameObjs.put("pageList", pageList);
        return frameObjs;
    }
}
