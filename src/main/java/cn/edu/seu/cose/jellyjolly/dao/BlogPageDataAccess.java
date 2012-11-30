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

import cn.edu.seu.cose.jellyjolly.dto.BlogPage;
import cn.edu.seu.cose.jellyjolly.dto.BlogPageBar;
import java.util.List;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
public interface BlogPageDataAccess {

    List<String> getPageTitleList() throws DataAccessException;

    List<BlogPageBar> getPageBarList() throws DataAccessException;

    List<BlogPage> getAllPages() throws DataAccessException;

    int getPageCount() throws DataAccessException;

    BlogPage getPage(int pageId) throws DataAccessException;

    int addNewPage(BlogPage page) throws DataAccessException;

    int addNewPage(String title, String content) throws DataAccessException;

    void changeTitle(int pageId, String title) throws DataAccessException;

    void changeContent(int pageId, String content) throws DataAccessException;

    void editPage(BlogPage page) throws DataAccessException;

    void deletePage(int pageId) throws DataAccessException;
}
