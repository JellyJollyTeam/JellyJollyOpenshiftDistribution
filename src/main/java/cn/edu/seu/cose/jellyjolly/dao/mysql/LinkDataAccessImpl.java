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
import cn.edu.seu.cose.jellyjolly.dao.LinkDataAccess;
import cn.edu.seu.cose.jellyjolly.model.Link;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
class LinkDataAccessImpl extends AbstractDataAccess implements LinkDataAccess {

    private static final String COLUMN_LINK_ID = "link_id";
    private static final String COLUMN_LINK_TITLE = "link_title";
    private static final String COLUMN_LINK_IMAGE = "link_image";
    private static final String COLUMN_LINK_URL = "link_url";
    private static final String STATEMENT_GET_LINK_BY_ID =
            "SELECT * FROM jj_links WHERE link_id=?;";
    private static final String STATEMENT_GET_LINKS =
            "SELECT * FROM jj_links;";
    private static final String STATEMENT_GET_LINK_NUM =
            "SELECT COUNT(1) FROM jj_links;";
    private static final String STATEMENT_CREATE_LINK =
            "INSERT INTO jj_links(link_title, link_image, link_url) VALUES (?, ?, ?);";
    private static final String STATEMENT_CREATE_LINK_WITHOUT_IMAGE =
            "INSERT INTO jj_links(link_title, link_url) VALUES (?, ?);";
    private static final String STATEMENT_UPDATE_LINK =
            "UPDATE jj_links SET link_title=?, link_image=?, link_url=? "
            + "WHERE link_id=?;";
    private static final String STATEMENT_DELETE_LINK_BY_ID =
            "DELETE FROM jj_links WHERE link_id=?;";

    private static Link getLinkByResultSet(ResultSet rs) throws SQLException {
        long id = rs.getLong(COLUMN_LINK_ID);
        String title = rs.getString(COLUMN_LINK_TITLE);
        String image = rs.getString(COLUMN_LINK_IMAGE);
        String url = rs.getString(COLUMN_LINK_URL);

        Link link = new Link();
        link.setImage(image);
        link.setLinkId(id);
        link.setTitle(title);
        link.setUrl(url);
        return link;
    }
    private static final Logger logger = Logger.getLogger(
            LinkDataAccessImpl.class.getName());

    public LinkDataAccessImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Link getLinkById(long linkId) throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_GET_LINK_BY_ID);
            ps.setLong(1, linkId);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return null;
            }
            Link link = getLinkByResultSet(rs);
            return link;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public List<Link> getAllLinks() throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_GET_LINKS);
            ResultSet rs = ps.executeQuery();
            List<Link> links = new LinkedList<Link>();
            while (rs.next()) {
                Link link = getLinkByResultSet(rs);
                links.add(link);
            }
            return links;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    public long getLinkNumber() throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_GET_LINK_NUM);
            ResultSet rs = ps.executeQuery();
            return (rs.next()) ? rs.getLong(1) : 0;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public long createNewLink(Link link) throws DataAccessException {
        return (link.getImage() == null)
                ? createNewLink(link.getTitle(), link.getUrl())
                : createNewLink(link.getTitle(), link.getImage(), link.getUrl());
    }

    @Override
    public long createNewLink(String title, String url) throws DataAccessException {

        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_CREATE_LINK_WITHOUT_IMAGE, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, title);
            ps.setString(2, url);
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
    public long createNewLink(String title, String image, String url)
            throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_CREATE_LINK, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, title);
            ps.setString(2, image);
            ps.setString(3, url);
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
    public void updateLink(Link link) throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_UPDATE_LINK);
            ps.setString(1, link.getTitle());
            ps.setString(2, link.getImage());
            ps.setString(3, link.getUrl());
            ps.setLong(4, link.getLinkId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public void deleteLink(long linkId) throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_DELETE_LINK_BY_ID);
            ps.setLong(1, linkId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }
}
