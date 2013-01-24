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

import cn.edu.seu.cose.jellyjolly.dao.CategoryDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.DataAccessException;
import cn.edu.seu.cose.jellyjolly.model.Category;
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
class CategoryDataAccessImpl
        extends AbstractDataAccess implements CategoryDataAccess {

    private static final CategoryOrderStrategy DEFAULT_STRATEGY =
            CategoryOrderStrategy.ORDERED_BY_NAME_DESC;
    private static final String COLUMN_CATEGORY_ID = "category_id";
    private static final String COLUMN_CATEGORY_NAME = "name";
    private static final String STATEMENT_GET_CATEGORY_BY_ID =
            "SELECT * FROM jj_categories WHERE category_id=?;";
    private static final String STATEMENT_GET_CATEGORIES =
            "SELECT * FROM jj_categories;";
    private static final String STATEMENT_GET_CATEGORIES_ORDERED_BY_NAME_ASC =
            "SELECT * FROM jj_categories ORDER BY name ASC;";
    private static final String STATEMENT_GET_CATEGORIES_ORDERED_BY_NAME_DESC =
            "SELECT * FROM jj_categories ORDER BY name DESC;";
    private static final String STATEMENT_GET_CATEGORIES_ORDERED_BY_ID_ASC =
            "SELECT * FROM jj_categories ORDER BY category_id ASC;";
    private static final String STATEMENT_GET_CATEGORIES_ORDERED_BY_ID_DESC =
            "SELECT * FROM jj_categories ORDER BY category_id DESC;";
    private static final String STATEMENT_CREATE_CATEGORY =
            "INSERT INTO jj_categories(name) VALUES (?);";
    private static final String STATEMENT_UPDATE_CATEGORY =
            "UPDATE jj_categories SET name=? WHERE category_id=?;";
    private static final String STATEMENT_DELETE_CATEGORY_BY_ID =
            "DELETE FROM jj_categories WHERE category_id=?;";
    private static final String STATEMENT_GET_CATEGORY_NUM =
            "SELECT COUNT(1) FROM jj_categories;";
    private static final Logger logger = Logger.getLogger(
            CategoryDataAccessImpl.class.getName());

    private static Category getCategoryByResultSet(ResultSet rs)
            throws SQLException {
        int id = rs.getInt(COLUMN_CATEGORY_ID);
        String name = rs.getString(COLUMN_CATEGORY_NAME);
        Category category = new Category();
        category.setCategoryId(id);
        category.setName(name);
        return category;
    }

    public CategoryDataAccessImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Category getCategoryById(int categoryId)
            throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_GET_CATEGORY_BY_ID);
            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return null;
            }

            Category category = getCategoryByResultSet(rs);
            return category;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public List<Category> getAllCategories() throws DataAccessException {
        return getAllCategories(DEFAULT_STRATEGY);
    }

    @Override
    public List<Category> getAllCategories(
            final CategoryOrderStrategy strategy) throws DataAccessException {
        Connection connection = newConnection();
        try {
            String statement;
            switch (strategy) {
                case ORDERED_BY_NAME_ASC:
                    statement = STATEMENT_GET_CATEGORIES_ORDERED_BY_NAME_ASC;
                    break;
                case ORDERED_BY_NAME_DESC:
                    statement = STATEMENT_GET_CATEGORIES_ORDERED_BY_NAME_DESC;
                    break;
                case ORDERED_BY_ID_ASC:
                    statement = STATEMENT_GET_CATEGORIES_ORDERED_BY_ID_ASC;
                    break;
                case ORDERED_BY_ID_DESC:
                    statement = STATEMENT_GET_CATEGORIES_ORDERED_BY_ID_DESC;
                    break;
                default:
                    statement = STATEMENT_GET_CATEGORIES;
                    break;
            }
            PreparedStatement ps = connection.prepareStatement(statement);
            ResultSet rs = ps.executeQuery();
            List<Category> categoryList = new LinkedList<Category>();
            while (rs.next()) {
                Category category = getCategoryByResultSet(rs);
                categoryList.add(category);
            }
            return categoryList;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public Category createNewCategory(String name)
            throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_CREATE_CATEGORY,
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            int rowCount = ps.executeUpdate();

            if (rowCount <= 0) {
                return null;
            }

            ResultSet rs = ps.getGeneratedKeys();
            if (!rs.next()) {
                return null;
            }

            int id = rs.getInt(1);
            Category category = new Category();
            category.setCategoryId(id);
            category.setName(name);
            return category;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public void updateCategoryNameById(int category, String name)
            throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_UPDATE_CATEGORY);
            ps.setString(1, name);
            ps.setInt(2, category);
            ps.executeUpdate();
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public void updateCategory(Category category) throws DataAccessException {
        updateCategoryNameById(category.getCategoryId(), category.getName());
    }

    @Override
    public void deleteCategoryById(int category) throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_DELETE_CATEGORY_BY_ID);
            ps.setInt(1, category);
            ps.executeUpdate();
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public void deleteCategory(Category category) throws DataAccessException {
        deleteCategoryById(category.getCategoryId());
    }

    public int getCategoryNumber() throws DataAccessException {
        Connection connection = newConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    STATEMENT_GET_CATEGORY_NUM);
            ResultSet rs = ps.executeQuery();
            return (rs.next()) ? rs.getInt(1) : 0;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JdbcDataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }
}
