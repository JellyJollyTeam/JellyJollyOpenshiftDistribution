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
import cn.edu.seu.cose.jellyjolly.dao.DataAccessException;
import cn.edu.seu.cose.jellyjolly.model.AdminUser;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
class AdminUserDataAccessImpl
        extends AbstractDataAccess implements AdminUserDataAccess {

    private static final String COLUMN_ID = "user_id";
    private static final String COLUMN_USERNAME = "user_name";
    private static final String COLUMN_PASSWORD = "user_pass";
    private static final String COLUMN_EMAIL = "user_email";
    private static final String COLUMN_HOMEPAGE = "user_home_page_url";
    private static final String COLUMN_DISPLAY_NAME = "display_name";
    private static final String COLUMN_REGISTER_TIME = "register_time";
    private static final String COLUMN_LAST_LOGIN_TIME = "last_login_time";
    private static final String STATEMENT_CONFIRM =
            "SELECT COUNT(*) FROM jj_users "
            + "WHERE user_name=? AND user_pass=PASSWORD(?) LIMIT 1;";
    private static final String STATEMENT_GET_USER =
            "SELECT * FROM jj_users WHERE user_name=? "
            + "AND user_pass=PASSWORD(?) LIMIT 1;";
    private static final String STATEMENT_GET_USER_BY_ID =
            "SELECT * FROM jj_users WHERE user_id=? LIMIT 1;";
    private static final String STATEMENT_GET_USER_BY_USERNAME =
            "SELECT * FROM jj_users WHERE user_name=? LIMIT 1;";
    private static final String STATEMENT_GET_ALL_USERS =
            "SELECT * FROM jj_users LIMIT 1024;";
    private static final String STATEMENT_ADD_NEW_USER =
            "INSERT INTO jj_users(user_name, user_pass, user_email, "
            + "user_home_page_url, display_name, register_time) "
            + "VALUES (?, PASSWORD(?), ?, ?, ?, ?);";
    private static final String STATEMENT_ADD_NEW_USER_WITHOUT_HOMEPAGE =
            "INSERT INTO jj_users(user_name, user_pass, user_email,  "
            + "display_name, register_time) VALUES (?, PASSWORD(?), ?, ?, ?);";
    private static final String STATEMENT_UPDATE_USER_EXCEPT_PASS =
            "UPDATE jj_users SET user_name=?, "
            + "user_email=?, user_home_page_url=?, display_name=? "
            + "WHERE user_id=?;";
    private static final String STATEMENT_USERNAME_USED =
            "SELECT COUNT(*) FROM jj_users WHERE user_name=? LIMIT 1;";
    private static final String STATEMENT_CHANGE_USERNAME =
            "UPDATE jj_users SET user_name=? WHERE user_id=?;";
    private static final String STATEMENT_CHANGE_PASSWORD =
            "UPDATE jj_users SET user_pass=PASSWORD(?) WHERE user_id=?;";
    private static final String STATEMENT_SET_LAST_LOGIN_TIME =
            "UPDATE jj_users SET last_login_time=? WHERE user_id=?;";
    private static final String STATEMENT_DELETE_USER_BY_ID =
            "DELETE FROM jj_users WHERE user_id=?;";
    private static final String STATEMENT_DELETE_USER_FROM_BLOG_POST =
            "DELETE FROM jj_blog_posts WHERE author_user_id=?;";
    private AdminUserMetaDataAccess adminUserMetaDataAccess;

    private AdminUser getAdminUserByResultSet(ResultSet rs)
            throws SQLException {
        long userId = rs.getLong(COLUMN_ID);
        String username = rs.getString(COLUMN_USERNAME);
        String password = rs.getString(COLUMN_PASSWORD);
        String email = rs.getString(COLUMN_EMAIL);
        String homePage = rs.getString(COLUMN_HOMEPAGE);
        String displayName = rs.getString(COLUMN_DISPLAY_NAME);
        Date registerTime = rs.getDate(COLUMN_REGISTER_TIME);
        Date lastLoginTime = rs.getDate(COLUMN_LAST_LOGIN_TIME);

        AdminUser user = new AdminUser();
        user.setUserId(userId);
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setHomePageUrl(homePage);
        user.setDisplayName(displayName);
        user.setRegisterTime(registerTime);
        user.setLastLoginTime(lastLoginTime);
        return user;
    }

    public AdminUserDataAccessImpl(DataSource dataSource) {
        super(dataSource);
        adminUserMetaDataAccess = new AdminUserMetaDataAccess(dataSource);
    }

    @Override
    public boolean confirm(String username, String password)
            throws DataAccessException {
        Connection connection = null;
        try {
            connection = newConnection();
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_CONFIRM);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return false;
            }

            long number = rs.getLong(1);
            return number >= 1;
        } catch (SQLException ex) {
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public AdminUser getUserIfConfirmed(String username, String password)
            throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_GET_USER);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return null;
            }

            AdminUser user = getAdminUserByResultSet(rs);
            adminUserMetaDataAccess.addUserProperties(user);
            return user;
        } catch (SQLException ex) {
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public AdminUser getUserByUsername(String username)
            throws DataAccessException {
        Connection connection = null;
        try {
            connection = newConnection();
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_GET_USER_BY_USERNAME);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return null;
            }

            AdminUser user = getAdminUserByResultSet(rs);
            adminUserMetaDataAccess.addUserProperties(user);
            return user;
        } catch (SQLException ex) {
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public AdminUser getUser(long userId) throws DataAccessException {
        Connection connection = null;
        try {
            connection = newConnection();
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_GET_USER_BY_ID);
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return null;
            }

            AdminUser user = getAdminUserByResultSet(rs);
            adminUserMetaDataAccess.addUserProperties(user);
            return user;
        } catch (SQLException ex) {
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public List<AdminUser> getAllUsers() throws DataAccessException {
        Connection connection = null;
        try {
            connection = newConnection();
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_GET_ALL_USERS);
            ResultSet rs = ps.executeQuery();

            List<AdminUser> adminUsers = new LinkedList<AdminUser>();
            while (rs.next()) {
                AdminUser user = getAdminUserByResultSet(rs);
                adminUserMetaDataAccess.addUserProperties(user);
                adminUsers.add(user);
            }
            return adminUsers;
        } catch (SQLException ex) {
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public AdminUser addNewUser(AdminUser user) throws DataAccessException {
        return (user.getHomePageUrl() == null)
                ? addNewUser(user.getUsername(), user.getPassword(),
                user.getEmail(), user.getDisplayName(), user.getRegisterTime())
                : addNewUser(user.getUsername(), user.getPassword(),
                user.getEmail(), user.getHomePageUrl(), user.getDisplayName(),
                user.getRegisterTime());
    }

    @Override
    public AdminUser addNewUser(String username, String password, String email,
            String displayName, Date registerTime) throws DataAccessException {
        Connection connection = null;
        try {
            connection = newConnection();
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_ADD_NEW_USER_WITHOUT_HOMEPAGE,
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, email);
            ps.setString(4, displayName);
            ps.setTimestamp(5, (registerTime == null)
                    ? null
                    : new Timestamp(registerTime.getTime()));
            int rowCount = ps.executeUpdate();

            if (rowCount <= 0) {
                return null;
            }

            ResultSet rs = ps.getGeneratedKeys();
            if (!rs.next()) {
                return null;
            }
            long userId = rs.getLong(1);
            return getUser(userId);
        } catch (SQLException ex) {
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public AdminUser addNewUser(String username, String password, String email,
            String homePageUrl, String displayName, Date registerTime)
            throws DataAccessException {
        Connection connection = null;
        try {
            connection = newConnection();
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_ADD_NEW_USER, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, email);
            ps.setString(4, homePageUrl);
            ps.setString(5, displayName);
            ps.setTimestamp(6, (registerTime == null)
                    ? null
                    : new Timestamp(registerTime.getTime()));
            int rowCount = ps.executeUpdate();

            if (rowCount <= 0) {
                return null;
            }

            ResultSet rs = ps.getGeneratedKeys();
            if (!rs.next()) {
                return null;
            }
            long userId = rs.getLong(1);
            return getUser(userId);
        } catch (SQLException ex) {
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public void updateUserExceptPassword(AdminUser user) throws DataAccessException {
        Connection connection = null;
        try {
            connection = newConnection();
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_UPDATE_USER_EXCEPT_PASS);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getHomePageUrl());
            ps.setString(4, user.getDisplayName());
            ps.setLong(5, user.getUserId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public boolean changeUserName(long userId, String username)
            throws DataAccessException {
        Connection connection = null;
        try {
            connection = newConnection();
            PreparedStatement ps0 = connection.prepareStatement(
                    STATEMENT_USERNAME_USED);
            ps0.setString(1, username);
            ResultSet rs = ps0.executeQuery();

            if (!rs.next()) {
                return false;
            }
            long number = rs.getLong(1);
            if (number > 0) {
                return false;
            }
            PreparedStatement ps1 = connection.prepareStatement(
                    STATEMENT_CHANGE_USERNAME);
            ps1.setString(1, username);
            ps1.setLong(2, userId);
            ps1.executeUpdate();
            return true;
        } catch (SQLException ex) {
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public void changePassword(long userId, String password)
            throws DataAccessException {
        Connection connection = null;
        try {
            connection = newConnection();
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_CHANGE_PASSWORD);
            ps.setString(1, password);
            ps.setLong(2, userId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public void setLastLoginTime(long userId, Date date)
            throws DataAccessException {
        Connection connection = null;
        try {
            connection = newConnection();
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_SET_LAST_LOGIN_TIME);
            ps.setTimestamp(1, new Timestamp(date.getTime()));
            ps.setLong(2, userId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public void deleteUser(long userId) throws DataAccessException {
        Connection connection = null;
        try {
            connection = newConnection();
            connection.setAutoCommit(false);
            // transaction begins
            PreparedStatement ps0 = connection.prepareStatement(
                    STATEMENT_DELETE_USER_BY_ID);
            ps0.setLong(1, userId);
            ps0.executeUpdate();

            PreparedStatement ps1 = connection.prepareStatement(
                    STATEMENT_DELETE_USER_FROM_BLOG_POST);
            ps1.setLong(1, userId);
            ps1.executeUpdate();

            adminUserMetaDataAccess.deleteUserPropertiesTransaction(connection,
                    userId);
            // transaction ends
            connection.commit();
        } catch (SQLException ex) {
            rollbackConnection(connection);
            throw new JdbcDataAccessException(ex);
        } catch (DataAccessException ex) {
            rollbackConnection(connection);
            throw ex;
        } finally {
            closeConnection(connection);
        }
    }

    public Map<String, List<String>> getUserProperties(long userId)
            throws DataAccessException {
        return adminUserMetaDataAccess.getUserPropertiesMap(userId);
    }

    public void addUserProperty(long userId, String key, String[] values)
            throws DataAccessException {
        adminUserMetaDataAccess.putUserProperties(userId, key, values);
    }

    public void deleteUserProperty(long userId, String key)
            throws DataAccessException {
        adminUserMetaDataAccess.deleteUserProperty(userId, key);
    }

    public void clearUserProperty(long userId) throws DataAccessException {
        adminUserMetaDataAccess.clearUserProperties(userId);
    }
}
