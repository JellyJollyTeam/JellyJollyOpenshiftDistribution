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

import cn.edu.seu.cose.jellyjolly.dao.AdminUserDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.BlogInfoDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.BlogPageDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.BlogPostDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.CategoryDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.CommentDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.DataAccessFactory;
import cn.edu.seu.cose.jellyjolly.dao.LinkDataAccess;
import javax.sql.DataSource;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
public class MysqlDataAccessFactory implements DataAccessFactory {

    private DataSource dataSource;

    public MysqlDataAccessFactory(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public BlogInfoDataAccess getBlogInfoDataAccess() {
        return new BlogInfoDataAccessImpl(dataSource);
    }

    @Override
    public BlogPostDataAccess getBlogPostDataAccess() {
        return new BlogPostDataAccessImpl(dataSource);
    }

    @Override
    public CategoryDataAccess getCategoryDataAccess() {
        return new CategoryDataAccessImpl(dataSource);
    }

    @Override
    public CommentDataAccess getCommentDataAccess() {
        return new CommentDataAccessImpl(dataSource);
    }

    @Override
    public LinkDataAccess getLinkDataAccess() {
        return new LinkDataAccessImpl(dataSource);
    }

    @Override
    public AdminUserDataAccess getAdminUserDataAccess() {
        return new AdminUserDataAccessImpl(dataSource);
    }

    @Override
    public BlogPageDataAccess getBlogPageDataAccess() {
        return new BlogPageDataAccessImpl(dataSource);
    }
}
