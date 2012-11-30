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

package cn.edu.seu.cose.jellyjolly.rest;

import cn.edu.seu.cose.jellyjolly.rest.resource.BlogInfoResource;
import cn.edu.seu.cose.jellyjolly.rest.resource.BlogPostInstanceResource;
import cn.edu.seu.cose.jellyjolly.rest.resource.BlogPostsResource;
import cn.edu.seu.cose.jellyjolly.rest.resource.CategoriesResource;
import cn.edu.seu.cose.jellyjolly.rest.resource.CategoryInstanceResource;
import cn.edu.seu.cose.jellyjolly.rest.resource.CommentInstanceResource;
import cn.edu.seu.cose.jellyjolly.rest.resource.CommentsResource;
import cn.edu.seu.cose.jellyjolly.rest.resource.LinkInstanceResource;
import cn.edu.seu.cose.jellyjolly.rest.resource.LinksResource;
import org.restlet.Context;
import org.restlet.routing.Router;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
public class JellyJollyRouter extends Router {
    
    public static final String PARAM_BLOG_POST_ID = "blog-post-id";
    
    public static final String PARAM_CATEGORY_ID = "category-id";
    
    public static final String PARAM_COMMENT_ID = "comment-id";
    
    public static final String PARAM_LINK_ID = "link-id";
    
    public static final String PARAM_ADMIN_USER_ID = "user-id";
    
    private static final String PATH_BLOG_INFO = "/bloginfo";
    
    private static final String PATH_BLOG_POSTS = "/post";
    
    private static final String PATH_BLOG_POST_INSTANCE =
            PATH_BLOG_POSTS + "/{" + PARAM_BLOG_POST_ID + "}";
    
    private static final String PATH_CATEGORIES = "/category";
    
    private static final String PATH_CATEGORY_INSTANCE =
            PATH_CATEGORIES + "/{" + PARAM_CATEGORY_ID + "}";
    
    private static final String PATH_COMMENTS = "/comment";
    
    private static final String PATH_COMMENT_INSTANCE =
            PATH_COMMENTS + "/{" + PARAM_COMMENT_ID + "}";
    
    private static final String PATH_LINKS = "/link";
    
    private static final String PATH_LINK_INSTANCE =
            PATH_LINKS + "/{" + PARAM_LINK_ID + "}";
    
    private static final String PATH_ADMIN_USERS = "/adminuser";
    
    private static final String PATH_ADMIN_USER_INSTANCE =
            PATH_ADMIN_USERS + "/{" + PARAM_ADMIN_USER_ID + "}";
    
    public JellyJollyRouter(Context appContext) {
        super(appContext);
        init();
    }
    
    private void init() {
        attach(PATH_BLOG_INFO, BlogInfoResource.class);
        attach(PATH_BLOG_POSTS, BlogPostsResource.class);
        attach(PATH_BLOG_POST_INSTANCE, BlogPostInstanceResource.class);
        attach(PATH_CATEGORIES, CategoriesResource.class);
        attach(PATH_CATEGORY_INSTANCE, CategoryInstanceResource.class);
        attach(PATH_COMMENTS, CommentsResource.class);
        attach(PATH_COMMENT_INSTANCE, CommentInstanceResource.class);
        attach(PATH_LINKS, LinksResource.class);
        attach(PATH_LINK_INSTANCE, LinkInstanceResource.class);
    }

}
