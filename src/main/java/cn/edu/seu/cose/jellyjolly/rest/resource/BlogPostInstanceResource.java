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
import cn.edu.seu.cose.jellyjolly.dao.DataAccessException;
import cn.edu.seu.cose.jellyjolly.dto.BlogPost;
import cn.edu.seu.cose.jellyjolly.rest.JellyJollyRouter;
import cn.edu.seu.cose.jellyjolly.rest.dto.BlogPostInstance;
import cn.edu.seu.cose.jellyjolly.rest.dto.BlogPosts;
import cn.edu.seu.cose.jellyjolly.rest.dto.CategoryInstance;
import cn.edu.seu.cose.jellyjolly.rest.dto.Property;
import cn.edu.seu.cose.jellyjolly.rest.dto.adapter.Adapter;
import cn.edu.seu.cose.jellyjolly.rest.dto.adapter.BlogPostCollectionAdapter;
import cn.edu.seu.cose.jellyjolly.util.Utils;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.restlet.data.Form;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
public class BlogPostInstanceResource extends ServerResource {

    private static final String PARAM_OFFSET = "offset";
    private static final String PARAM_LIMIT = "limit";
    private static final String PARAM_CATEGORY = "category";
    private static final String PARAM_AUTHOR = "author";
    private static final String PARAM_YEAR = "year";
    private static final String PARAM_MONTH = "month";
    private static final String PARAM_KEYWORD = "keyword";
    private static final String ILLEGAL_PARAM_MSG =
            "parameter: blog-post-id should be numeric";
    private static final Logger logger = Logger.getLogger(
            BlogPostInstanceResource.class.getName());
    private BlogPostDataAccess blogPostDao;

    private static Representation handleException(Exception ex) {
        logger.log(Level.SEVERE, ex.getMessage(), ex);
        return ResourceUtils.getFailureRepresentation(ex);
    }

    public BlogPostInstanceResource() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext(
                "cn/edu/seu/cose/jellyjolly/dist/applicationContext.xml");
        blogPostDao = (BlogPostDataAccess) ctx.getBean("blogInfoDataAccess");
    }

    @Get("xml")
    public Representation getBlogPost() {
        try {
            Form queryForm = getQuery();
            Map<String, String> valuesMap = queryForm.getValuesMap();
            List<BlogPost> postList = getPostsByValuesMap(valuesMap);
            Adapter<Collection<BlogPost>, BlogPosts> adapter =
                    new BlogPostCollectionAdapter();
            BlogPosts posts = adapter.adapt(postList);
            return ResourceUtils.getRepresentationOfXmlObject(posts);
        } catch (Exception ex) {
            return handleException(ex);
        }
    }

    @Post("xml:xml")
    public Representation createBlogPost(Representation newPost) {
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
            return handleException(ex);
        }
    }

    @Delete("xml:xml")
    public Representation deleteBlogPost(Representation none) {
        try {
            Map<String, Object> requestAttributes = getRequestAttributes();
            String blogPostIdParam = (String) requestAttributes
                    .get(JellyJollyRouter.PARAM_BLOG_POST_ID);
            if (!Utils.isNumeric(blogPostIdParam)) {
                throw new IllegalArgumentException(ILLEGAL_PARAM_MSG);
            }
            long blogPostId = Long.valueOf(blogPostIdParam);
            blogPostDao.deletePost(blogPostId);
            return ResourceUtils.getUpdateSuccessRepresentation();
        } catch (Exception ex) {
            return handleException(ex);
        }
    }

    private List<BlogPost> getPostsByValuesMap(Map<String, String> valuesMap)
            throws DataAccessException {
        try {
            // offset and limit cannot be null or non-numeric
            if (!valuesMap.containsKey(PARAM_OFFSET)
                    || !valuesMap.containsKey(PARAM_LIMIT)
                    || !Utils.isNumeric(valuesMap.get(PARAM_OFFSET))
                    || !Utils.isNumeric(valuesMap.get(PARAM_LIMIT))) {
                return Collections.EMPTY_LIST;
            }
            String offsetParam = valuesMap.get(PARAM_OFFSET);
            String limitParam = valuesMap.get(PARAM_LIMIT);
            long offset = Long.valueOf(offsetParam);
            long limit = Long.valueOf(limitParam);

            // get posts by category
            if (valuesMap.containsKey(PARAM_CATEGORY)) {
                String categoryParam = valuesMap.get(PARAM_CATEGORY);
                if (!Utils.isNumeric(categoryParam)) {
                    return Collections.EMPTY_LIST;
                }
                int categoryId = Integer.valueOf(categoryParam);
                return blogPostDao.getPostsByCategoryId(categoryId, offset, limit);
            }

            // get posts by author
            if (valuesMap.containsKey(PARAM_AUTHOR)) {
                String authorParam = valuesMap.get(PARAM_AUTHOR);
                if (!Utils.isNumeric(authorParam)) {
                    return Collections.EMPTY_LIST;
                }
                long userId = Long.valueOf(authorParam);
                return blogPostDao.getPostsByUserId(userId, offset, limit);
            }

            // get posts by archive
            if (valuesMap.containsKey(PARAM_YEAR)
                    && valuesMap.containsKey(PARAM_YEAR)) {
                String yearParam = valuesMap.get(PARAM_YEAR);
                String monthParam = valuesMap.get(PARAM_MONTH);
                if (!Utils.isNumeric(yearParam) || !Utils.isNumeric(monthParam)) {
                    return Collections.EMPTY_LIST;
                }
                int year = Integer.valueOf(yearParam);
                int month = Integer.valueOf(monthParam);
                return blogPostDao.getPostsByMonthlyArchive(year, month, offset, limit);
            }

            // get posts by keyword
            if (valuesMap.containsKey(PARAM_KEYWORD)) {
                String keyword = valuesMap.get(PARAM_KEYWORD);
                return blogPostDao.getPostsByKeyword(keyword, offset, limit);
            }

            // get posts just by offset and limit
            return blogPostDao.getPosts(offset, limit);
        } catch (NumberFormatException ex) {
            return Collections.EMPTY_LIST;
        }
    }
}
