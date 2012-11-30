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
package cn.edu.seu.cose.jellyjolly.controller.servlet;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
public interface ICommentOperation {

    public static final String PARAM_OPERATION = "op";
    public static final String PARAM_COMMENT_ID = "commentid";
    public static final String PARAM_PARENT_COMMENT_ID = "parentid";
    public static final String PARAM_BLOG_POST_ID = "postid";
    public static final String PARAM_AUTHOR_NAME = "authorname";
    public static final String PARAM_AUTHOR_EMAIL = "email";
    public static final String PARAM_AUTHOR_HOMEPAGE = "homepage";
    public static final String PARAM_CONTENT = "content";
    public static final String OP_ADD = "add";
    public static final String OP_DEL = "del";
    public static final String POST_URL = "./post.jsp?postid=";
}
