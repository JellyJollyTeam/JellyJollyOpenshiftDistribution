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

import cn.edu.seu.cose.jellyjolly.dao.BlogInfoDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.DataAccessException;
import cn.edu.seu.cose.jellyjolly.model.BlogInfo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
class BlogInfoDataAccessImpl
        extends AbstractDataAccess implements BlogInfoDataAccess {

    private static final String COLUMN_BLOG_ID = "blog_id";
    private static final String COLUMN_BLOG_TITLE = "blog_title";
    private static final String COLUMN_BLOG_SUBTITLE = "blog_subtitle";
    private static final String COLUMN_BLOG_URL = "blog_url";
    private static final String COLUMN_BLOG_META_KEY = "meta_key";
    private static final String COLUMN_BLOG_META_VALUE = "meta_value";
    private static final String STATEMENT_GET_BLOG_INFO =
            "SELECT * FROM jj_blog_info LIMIT 1;";
    private static final String STATEMENT_SET_BLOG_TITLE =
            "UPDATE jj_blog_info SET blog_title=?;";
    private static final String STATEMENT_SET_BLOG_SUB_TITLE =
            "UPDATE jj_blog_info SET blog_subtitle=?;";
    private static final String STATEMENT_GET_BLOG_INFO_META =
            "SELECT * FROM jj_blog_meta;";
    private static final String STATEMENT_GET_BLOG_INFO_META_BY_KEY =
            "SELECT meta_value FROM jj_blog_meta WHERE meta_key=?;";
    private static final String STATEMENT_ADD_BLOG_INFO_META =
            "INSERT INTO jj_blog_meta(meta_key, meta_value) VALUES (?, ?);";
    private static final String STATEMENT_DELETE_BLOG_INFO_META =
            "DELETE FROM jj_blog_meta WHERE meta_key=?;";
    private static final String STATEMENT_SET_BLOG_INFO_META =
            "UPDATE jj_blog_meta SET meta_value=? WHERE meta_key=? "
            + "AND meta_value=?;";
    private static final String STATEMENT_SET_BLOG_URL =
            "UPDATE jj_blog_info SET blog_url=?;";
    private static final Logger logger = Logger.getLogger(
            BlogInfoDataAccessImpl.class.getName());

    private static Map<String, List<String>> getKeyValuesByResultSet(
            ResultSet rs) throws SQLException {
        Map<String, List<String>> keyValues =
                new HashMap<String, List<String>>();
        while (rs.next()) {
            String key = rs.getString(COLUMN_BLOG_META_KEY);
            String value = rs.getString(COLUMN_BLOG_META_VALUE);

            if (!keyValues.containsKey(key)) {
                List<String> list = new LinkedList<String>();
                list.add(value);
                keyValues.put(key, list);
                continue;
            }

            List<String> list = keyValues.get(key);
            list.add(value);
        }
        return keyValues;
    }

    private static void addPropertiesIntoBlogInfo(
            Map<String, List<String>> keyValues, BlogInfo info) {
        for (Entry<String, List<String>> e : keyValues.entrySet()) {
            String key = e.getKey();
            List<String> values = e.getValue();
            String[] strArr = values.toArray(new String[0]);
            info.setOtherProperty(key, strArr);
        }
    }

    public BlogInfoDataAccessImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public BlogInfo getBlogInfoInstance() throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps1 = connection.prepareStatement(
                    STATEMENT_GET_BLOG_INFO);
            ResultSet rs1 = ps1.executeQuery();

            if (!rs1.next()) {
                return null;
            }

            int blogId = rs1.getInt(COLUMN_BLOG_ID);
            String title = rs1.getString(COLUMN_BLOG_TITLE);
            String subTitle = rs1.getString(COLUMN_BLOG_SUBTITLE);
            String blogUrl = rs1.getString(COLUMN_BLOG_URL);

            PreparedStatement ps2 = connection.prepareStatement(
                    STATEMENT_GET_BLOG_INFO_META);
            ResultSet rs2 = ps2.executeQuery();
            Map<String, List<String>> keyValues = getKeyValuesByResultSet(rs2);

            BlogInfo info = new BlogInfo();
            info.setBlogId(blogId);
            info.setBlogTitle(title);
            info.setBlogSubTitle(subTitle);
            info.setBlogUrl(blogUrl);
            addPropertiesIntoBlogInfo(keyValues, info);

            return info;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public void setBlogTitle(String title) throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_SET_BLOG_TITLE);
            ps.setString(1, title);
            ps.executeUpdate();
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public void setBlogSubTitle(String subTitle) throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_SET_BLOG_SUB_TITLE);
            ps.setString(1, subTitle);
            ps.executeUpdate();
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    public void setBlogUrl(String blogUrl) throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_SET_BLOG_URL);
            ps.setString(1, blogUrl);
            ps.executeUpdate();
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public void addBlogInfoMeta(String key, String value)
            throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_ADD_BLOG_INFO_META);
            ps.setString(1, key);
            ps.setString(2, value);
            ps.executeUpdate();
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new DataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public void deleteBlogInfoMeta(String key) throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_DELETE_BLOG_INFO_META);
            ps.setString(1, key);
            ps.executeUpdate();
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new DataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public String[] getBlogInfoMeta(String key) throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_GET_BLOG_INFO_META_BY_KEY);
            ps.setString(1, key);
            ResultSet rs = ps.executeQuery();

            List<String> strList = new LinkedList<String>();
            while (rs.next()) {
                String value = rs.getString(COLUMN_BLOG_META_VALUE);
                strList.add(value);
            }
            return strList.toArray(new String[0]);
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new DataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public void setBlogInfoMeta(String key, String previousValue,
            String currentValue) throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_SET_BLOG_INFO_META);
            ps.setString(1, currentValue);
            ps.setString(2, key);
            ps.setString(3, previousValue);
            ps.executeUpdate();
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new DataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    public void setBlogInfoMeta(String key, String[] values)
            throws DataAccessException {
        Connection connection = newConnection();
        try {
            connection.setAutoCommit(false);
            // transaction starts
            PreparedStatement ps1 = connection.prepareStatement(
                    STATEMENT_DELETE_BLOG_INFO_META);
            ps1.setString(1, key);
            ps1.executeUpdate();

            for (String value : values) {
                PreparedStatement ps2 = connection.prepareStatement(
                        STATEMENT_ADD_BLOG_INFO_META);
                ps2.setString(1, key);
                ps2.setString(2, value);
                ps2.executeUpdate();
            }
            // transaction ends
            connection.commit();
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            rollbackConnection(connection);
            throw new DataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }
}
