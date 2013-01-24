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

import cn.edu.seu.cose.jellyjolly.dao.BlogPageDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.DataAccessException;
import cn.edu.seu.cose.jellyjolly.model.BlogPage;
import cn.edu.seu.cose.jellyjolly.model.BlogPageBar;
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
class BlogPageDataAccessImpl
        extends AbstractDataAccess implements BlogPageDataAccess {

    private static final String COLUMN_PAGE_ID = "blog_page_id";
    private static final String COLUMN_PAGE_TITLE = "page_title";
    private static final String COLUMN_PAGE_CONTENT = "page_content";
    private static final String STATEMENT_GET_PAGE_TITLE_LIST =
            "SELECT page_title FROM jj_blog_pages;";
    private static final String STATEMENT_GET_PAGE_TITLE_MAP =
            "SELECT blog_page_id, page_title FROM jj_blog_pages;";
    private static final String STATEMENT_GET_PAGES =
            "SELECT * FROM jj_blog_pages;";
    private static final String STATEMENT_GET_PAGE_BY_ID =
            "SELECT * FROM jj_blog_pages WHERE blog_page_id=?;";
    private static final String STATEMENT_ADD_PAGE =
            "INSERT INTO jj_blog_pages(page_title, page_content) "
            + "VALUES (?, ?);";
    private static final String STATEMENT_CHANGE_TITLE =
            "UPDATE jj_blog_pages SET page_title=? WHERE blog_page_id=?;";
    private static final String STATEMENT_CHANGE_CONTENT =
            "UPDATE jj_blog_pages SET page_content=? WHERE blog_page_id=?;";
    private static final String STATEMENT_EDIT_PAGE =
            "UPDATE jj_blog_pages SET page_title=?, page_content=? "
            + "WHERE blog_page_id=?;";
    private static final String STATEMENT_DELETE_PAGE =
            "DELETE FROM jj_blog_pages WHERE blog_page_id=?;";
    private static final String STATEMENT_GET_PAGE_COUNT =
            "SELECT COUNT(1) FROM jj_blog_pages;";
    private static final Logger logger = Logger.getLogger(
            BlogPageDataAccessImpl.class.getName());

    private static BlogPage getPageByResultSet(ResultSet rs)
            throws SQLException {
        int id = rs.getInt(COLUMN_PAGE_ID);
        String title = rs.getString(COLUMN_PAGE_TITLE);
        String content = rs.getString(COLUMN_PAGE_CONTENT);
        BlogPage page = new BlogPage();
        page.setBlogPageId(id);
        page.setPageTitle(title);
        page.setPageContent(content);
        return page;
    }

    public BlogPageDataAccessImpl(DataSource dataSource) {
        super(dataSource);
    }

    public List<String> getPageTitleList() throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_GET_PAGE_TITLE_LIST);
            ResultSet rs = ps.executeQuery();
            List<String> titleList = new LinkedList<String>();
            while (rs.next()) {
                String title = rs.getString(COLUMN_PAGE_TITLE);
                titleList.add(title);
            }
            return titleList;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    public List<BlogPageBar> getPageBarList() throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_GET_PAGE_TITLE_MAP);
            ResultSet rs = ps.executeQuery();
            List<BlogPageBar> barList = new LinkedList<BlogPageBar>();
            while (rs.next()) {
                int pageId = rs.getInt(COLUMN_PAGE_ID);
                String title = rs.getString(COLUMN_PAGE_TITLE);
                BlogPageBar bar = new BlogPageBar();
                bar.setBlogPageId(pageId);
                bar.setPageTitle(title);
                barList.add(bar);
            }
            return barList;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    public List<BlogPage> getAllPages() throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_GET_PAGES);
            ResultSet rs = ps.executeQuery();
            List<BlogPage> pageList = new LinkedList<BlogPage>();
            while (rs.next()) {
                BlogPage page = getPageByResultSet(rs);
                pageList.add(page);
            }
            return pageList;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    public BlogPage getPage(int pageId) throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_GET_PAGE_BY_ID);
            ps.setInt(1, pageId);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return null;
            }
            BlogPage page = getPageByResultSet(rs);
            return page;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    public int addNewPage(BlogPage page) throws DataAccessException {
        return addNewPage(page.getPageTitle(), page.getPageContent());
    }

    public int addNewPage(String title, String content) throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_ADD_PAGE, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, title);
            ps.setString(2, content);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            return (rs.next()) ? rs.getInt(1) : -1;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new DataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    public void changeTitle(int pageId, String title) throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_CHANGE_TITLE);
            ps.setString(1, title);
            ps.setInt(2, pageId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new DataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    public void changeContent(int pageId, String content) throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_CHANGE_CONTENT);
            ps.setString(1, content);
            ps.setInt(2, pageId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new DataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    public void editPage(BlogPage page) throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_EDIT_PAGE);
            ps.setString(1, page.getPageTitle());
            ps.setString(2, page.getPageContent());
            ps.setInt(3, page.getBlogPageId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new DataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    public void deletePage(int pageId) throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_DELETE_PAGE);
            ps.setInt(1, pageId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new DataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    public int getPageCount() throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_GET_PAGE_COUNT);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return 0;
            }

            return rs.getInt(1);
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }
}
