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

import cn.edu.seu.cose.jellyjolly.dto.AdminUser;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
public interface AdminUserDataAccess {

    boolean confirm(String username, String password)
            throws DataAccessException;

    AdminUser getUserIfConfirmed(String username, String password)
            throws DataAccessException;

    AdminUser getUser(long userId) throws DataAccessException;

    List<AdminUser> getAllUsers() throws DataAccessException;

    Map<String, List<String>> getUserProperties(long userId)
            throws DataAccessException;

    AdminUser addNewUser(AdminUser user) throws DataAccessException;

    AdminUser addNewUser(String username, String password, String email,
            String displayName, Date registerTime) throws DataAccessException;

    AdminUser addNewUser(String username, String password, String email,
            String homePageUrl, String displayName, Date registerTime)
            throws DataAccessException;

    void addUserProperty(long userId, String key, String[] values)
            throws DataAccessException;

    void updateUser(AdminUser user) throws DataAccessException;

    boolean changeUserName(long userId, String username)
            throws DataAccessException;

    void changePassword(long userId, String password)
            throws DataAccessException;

    void setLastLoginTime(long userId, Date date)
            throws DataAccessException;

    void deleteUser(long userId) throws DataAccessException;

    void deleteUserProperty(long userId, String key)
            throws DataAccessException;

    void clearUserProperty(long userId) throws DataAccessException;
}
