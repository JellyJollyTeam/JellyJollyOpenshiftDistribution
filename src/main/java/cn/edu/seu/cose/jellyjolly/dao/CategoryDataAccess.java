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

import cn.edu.seu.cose.jellyjolly.dto.Category;
import java.util.List;

/**
 *
 * @author rAy
 */
public interface CategoryDataAccess {

    public static enum CategoryOrderStrategy {

        ORDERED_BY_NAME_ASC, ORDERED_BY_NAME_DESC,
        ORDERED_BY_ID_ASC, ORDERED_BY_ID_DESC
    }

    Category getCategoryById(int categoryId) throws DataAccessException;

    int getCategoryNumber() throws DataAccessException;

    List<Category> getAllCategories() throws DataAccessException;

    List<Category> getAllCategories(CategoryOrderStrategy strategy)
            throws DataAccessException;

    Category createNewCategory(String name) throws DataAccessException;

    void updateCategoryNameById(int categoryId, String name)
            throws DataAccessException;

    void updateCategory(Category category) throws DataAccessException;

    void deleteCategoryById(int categoryId) throws DataAccessException;

    void deleteCategory(Category category) throws DataAccessException;
}
