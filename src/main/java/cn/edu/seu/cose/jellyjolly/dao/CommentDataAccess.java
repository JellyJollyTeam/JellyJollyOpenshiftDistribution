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
package cn.edu.seu.cose.jellyjolly.dao;

import cn.edu.seu.cose.jellyjolly.dto.Comment;
import java.util.Date;
import java.util.List;

/**
 *
 * @author rAy
 */
public interface CommentDataAccess {

    public static enum CommentOrderStrategy {

        ORDERED_BY_DATE_ASC, ORDERED_BY_DATE_DESC
    }

    Comment getCommentById(long commentId) throws DataAccessException;

    List<Comment> getComments() throws DataAccessException;

    List<Comment> getComments(long offset, long limit)
            throws DataAccessException;

    List<Comment> getRecentComments(long number) throws DataAccessException;

    List<Comment> getCommentsByParentCommentId(
            long parentCommentId) throws DataAccessException;

    List<Comment> getCommentsByParentCommentId(
            long parentCommentId, CommentOrderStrategy strategy)
            throws DataAccessException;

    List<Comment> getCommentsByPostId(long postId)
            throws DataAccessException;

    List<Comment> getCommentsByPostId(long postId,
            CommentOrderStrategy strategy) throws DataAccessException;

    long getCommentNumber() throws DataAccessException;

    void updateComment(long commentId, String content)
            throws DataAccessException;

    long addNewComment(Comment comment) throws DataAccessException;

    long addNewComment(long postId, long userId, String content, Date date)
            throws DataAccessException;

    long addNewComment(long postId, long userId, String content,
            long parentCommentId, Date date) throws DataAccessException;

    long addNewComment(long postId, String authorName, String authorEmail,
            String authorHomePageURL, String content, Date date)
            throws DataAccessException;

    long addNewComment(long postId, String authorName, String authorEmail,
            String authorHomePageURL, String content,
            long parentCommentId, Date date) throws DataAccessException;

    void deleteCommentById(long commentId) throws DataAccessException;

    void deleteCommentsByPostId(long postId)
            throws DataAccessException;
}
