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
package cn.edu.seu.cose.jellyjolly.rest.resource;

import cn.edu.seu.cose.jellyjolly.dao.BlogInfoDataAccess;
import cn.edu.seu.cose.jellyjolly.dto.BlogInfo;
import cn.edu.seu.cose.jellyjolly.rest.dto.BlogInfoInstance;
import cn.edu.seu.cose.jellyjolly.rest.dto.Property;
import cn.edu.seu.cose.jellyjolly.rest.dto.Values;
import cn.edu.seu.cose.jellyjolly.rest.dto.adapter.BlogInfoAdapter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
public class BlogInfoResource extends ServerResource {

    private static final Logger logger = Logger.getLogger(
            BlogInfoResource.class.getName());
    private BlogInfoDataAccess blogInfoDao;

    public BlogInfoResource() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext(
                "cn/edu/seu/cose/jellyjolly/dist/applicationContext.xml");
        blogInfoDao = (BlogInfoDataAccess) ctx.getBean("blogInfoDataAccess");
    }

    @Get("xml")
    public Representation getBlogInfo() {
        try {
            BlogInfo blogInfo = blogInfoDao.getBlogInfoInstance();
            BlogInfoAdapter blogInfoAdptr = new BlogInfoAdapter();
            BlogInfoInstance xmlBlogInfo = blogInfoAdptr.adapt(blogInfo);
            return ResourceUtils.getRepresentationOfXmlObject(xmlBlogInfo);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            return ResourceUtils.getFailureRepresentation(ex);
        }
    }

    @Put("xml:xml")
    public Representation editBlogInfo(Representation newBlogInfoRepr) {
        try {
            DomRepresentation xmlBlogInfo = new DomRepresentation(
                    newBlogInfoRepr);
            DomRepresentationReader domReader = new DomRepresentationReader();
            BlogInfoInstance newBlogInfo = (BlogInfoInstance) domReader
                    .getXmlObject( xmlBlogInfo, BlogInfoInstance.class);
            String title = newBlogInfo.getBlogTitle();
            String subtitle = newBlogInfo.getBlogSubTitle();
            String blogUrl = newBlogInfo.getBlogUrl();
            List<Property> propertyList = newBlogInfo.getProperties();
            if (title != null) {
                blogInfoDao.setBlogTitle(title);
            }
            if (subtitle != null) {
                blogInfoDao.setBlogSubTitle(subtitle);
            }
            if (blogUrl != null) {
                blogInfoDao.setBlogUrl(blogUrl);
            }
            if (propertyList != null) {
                for (Property property : propertyList) {
                    String key = property.getKey();
                    Values values = property.getValues();
                    String[] valueArr = values.getValue().toArray(
                            new String[0]);
                    blogInfoDao.setBlogInfoMeta(key, valueArr);
                }
            }
            return ResourceUtils.getUpdateSuccessRepresentation();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            return ResourceUtils.getFailureRepresentation(ex);
        }
    }
}
