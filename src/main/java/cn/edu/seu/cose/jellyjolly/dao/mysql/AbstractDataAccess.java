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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
abstract class AbstractDataAccess {

    private static final Logger parentLogger = Logger.getLogger(
            AbstractDataAccess.class.getName());
    private DataSource dataSource;

    AbstractDataAccess(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected Connection newConnection()
            throws JdbcDataAccessException {
        try {
            return dataSource.getConnection();
        } catch (SQLException ex) {
            parentLogger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        }
    }

    protected void closeConnection(Connection connection)
            throws JdbcDataAccessException {
        try {
            if (connection.isClosed()) {
                parentLogger.log(Level.WARNING, "Attempting to close "
                        + "a connection that is already closed.");
            } else {
                connection.close();
            }
        } catch (SQLException ex) {
            parentLogger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        }
    }

    protected void rollbackConnection(Connection connection)
            throws JdbcDataAccessException {
        try {
            connection.rollback();
        } catch (SQLException ex) {
            parentLogger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        }
    }
}
