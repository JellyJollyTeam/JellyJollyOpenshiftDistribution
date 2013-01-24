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

import cn.edu.seu.cose.jellyjolly.dao.DataAccessException;
import java.sql.SQLException;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
class JdbcDataAccessException extends DataAccessException {

    private static final String DEFAULT_HINT = "jdbc data access exception";

    public JdbcDataAccessException() {
        super(DEFAULT_HINT);
    }

    public JdbcDataAccessException(String hint) {
        super(hint);
    }

    public JdbcDataAccessException(SQLException e) {
        super(e);
    }

    public JdbcDataAccessException(String hint, SQLException e) {
        super(hint, e);
    }
}
