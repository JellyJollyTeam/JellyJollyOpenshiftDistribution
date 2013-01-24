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

import cn.edu.seu.cose.jellyjolly.dao.AdminUserDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.BlogPostDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.CategoryDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.CommentDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.DataAccessException;
import cn.edu.seu.cose.jellyjolly.model.BlogPost;
import cn.edu.seu.cose.jellyjolly.model.MonthArchive;
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
class BlogPostDataAccessImpl
        extends AbstractDataAccess implements BlogPostDataAccess {

    private static final BlogPostOrderStrategy DEFAULT_STRATEGY =
            BlogPostOrderStrategy.ORDER_BY_DATE_DESC;
    private static final String COLUMN_POST_ID = "blog_post_id";
    private static final String COLUMN_AUTHOR_USER_ID = "author_user_id";
    private static final String COLUMN_CATEGORY_ID = "category_id";
    private static final String COLUMN_POST_DATE = "post_date";
    private static final String COLUMN_POST_TITLE = "post_title";
    private static final String COLUMN_POST_CONTENT = "post_content";
    private static final String COLUMN_META_VALUE = "meta_value";
    private static final String COLUMN_YEAR = "year";
    private static final String COLUMN_MONTH = "month";
    private static final String COLUMN_COUNT = "count";
    private static final String STMT_GET_POST_BY_ID =
            "SELECT * FROM jj_blog_posts WHERE blog_post_id=?;";
    private static final String STMT_GET_LATEST =
            "SELECT post_date FROM jj_blog_posts ORDER BY post_date DESC "
            + "LIMIT 1;";
    private static final String STMT_GET_LATEST_BY_USER_ID =
            "SELECT MAX(post_date) FROM jj_blog_posts WHERE user_id=?;";
    private static final String STMT_GET_POSTS =
            "SELECT * FROM jj_blog_posts LIMIT ?, ?;";
    private static final String STMT_GET_POSTS_ORDER_BY_DATE_ASC =
            "SELECT * FROM jj_blog_posts ORDER BY post_date ASC LIMIT ?, ?;";
    private static final String STMT_GET_POSTS_ORDER_BY_DATE_DESC =
            "SELECT * FROM jj_blog_posts ORDER BY post_date DESC LIMIT ?, ?;";
    private static final String STMT_GET_POSTS_BY_DATE =
            "SELECT * FROM jj_blog_posts WHERE post_date BETWEEN ? AND ? "
            + "ORDER BY post_date LIMIT ?, ?;";
    private static final String STMT_GET_POSTS_BY_DATE_ORDER_BY_DATE_ASC =
            "SELECT * FROM jj_blog_posts WHERE post_date BETWEEN ? AND ? "
            + "ORDER BY post_date ASC LIMIT ?, ?;";
    private static final String STMT_GET_PSTS_BY_DATE_ORDER_BY_DATE_DESC =
            "SELECT * FROM jj_blog_posts WHERE post_date BETWEEN ? AND ? "
            + "ORDER BY post_date DESC LIMIT ?, ?;";
    private static final String STMT_GET_POSTS_BY_KEYWORD =
            "SELECT * FROM jj_blog_posts WHERE "
            + "post_title LIKE CONCAT('%', ?, '%') "
            + "OR post_content LIKE CONCAT('%', ?, '%') "
            + "LIMIT ?, ?;";
    private static final String STMT_GET_PSTS_BY_KEYWORD_ORDER_BY_DATE_ASC =
            "SELECT * FROM jj_blog_posts WHERE "
            + "post_title LIKE CONCAT('%', ?, '%') "
            + "OR post_content LIKE CONCAT('%', ?, '%') "
            + "ORDER BY post_date ASC LIMIT ?, ?;";
    private static final String STMT_GET_PSTS_BY_KEYWORD_ORDER_BY_DATE_DESC =
            "SELECT * FROM jj_blog_posts WHERE "
            + "post_title LIKE CONCAT('%', ?, '%') "
            + "OR post_content LIKE CONCAT('%', ?, '%') "
            + "ORDER BY post_date DESC LIMIT ?, ?;";
    private static final String STMT_GET_POSTS_BY_USER_ID =
            "SELECT * FROM jj_blog_posts WHERE author_user_id=? LIMIT ?, ?;";
    private static final String STMT_GET_POSTS_BY_USER_ID_ORDER_BY_DATE_ASC =
            "SELECT * FROM jj_blog_posts WHERE author_user_id=? "
            + "ORDER BY post_date ASC LIMIT ?, ?;";
    private static final String STMT_GET_POSTS_BY_USER_ID_ORDER_BY_DATE_DESC =
            "SELECT * FROM jj_blog_posts WHERE author_user_id=? "
            + "ORDER BY post_date DESC LIMIT ?, ?;";
    private static final String STMT_GET_POSTS_BY_CATEGORY_ID =
            "SELECT * FROM jj_blog_posts WHERE category_id=? LIMIT ?, ?;";
    private static final String STMT_GET_POSTS_BY_CATEGORY_ID_DATE_ASC =
            "SELECT * FROM jj_blog_posts WHERE category_id=? "
            + "ORDER BY post_date ASC LIMIT ?, ?;";
    private static final String STMT_GET_POSTS_BY_CATEGORY_ID_DATE_DESC =
            "SELECT * FROM jj_blog_posts WHERE category_id=? "
            + "ORDER BY post_date DESC LIMIT ?, ?;";
    private static final String STMT_GET_POSTS_MONTHLY =
            "SELECT * FROM jj_blog_posts WHERE YEAR(post_date)=? "
            + "AND MONTH(post_date)=? LIMIT ?, ?;";
    private static final String STMT_GET_POSTS_MONTHLY_DATE_ASC =
            "SELECT * FROM jj_blog_posts WHERE YEAR(post_date)=? "
            + "AND MONTH(post_date)=? ORDER BY post_date ASC LIMIT ?, ?;";
    private static final String STMT_GET_POSTS_MONTHLY_DATE_DESC =
            "SELECT * FROM jj_blog_posts WHERE YEAR(post_date)=? "
            + "AND MONTH(post_date)=? ORDER BY post_date DESC LIMIT ?, ?;";
    private static final String STMT_GET_POSTS_NUM =
            "SELECT COUNT(1) FROM jj_blog_posts;";
    private static final String STMT_GET_MONTHLY_ARCHIVE_LIST =
            "SELECT YEAR(post_date) AS year, MONTH(post_date) AS month, "
            + "COUNT(1) AS count FROM jj_blog_posts GROUP BY year, month "
            + "ORDER BY year DESC, month DESC;";
    private static final String STMT_GET_ARCHIVE_POSTS_NUM =
            "SELECT count FROM (SELECT YEAR(post_date) AS year, "
            + "MONTH(post_date) AS month, COUNT(1) AS count FROM jj_blog_posts "
            + "GROUP BY year, month) t WHERE year=? AND month=?;";
    private static final String STMT_GET_CATEGORY_POSTS_NUM =
            "SELECT COUNT(1) FROM jj_blog_posts WHERE category_id=?;";
    private static final String STMT_GET_POSTS_NUM_BY_KEYWORD =
            "SELECT COUNT(1) FROM jj_blog_posts WHERE "
            + "post_title LIKE CONCAT('%', ?, '%') "
            + "OR post_content LIKE CONCAT('%', ?, '%') ";
    private static final String STMT_CREATE_NEW_POST =
            "INSERT INTO jj_blog_posts(author_user_id, category_id, post_date,"
            + "post_title, post_content) VALUES (?, ?, ?, ?, ?);";
    private static final String STMT_UPDATE_POST =
            "UPDATE jj_blog_posts SET author_user_id=?, category_id=?, "
            + "post_date=?, post_title=?, post_content=? WHERE blog_post_id=?;";
    private static final String STMT_UPDATE_TITLE =
            "UPDATE jj_blog_posts SET post_title=? WHERE blog_post_id=?;";
    private static final String STMT_UPDATE_CONTENT =
            "UPDATE jj_blog_posts SET post_content=? WHERE blog_post_id=?;";
    private static final String STMT_UPDATE_CATEGORY =
            "UPDATE jj_blog_posts SET category_id=? WHERE blog_post_id=?;";
    private static final String STMT_DELETE_POST =
            "DELETE FROM jj_blog_posts WHERE blog_post_id=?;";
    private static final String STMT_GET_PROPERTY =
            "SELECT * FROM jj_blog_post_meta WHERE post_id=? AND meta_key=?;";
    private static final String STMT_DEL_PROPERTY =
            "DELETE FROM jj_blog_post_meta WHERE post_id=? AND meta_key=?;";
    private static final String STMT_SET_PROPERTY =
            "INSERT INTO jj_blog_post_meta(post_id, meta_key, meta_value) "
            + "VALUES (?, ?, ?);";
    private static final String STMT_CLEAR_PROPERTIES =
            "DELETE FROM jj_blog_post_meta WHERE post_id=?;";
    private static final Logger logger = Logger.getLogger(
            BlogPostDataAccessImpl.class.getName());
    private AdminUserDataAccess adminUserDataAccess;
    private CategoryDataAccess categoryDataAccess;
    private CommentDataAccess commentDataAccess;

    private BlogPost getPostByResultSet(ResultSet rs) throws SQLException,
            DataAccessException {
        long id = rs.getLong(COLUMN_POST_ID);
        long authorId = rs.getLong(COLUMN_AUTHOR_USER_ID);
        int categoryId = rs.getInt(COLUMN_CATEGORY_ID);
        Timestamp postDate = rs.getTimestamp(COLUMN_POST_DATE);
        String title = rs.getString(COLUMN_POST_TITLE);
        String content = rs.getString(COLUMN_POST_CONTENT);

        BlogPost post = new BlogPost();
        post.setAuthor(adminUserDataAccess.getUser(authorId));
        post.setCategory(categoryDataAccess.getCategoryById(categoryId));
        post.setComments(commentDataAccess.getCommentsByPostId(id));
        post.setDate(new Date(postDate.getTime()));
        post.setPostId(id);
        post.setTitle(title);
        post.setContent(content);
        return post;
    }

    private List<BlogPost> getPostsByResultSet(ResultSet rs)
            throws SQLException, DataAccessException {
        List<BlogPost> postList = new LinkedList<BlogPost>();
        while (rs.next()) {
            BlogPost post = getPostByResultSet(rs);
            postList.add(post);
        }
        return postList;
    }

    private Date getDateByResultSet(ResultSet rs) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(COLUMN_POST_DATE);
        return (timestamp == null)
                ? null
                : new Date(timestamp.getTime());
    }

    public BlogPostDataAccessImpl(DataSource dataSource) {
        super(dataSource);
        this.adminUserDataAccess = new AdminUserDataAccessImpl(dataSource);
        this.categoryDataAccess = new CategoryDataAccessImpl(dataSource);
        this.commentDataAccess = new CommentDataAccessImpl(dataSource);
    }

    @Override
    public Date getLatestPubDate() throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STMT_GET_LATEST);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return null;
            }

            return getDateByResultSet(rs);
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public Date getLatestPubDate(long userId) throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STMT_GET_LATEST_BY_USER_ID);
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return null;
            }

            return getDateByResultSet(rs);
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public BlogPost getPostById(long postId) throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STMT_GET_POST_BY_ID);
            ps.setLong(1, postId);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return null;
            }

            return getPostByResultSet(rs);
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public List<BlogPost> getPosts(long offset, long limit)
            throws DataAccessException {
        return getPosts(offset, limit, DEFAULT_STRATEGY);
    }

    @Override
    public List<BlogPost> getPosts(long offset, long limit,
            BlogPostOrderStrategy strategy) throws DataAccessException {
        Connection connection = newConnection();
        try {
            String statement;
            switch (strategy) {
                case ORDER_BY_DATE_ASC:
                    statement = STMT_GET_POSTS_ORDER_BY_DATE_ASC;
                    break;
                case ORDER_BY_DATE_DESC:
                    statement = STMT_GET_POSTS_ORDER_BY_DATE_DESC;
                    break;
                default:
                    statement = STMT_GET_POSTS;
                    break;
            }
            PreparedStatement ps = connection.prepareStatement(statement);
            ps.setLong(1, offset);
            ps.setLong(2, limit);
            ResultSet rs = ps.executeQuery();
            return getPostsByResultSet(rs);
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public List<BlogPost> getPosts(Date from, Date to, long offset, long limit)
            throws DataAccessException {
        return getPosts(from, to, offset, limit, DEFAULT_STRATEGY);
    }

    @Override
    public List<BlogPost> getPosts(Date from, Date to, long offset, long limit,
            BlogPostOrderStrategy strategy) throws DataAccessException {
        Connection connection = newConnection();
        try {
            String statement;
            switch (strategy) {
                case ORDER_BY_DATE_ASC:
                    statement = STMT_GET_POSTS_BY_DATE_ORDER_BY_DATE_ASC;
                    break;
                case ORDER_BY_DATE_DESC:
                    statement = STMT_GET_PSTS_BY_DATE_ORDER_BY_DATE_DESC;
                    break;
                default:
                    statement = STMT_GET_POSTS_BY_DATE;
                    break;
            }
            PreparedStatement ps = connection.prepareStatement(statement);
            ps.setTimestamp(1, new Timestamp(from.getTime()));
            ps.setTimestamp(2, new Timestamp(to.getTime()));
            ps.setLong(3, offset);
            ps.setLong(4, limit);
            ResultSet rs = ps.executeQuery();
            return getPostsByResultSet(rs);
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public List<BlogPost> getPostsByKeyword(String keyword, long offset,
            long limit) throws DataAccessException {
        return getPostsByKeyword(keyword, offset, limit, DEFAULT_STRATEGY);
    }

    @Override
    public List<BlogPost> getPostsByKeyword(String keyword, long offset,
            long limit, BlogPostOrderStrategy strategy)
            throws DataAccessException {
        Connection connection = newConnection();
        try {
            String statement;
            switch (strategy) {
                case ORDER_BY_DATE_ASC:
                    statement = STMT_GET_PSTS_BY_KEYWORD_ORDER_BY_DATE_ASC;
                    break;
                case ORDER_BY_DATE_DESC:
                    statement = STMT_GET_PSTS_BY_KEYWORD_ORDER_BY_DATE_DESC;
                    break;
                default:
                    statement = STMT_GET_POSTS_BY_KEYWORD;
                    break;
            }
            PreparedStatement ps = connection.prepareStatement(statement);
            ps.setString(1, keyword);
            ps.setString(2, keyword);
            ps.setLong(3, offset);
            ps.setLong(4, limit);
            ResultSet rs = ps.executeQuery();
            return getPostsByResultSet(rs);
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public List<BlogPost> getPostsByUserId(long userId, long offset,
            long limit) throws DataAccessException {
        return getPostsByUserId(userId, offset, limit, DEFAULT_STRATEGY);
    }

    @Override
    public List<BlogPost> getPostsByUserId(long userId, long offset,
            long limit, BlogPostOrderStrategy strategy)
            throws DataAccessException {
        Connection connection = newConnection();
        try {
            String statement;
            switch (strategy) {
                case ORDER_BY_DATE_ASC:
                    statement = STMT_GET_POSTS_BY_USER_ID_ORDER_BY_DATE_ASC;
                    break;
                case ORDER_BY_DATE_DESC:
                    statement = STMT_GET_POSTS_BY_USER_ID_ORDER_BY_DATE_DESC;
                    break;
                default:
                    statement = STMT_GET_POSTS_BY_USER_ID;
                    break;
            }
            PreparedStatement ps = connection.prepareStatement(statement);
            ps.setLong(1, userId);
            ps.setLong(2, offset);
            ps.setLong(3, limit);
            ResultSet rs = ps.executeQuery();
            return getPostsByResultSet(rs);
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public List<BlogPost> getPostsByCategoryId(int categoryId, long offset,
            long limit) throws DataAccessException {
        return getPostsByCategoryId(categoryId, offset, limit,
                DEFAULT_STRATEGY);
    }

    @Override
    public List<BlogPost> getPostsByCategoryId(int categoryId, long offset,
            long limit, BlogPostOrderStrategy strategy)
            throws DataAccessException {
        Connection connection = newConnection();
        try {
            String statement;
            switch (strategy) {
                case ORDER_BY_DATE_ASC:
                    statement = STMT_GET_POSTS_BY_CATEGORY_ID_DATE_ASC;
                    break;
                case ORDER_BY_DATE_DESC:
                    statement = STMT_GET_POSTS_BY_CATEGORY_ID_DATE_DESC;
                    break;
                default:
                    statement = STMT_GET_POSTS_BY_CATEGORY_ID;
                    break;
            }
            PreparedStatement ps = connection.prepareStatement(statement);
            ps.setLong(1, categoryId);
            ps.setLong(2, offset);
            ps.setLong(3, limit);
            ResultSet rs = ps.executeQuery();
            return getPostsByResultSet(rs);
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public List<BlogPost> getPostsByMonthlyArchive(int year, int month,
            long offset, long limit) throws DataAccessException {
        return getPostsByMonthlyArchive(year, month, offset, limit,
                DEFAULT_STRATEGY);
    }

    @Override
    public List<BlogPost> getPostsByMonthlyArchive(int year, int month,
            long offset, long limit, BlogPostOrderStrategy strategy)
            throws DataAccessException {
        Connection connection = newConnection();
        try {
            String statement;
            switch (strategy) {
                case ORDER_BY_DATE_ASC:
                    statement = STMT_GET_POSTS_MONTHLY_DATE_ASC;
                    break;
                case ORDER_BY_DATE_DESC:
                    statement = STMT_GET_POSTS_MONTHLY_DATE_DESC;
                    break;
                default:
                    statement = STMT_GET_POSTS_MONTHLY;
                    break;
            }
            PreparedStatement ps = connection.prepareStatement(statement);
            ps.setInt(1, year);
            ps.setInt(2, month);
            ps.setLong(3, offset);
            ps.setLong(4, limit);
            ResultSet rs = ps.executeQuery();
            return getPostsByResultSet(rs);
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public long createNewPost(BlogPost post) throws DataAccessException {
        return createNewPost(post.getAuthor().getUserId(),
                post.getCategory().getCategoryId(), post.getDate(),
                post.getTitle(), post.getContent());
    }

    @Override
    public long createNewPost(long authorUserId, int categoryId, Date date,
            String title, String content) throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STMT_CREATE_NEW_POST, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, authorUserId);
            ps.setInt(2, categoryId);
            Timestamp time = (date == null)
                    ? null
                    : new Timestamp(date.getTime());
            ps.setTimestamp(3, time);
            ps.setString(4, title);
            ps.setString(5, content);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (!rs.next()) {
                return 0;
            }
            return rs.getLong(1);
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public void updatePost(BlogPost post) throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STMT_UPDATE_POST);
            ps.setLong(1, post.getAuthor().getUserId());
            ps.setInt(2, post.getCategory().getCategoryId());
            Timestamp time = (post.getDate() == null)
                    ? null
                    : new Timestamp(post.getDate().getTime());
            ps.setTimestamp(3, time);
            ps.setString(4, post.getTitle());
            ps.setString(5, post.getContent());
            ps.setLong(6, post.getPostId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public void updatePostTitle(long postId, String title)
            throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STMT_UPDATE_TITLE);
            ps.setString(1, title);
            ps.setLong(2, postId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public void updatePostContent(long postId, String content)
            throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STMT_UPDATE_CONTENT);
            ps.setString(1, content);
            ps.setLong(2, postId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public void updatePostCategory(long postId, int categoryId)
            throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STMT_UPDATE_CATEGORY);
            ps.setInt(1, categoryId);
            ps.setLong(2, postId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public void deletePost(long postId) throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STMT_DELETE_POST);
            ps.setLong(1, postId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    public String[] getProperty(long postId, String key)
            throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STMT_GET_PROPERTY);
            ps.setLong(1, postId);
            ps.setString(2, key);
            ResultSet rs = ps.executeQuery();
            List<String> strlst = new LinkedList<String>();
            while (rs.next()) {
                String value = rs.getString(COLUMN_META_VALUE);
                strlst.add(value);
            }
            return strlst.toArray(new String[0]);
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    public void setProperty(long postId, String key, String[] values)
            throws DataAccessException {
        Connection connection = newConnection();
        try {
            connection.setAutoCommit(false);
            // transaction begins
            PreparedStatement ps1 = connection.prepareStatement(
                    STMT_DEL_PROPERTY);
            ps1.setLong(1, postId);
            ps1.setString(2, key);
            ps1.executeUpdate();
            PreparedStatement ps2 = connection.prepareStatement(
                    STMT_SET_PROPERTY);
            for (String value : values) {
                ps2.setLong(1, postId);
                ps2.setString(2, key);
                ps2.setString(3, value);
                ps2.executeUpdate();
            }
            // transaction ends
            connection.commit();
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            rollbackConnection(connection);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    public void deleteProperty(long postId, String key)
            throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STMT_DEL_PROPERTY);
            ps.setLong(1, postId);
            ps.setString(2, key);
            ps.executeUpdate();
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    public void clearPorperties(long postId) throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STMT_CLEAR_PROPERTIES);
            ps.setLong(1, postId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    public long getPostNumber() throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STMT_GET_POSTS_NUM);
            ResultSet rs = ps.executeQuery();
            return (rs.next()) ? rs.getLong(1) : 0;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    public long getPostNumber(int year, int month)
            throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STMT_GET_ARCHIVE_POSTS_NUM);
            ps.setInt(1, year);
            ps.setInt(2, month);
            ResultSet rs = ps.executeQuery();
            return (rs.next()) ? rs.getLong(1) : 0;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    public long getPostNumber(int categoryId) throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STMT_GET_CATEGORY_POSTS_NUM);
            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();
            return (rs.next()) ? rs.getLong(1) : 0;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    public long getPostNumber(String keyword) throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STMT_GET_POSTS_NUM_BY_KEYWORD);
            ps.setString(1, keyword);
            ps.setString(2, keyword);
            ResultSet rs = ps.executeQuery();
            return (rs.next()) ? rs.getLong(1) : 0;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    public List<MonthArchive> getMonthlyArchiveList()
            throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STMT_GET_MONTHLY_ARCHIVE_LIST);
            ResultSet rs = ps.executeQuery();
            List<MonthArchive> aList = new LinkedList<MonthArchive>();
            while (rs.next()) {
                int year = rs.getInt(COLUMN_YEAR);
                int month = rs.getInt(COLUMN_MONTH);
                long count = rs.getLong(COLUMN_COUNT);
                MonthArchive ma = new MonthArchive();
                ma.setCount(count);
                ma.setMonth(month);
                ma.setYear(year);
                aList.add(ma);
            }
            return aList;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }
}
