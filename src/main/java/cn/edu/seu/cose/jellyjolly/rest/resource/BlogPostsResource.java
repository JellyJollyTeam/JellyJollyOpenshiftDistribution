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

import cn.edu.seu.cose.jellyjolly.dao.BlogPostDataAccess;
import cn.edu.seu.cose.jellyjolly.rest.dto.BlogPostInstance;
import cn.edu.seu.cose.jellyjolly.rest.dto.BlogPosts;
import cn.edu.seu.cose.jellyjolly.rest.dto.CategoryInstance;
import cn.edu.seu.cose.jellyjolly.rest.dto.Property;
import cn.edu.seu.cose.jellyjolly.rest.dto.adapter.BlogPostCollectionAdapter;
import cn.edu.seu.cose.jellyjolly.util.Utils;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
public class BlogPostsResource extends ServerResource {

    private static final String PARAM_OFFSET = "offset";
    private static final String PARAM_LIMIT = "limit";
    private static final Logger logger = Logger.getLogger(
            BlogPostsResource.class.getName());
    private BlogPostDataAccess blogPostDao;

    public BlogPostsResource() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext(
                "cn/edu/seu/cose/jellyjolly/dist/applicationContext.xml");
        blogPostDao = (BlogPostDataAccess) ctx.getBean("blogPostDataAccess");
    }

    @Get("xml")
    public Representation getPosts() {
        try {
            String offsetParam = getQuery().getFirstValue(PARAM_OFFSET);
            String limitParam = getQuery().getFirstValue(PARAM_LIMIT);
            long offset = Utils.isNumeric(offsetParam)
                    ? Long.valueOf(offsetParam)
                    : 0;
            long limit = Utils.isNumeric(limitParam)
                    ? Long.valueOf(limitParam)
                    : 0;
            List<cn.edu.seu.cose.jellyjolly.dto.BlogPost> postList =
                    blogPostDao.getPosts(offset, limit);
            BlogPostCollectionAdapter adapter = new BlogPostCollectionAdapter();
            BlogPosts postsTarget = adapter.adapt(postList);
            return ResourceUtils.getRepresentationOfXmlObject(postsTarget);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            return ResourceUtils.getFailureRepresentation(ex);
        }
    }

    @Post("xml")
    public Representation createPost(Representation newPost) {
        try {
            DomRepresentation newPostDom = new DomRepresentation(newPost);
            DomRepresentationReader reader = new DomRepresentationReader();
            BlogPostInstance newPostInstance =
                    (BlogPostInstance) reader.getXmlObject(newPostDom,
                    BlogPostInstance.class);
            CategoryInstance category = newPostInstance.getCategory();
            long userId = newPostInstance.getAuthor().getUserId();
            int categoryId = category.getCategoryId();
            Date currentDate = new Date();
            String title = newPostInstance.getTitle();
            String content = newPostInstance.getContent();
            List<Property> postProperties = newPostInstance.getProperties();
            long postId = blogPostDao.createNewPost(userId, categoryId,
                    currentDate, title, content);
            for (Property p : postProperties) {
                String key = p.getKey();
                List<String> values = p.getValues().getValue();
                String[] valueArr = values.toArray(new String[0]);
                blogPostDao.setProperty(postId, key, valueArr);
            }
            return ResourceUtils.getUpdateSuccessRepresentation();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            return ResourceUtils.getFailureRepresentation(ex);
        }
    }
}
