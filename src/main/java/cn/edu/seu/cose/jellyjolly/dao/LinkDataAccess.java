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

import cn.edu.seu.cose.jellyjolly.dto.Link;
import java.util.List;

/**
 *
 * @author rAy
 */
public interface LinkDataAccess {

    public static enum LinkOrderStrategy {

        ORDERED_BY_NAME_ASC, ORDERED_BY_NAME_DESC,
        ORDERED_BY_ID_ASC, ORDERED_BY_ID_DESC
    }

    Link getLinkById(long linkId) throws DataAccessException;

    List<Link> getAllLinks() throws DataAccessException;

    long getLinkNumber() throws DataAccessException;

    long createNewLink(Link link) throws DataAccessException;

    long createNewLink(String title, String url) throws DataAccessException;

    long createNewLink(String title, String image, String url)
            throws DataAccessException;

    void updateLink(Link link) throws DataAccessException;

    void deleteLink(long linkId) throws DataAccessException;
}
