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

import cn.edu.seu.cose.jellyjolly.dto.BlogPost;
import cn.edu.seu.cose.jellyjolly.dto.MonthArchive;
import java.util.Date;
import java.util.List;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
public interface BlogPostDataAccess {

    public static enum BlogPostOrderStrategy {

        ORDERED_BY_DATE_ASC, ORDERED_BY_DATE_DESC
    }

    Date getLatestPubDate() throws DataAccessException;

    Date getLatestPubDate(long userId) throws DataAccessException;

    BlogPost getPostById(long postId) throws DataAccessException;

    long getPostNumber() throws DataAccessException;

    long getPostNumber(int categoryId) throws DataAccessException;

    long getPostNumber(int year, int month) throws DataAccessException;

    long getPostNumber(String keyword) throws DataAccessException;

    List<BlogPost> getPosts(long offset, long limit)
            throws DataAccessException;

    List<BlogPost> getPosts(long offset, long limit,
            BlogPostOrderStrategy strategy) throws DataAccessException;

    List<BlogPost> getPosts(Date from, Date to, long offset, long limit)
            throws DataAccessException;

    List<BlogPost> getPosts(Date from, Date to, long offset, long limit,
            BlogPostOrderStrategy strategy) throws DataAccessException;

    List<BlogPost> getPostsByKeyword(String keyword, long offset, long limit)
            throws DataAccessException;

    List<BlogPost> getPostsByKeyword(String keyword, long offset, long limit,
            BlogPostOrderStrategy strategy) throws DataAccessException;

    List<BlogPost> getPostsByUserId(long userId, long offset, long limit)
            throws DataAccessException;

    List<BlogPost> getPostsByUserId(long userId, long offset, long limit,
            BlogPostOrderStrategy strategy) throws DataAccessException;

    List<BlogPost> getPostsByCategoryId(int categoryId, long offset,
            long limit) throws DataAccessException;

    List<BlogPost> getPostsByCategoryId(int categoryId, long offset,
            long limit, BlogPostOrderStrategy strategy)
            throws DataAccessException;

    List<BlogPost> getPostsByMonthlyArchive(int year, int month, long offset,
            long limit) throws DataAccessException;

    String[] getProperty(long postId, String key) throws DataAccessException;

    List<BlogPost> getPostsByMonthlyArchive(int year, int month, long offset,
            long limit, BlogPostOrderStrategy strategy)
            throws DataAccessException;

    List<MonthArchive> getMonthlyArchiveList() throws DataAccessException;

    long createNewPost(BlogPost post) throws DataAccessException;

    long createNewPost(long authorUserId, int categoryId, Date date,
            String title, String content) throws DataAccessException;

    void setProperty(long postId, String key, String[] values)
            throws DataAccessException;

    void updatePost(BlogPost post) throws DataAccessException;

    void updatePostTitle(long postId, String title)
            throws DataAccessException;

    void updatePostContent(long postId, String content)
            throws DataAccessException;

    void updatePostCategory(long postId, int categoryId)
            throws DataAccessException;

    void deletePost(long postId) throws DataAccessException;

    void deleteProperty(long postId, String key) throws DataAccessException;

    void clearPorperties(long postId) throws DataAccessException;
}
