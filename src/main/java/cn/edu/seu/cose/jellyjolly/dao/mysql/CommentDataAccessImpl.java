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
package cn.edu.seu.cose.jellyjolly.dao.mysql;

import cn.edu.seu.cose.jellyjolly.dao.CommentDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.DataAccessException;
import cn.edu.seu.cose.jellyjolly.model.Comment;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
class CommentDataAccessImpl
        extends AbstractDataAccess implements CommentDataAccess {

    private static final CommentOrderStrategy DEFAULT_STRATEGY =
            CommentOrderStrategy.ORDERED_BY_DATE_ASC;
    private static final String COLUMN_COMMENT_ID = "comment_id";
    private static final String COLUMN_PARENT_COMMENT_ID = "parent_comment_id";
    private static final String COLUMN_POST_ID = "blog_post_id";
    private static final String COLUMN_USER_ID = "comment_user_author_id";
    private static final String COLUMN_AUTHOR_NAME = "comment_author_name";
    private static final String COLUMN_AUTHOR_EMAIL = "comment_author_email";
    private static final String COLUMN_AUTHOR_HOME_PAGE =
            "comment_author_home_page_url";
    private static final String COLUMN_DATE = "comment_date";
    private static final String COLUMN_CONTENT = "comment_content";
    private static final String STATEMENT_GET_COMMENTS =
            "SELECT * FROM jj_blog_comments;";
    private static final String STATEMENT_GET_RECENT_COMMENTS =
            "SELECT * FROM jj_blog_comments ORDER BY comment_date DESC LIMIT ?";
    private static final String STATEMENT_GET_COMMENTS_BY_LIMIT =
            "SELECT * FROM jj_blog_comments LIMIT ?, ?;";
    private static final String STATEMENT_GET_COMMENT_BY_ID =
            "SELECT * FROM jj_blog_comments WHERE comment_id=?;";
    private static final String STATEMENT_GET_COMMENT_NUM =
            "SELECT COUNT(1) FROM jj_blog_comments;";
    private static final String STATEMENT_GET_COMMENTS_BY_PARENT_ID =
            "SELECT * FROM jj_blog_comments WHERE parent_comment_id=?;";
    private static final String STATEMENT_GET_COMMENTS_BY_PARENT_ID_DATE_ASC =
            "SELECT * FROM jj_blog_comments WHERE parent_comment_id=? "
            + "ORDER BY comment_date ASC;";
    private static final String STATEMENT_GET_COMMENTS_BY_PARENT_ID_DATE_DESC =
            "SELECT * FROM jj_blog_comments WHERE parent_comment_id=? "
            + "ORDER BY comment_date DESC;";
    private static final String STATEMENT_GET_COMMENTS_BY_POST_ID =
            "SELECT * FROM jj_blog_comments WHERE blog_post_id=?;";
    private static final String STATEMENT_GET_COMMENTS_BY_POST_ID_DATE_ASC =
            "SELECT * FROM jj_blog_comments WHERE blog_post_id=? "
            + "ORDER BY comment_date ASC;";
    private static final String STATEMENT_GET_COMMENTS_BY_POST_ID_DATE_DESC =
            "SELECT * FROM jj_blog_comments WHERE blog_post_id=? "
            + "ORDER BY comment_date DESC;";
    private static final String STATEMENT_UPDATE_COMMENT =
            "UPDATE jj_blog_comments SET comment_content=? WHERE comment_id=?";
    private static final String STATEMENT_ADD_COMMENT =
            "INSERT INTO jj_blog_comments(parent_comment_id, blog_post_id, "
            + "comment_author_name, comment_author_email, "
            + "comment_author_home_page_url, comment_date, comment_content) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?);";
    private static final String STATEMENT_ADD_COMMENT_WITHOUT_PARENT =
            "INSERT INTO jj_blog_comments(parent_comment_id, blog_post_id, "
            + "comment_author_name, comment_author_email, "
            + "comment_author_home_page_url, comment_date, comment_content) "
            + "VALUES (0, ?, ?, ?, ?, ?, ?);";
    private static final String STATEMENT_ADD_COMMENT_BY_USER_ID =
            "INSERT INTO jj_blog_comments("
            + "parent_comment_id, blog_post_id, comment_author_name, "
            + "comment_author_email, comment_author_home_page_url, "
            + "comment_date, comment_content) SELECT ?, ?, "
            + "display_name as comment_author_name, "
            + "user_email as comment_author_email, "
            + "user_home_page_url as comment_author_home_page_url, ?, ? "
            + "FROM jj_users WHERE user_id=?;";
    private static final String STATEMENT_ADD_CMMT_BY_USER_ID_WITHOUT_PARENT =
            "INSERT INTO jj_blog_comments("
            + "parent_comment_id, blog_post_id, comment_user_author_id, "
            + "comment_author_name, comment_author_email, "
            + "comment_author_home_page_url, "
            + "comment_date, comment_content) SELECT 0, ?, ?, "
            + "display_name as comment_author_name, "
            + "user_email as comment_author_email, "
            + "user_home_page_url as comment_author_home_page_url, ?, ? "
            + "FROM jj_users WHERE user_id=?;";
    private static final String STATEMENT_DELETE_COMMENT_BY_ID =
            "DELETE FROM jj_blog_comments WHERE comment_id=?;";
    private static final String STATEMENT_DELETE_COMMENTS_BY_POST_ID =
            "DELETE FROM jj_blog_comments WHERE blog_post_id=?;";
    private static final Logger logger = Logger.getLogger(
            CommentDataAccessImpl.class.getName());

    public CommentDataAccessImpl(DataSource dataSource) {
        super(dataSource);
    }

    private Comment getCommentByResultSet(ResultSet rs) throws SQLException {
        long id = rs.getLong(COLUMN_COMMENT_ID);
        long parentId = rs.getLong(COLUMN_PARENT_COMMENT_ID);
        long postId = rs.getLong(COLUMN_POST_ID);
        long userId = rs.getLong(COLUMN_USER_ID);
        String authorName = rs.getString(COLUMN_AUTHOR_NAME);
        String authorEmail = rs.getString(COLUMN_AUTHOR_EMAIL);
        String authorHomePage = rs.getString(COLUMN_AUTHOR_HOME_PAGE);
        Timestamp time = rs.getTimestamp(COLUMN_DATE);
        String content = rs.getString(COLUMN_CONTENT);

        Comment comment = new Comment();
        comment.setAuthorEmail(authorEmail);
        comment.setAuthorHomePageUrl(authorHomePage);
        comment.setAuthorName(authorName);
        comment.setAuthorUserId(userId);
        comment.setCommentId(id);
        comment.setContent(content);
        comment.setDate(new Date(time.getTime()));
        comment.setPostId(postId);

        if (parentId != 0) {
            try {
                Comment parentComment = getCommentById(parentId);
                comment.setParentComment(parentComment);
            } catch (DataAccessException ex) {
                comment.setParentComment(null);
            }
        }
        return comment;
    }

    private List<Comment> getCommentsByResultSet(ResultSet rs)
            throws SQLException {
        List<Comment> commentList = new LinkedList<Comment>();
        while (rs.next()) {
            Comment comment = getCommentByResultSet(rs);
            commentList.add(comment);
        }
        return commentList;
    }

    public List<Comment> getComments() throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_GET_COMMENTS);
            ResultSet rs = ps.executeQuery();
            return getCommentsByResultSet(rs);
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    public List<Comment> getComments(long offset, long limit)
            throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_GET_COMMENTS_BY_LIMIT);
            ps.setLong(1, offset);
            ps.setLong(2, limit);
            ResultSet rs = ps.executeQuery();
            return getCommentsByResultSet(rs);
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public List<Comment> getRecentComments(long number)
            throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_GET_RECENT_COMMENTS);
            ps.setLong(1, number);
            ResultSet rs = ps.executeQuery();
            return getCommentsByResultSet(rs);
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public Comment getCommentById(long commentId) throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_GET_COMMENT_BY_ID);
            ps.setLong(1, commentId);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return null;
            }

            Comment comment = getCommentByResultSet(rs);
            return comment;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public List<Comment> getCommentsByParentCommentId(long parentCommentId)
            throws DataAccessException {
        return getCommentsByParentCommentId(parentCommentId, DEFAULT_STRATEGY);
    }

    @Override
    public List<Comment> getCommentsByParentCommentId(long parentCommentId,
            CommentOrderStrategy strategy) throws DataAccessException {
        Connection connection = newConnection();
        try {
            String statement;
            switch (strategy) {
                case ORDERED_BY_DATE_ASC:
                    statement = STATEMENT_GET_COMMENTS_BY_PARENT_ID_DATE_ASC;
                    break;
                case ORDERED_BY_DATE_DESC:
                    statement = STATEMENT_GET_COMMENTS_BY_PARENT_ID_DATE_DESC;
                    break;
                default:
                    statement = STATEMENT_GET_COMMENTS_BY_PARENT_ID;
                    break;
            }
            PreparedStatement ps = connection.prepareStatement(statement);
            ps.setLong(1, parentCommentId);
            ResultSet rs = ps.executeQuery();
            return getCommentsByResultSet(rs);
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public List<Comment> getCommentsByPostId(long postId)
            throws DataAccessException {
        return getCommentsByPostId(postId, DEFAULT_STRATEGY);
    }

    @Override
    public List<Comment> getCommentsByPostId(long postId,
            CommentOrderStrategy strategy) throws DataAccessException {
        Connection connection = newConnection();
        try {
            String statement;
            switch (strategy) {
                case ORDERED_BY_DATE_ASC:
                    statement = STATEMENT_GET_COMMENTS_BY_POST_ID_DATE_ASC;
                    break;
                case ORDERED_BY_DATE_DESC:
                    statement = STATEMENT_GET_COMMENTS_BY_POST_ID_DATE_DESC;
                    break;
                default:
                    statement = STATEMENT_GET_COMMENTS_BY_POST_ID;
                    break;
            }
            PreparedStatement ps = connection.prepareStatement(statement);
            ps.setLong(1, postId);
            ResultSet rs = ps.executeQuery();
            return getCommentsByResultSet(rs);
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    public long getCommentNumber() throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_GET_COMMENT_NUM);
            ResultSet rs = ps.executeQuery();
            return (!rs.next()) ? 0 : rs.getLong(1);
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public void updateComment(long commentId, String content)
            throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_UPDATE_COMMENT);
            ps.setString(1, content);
            ps.setLong(2, commentId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public long addNewComment(Comment comment) throws DataAccessException {
        return addNewComment(comment.getPostId(), comment.getAuthorName(),
                comment.getAuthorEmail(), comment.getAuthorHomePageUrl(),
                comment.getContent(), comment.getDate());
    }

    @Override
    public long addNewComment(long postId, String authorName,
            String authorEmail, String authorHomePageURL, String content,
            long parentCommentId, Date date) throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_ADD_COMMENT,
                    Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, parentCommentId);
            ps.setLong(2, postId);
            ps.setString(3, authorName);
            ps.setString(4, authorEmail);
            ps.setString(5, authorHomePageURL);
            ps.setTimestamp(6, (date != null)
                    ? new Timestamp(date.getTime())
                    : null);
            ps.setString(7, content);
            int rowCount = ps.executeUpdate();
            if (rowCount <= 0) {
                return 0;
            }

            ResultSet rs = ps.getGeneratedKeys();
            if (!rs.next()) {
                return 0;
            }

            long id = rs.getLong(1);
            return id;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public long addNewComment(long postId, long userId, String content,
            Date date) throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_ADD_CMMT_BY_USER_ID_WITHOUT_PARENT,
                    Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, postId);
            ps.setLong(2, userId);
            ps.setTimestamp(3, (date != null)
                    ? new Timestamp(date.getTime())
                    : null);
            ps.setString(4, content);
            ps.setLong(5, userId);
            int rowCount = ps.executeUpdate();
            if (rowCount <= 0) {
                return 0;
            }

            ResultSet rs = ps.getGeneratedKeys();
            if (!rs.next()) {
                return 0;
            }

            long id = rs.getLong(1);
            return id;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public long addNewComment(long postId, long userId, String content,
            long parentCommentId, Date date) throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_ADD_COMMENT_BY_USER_ID,
                    Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, parentCommentId);
            ps.setLong(2, postId);
            ps.setTimestamp(3, (date != null)
                    ? new Timestamp(date.getTime())
                    : null);
            ps.setString(4, content);
            ps.setLong(5, userId);
            int rowCount = ps.executeUpdate();
            if (rowCount <= 0) {
                return 0;
            }

            ResultSet rs = ps.getGeneratedKeys();
            if (!rs.next()) {
                return 0;
            }

            long id = rs.getLong(1);
            return id;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public long addNewComment(long postId, String authorName,
            String authorEmail, String authorHomePageURL, String content,
            Date date) throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_ADD_COMMENT_WITHOUT_PARENT,
                    Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, postId);
            ps.setString(2, authorName);
            ps.setString(3, authorEmail);
            ps.setString(4, authorHomePageURL);
            ps.setTimestamp(5, (date != null)
                    ? new Timestamp(date.getTime())
                    : null);
            ps.setString(6, content);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (!rs.next()) {
                return 0;
            }

            long id = rs.getLong(1);
            return id;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public void deleteCommentById(long commentId) throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_DELETE_COMMENT_BY_ID);
            ps.setLong(1, commentId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public void deleteCommentsByPostId(long postId)
            throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_DELETE_COMMENTS_BY_POST_ID);
            ps.setLong(1, postId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }
}
