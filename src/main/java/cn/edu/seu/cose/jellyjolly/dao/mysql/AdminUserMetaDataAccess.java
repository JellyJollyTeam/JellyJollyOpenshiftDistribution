/*
 * Copyright (C) 2012 rAy <predator.ray@gmail.com>
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

import cn.edu.seu.cose.jellyjolly.model.AdminUser;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
class AdminUserMetaDataAccess extends AbstractDataAccess {

    private static final String COLUMN_META_KEY = "meta_key";
    private static final String COLUMN_META_VALUE = "meta_value";
    private static final String STATEMENT_DELETE_USER_FROM_USER_META =
            "DELETE FROM jj_user_meta WHERE user_id=?;";
    private static final String STATEMENT_DELETE_KEY_FROM_USER_META =
            "DELETE FROM jj_user_meta WHERE user_id=? AND meta_key=?;";
    private static final String STATEMENT_GET_USER_META =
            "SELECT * FROM jj_user_meta WHERE user_id=?;";
    private static final String STATEMENT_PUT_USER_META =
            "INSERT INTO jj_user_meta(user_id, meta_key, meta_value) "
            + "VALUES (?, ?, ?);";
    private static final Logger logger = Logger.getLogger(
            AdminUserMetaDataAccess.class.getName());

    public AdminUserMetaDataAccess(DataSource dataSource) {
        super(dataSource);
    }

    public Map<String, List<String>> getUserPropertiesMap(long userId)
            throws JdbcDataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_GET_USER_META);
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            Map<String, List<String>> keyValueMap =
                    new LinkedHashMap<String, List<String>>();
            while (rs.next()) {
                String key = rs.getString(COLUMN_META_KEY);
                String value = rs.getString(COLUMN_META_VALUE);
                if (!keyValueMap.containsKey(key)) {
                    keyValueMap.put(key, new LinkedList<String>());
                }

                List<String> list = keyValueMap.get(key);
                list.add(value);
            }
            return keyValueMap;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    public void addUserProperties(AdminUser user)
            throws JdbcDataAccessException {
        Map<String, List<String>> keyValueMap =
                getUserPropertiesMap(user.getUserId());
        for (Map.Entry<String, List<String>> e : keyValueMap.entrySet()) {
            String key = e.getKey();
            List<String> value = e.getValue();
            user.setOtherProperty(key, value.toArray(new String[0]));
        }
    }

    public void deleteUserProperty(long userId, String key)
            throws JdbcDataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_DELETE_KEY_FROM_USER_META);
            ps.setLong(1, userId);
            ps.setString(2, key);
            ps.executeUpdate();
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    public void clearUserProperties(long userId)
            throws JdbcDataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_DELETE_USER_FROM_USER_META);
            ps.setLong(1, userId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    public void deleteUserPropertiesTransaction(Connection connection,
            long userId) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(
                STATEMENT_DELETE_USER_FROM_USER_META);
        ps.setLong(1, userId);
        ps.executeUpdate();
    }

    public void putUserProperties(long userId, String key, String[] values)
            throws JdbcDataAccessException {
        Connection connection = newConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_PUT_USER_META);
            for (String value : values) {
                ps.setLong(1, userId);
                ps.setString(2, key);
                ps.setString(3, value);
                ps.executeUpdate();
            }
            connection.commit();
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            rollbackConnection(connection);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }
}
