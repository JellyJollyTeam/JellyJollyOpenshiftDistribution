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
package cn.edu.seu.cose.jellyjolly.dao.jdbc;

import cn.edu.seu.cose.jellyjolly.dao.DataAccessException;
import cn.edu.seu.cose.jellyjolly.dao.InitializationDataAccess;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
public class InitializationDataAccessImpl extends AbstractDataAccess
        implements InitializationDataAccess {

    private static final Logger logger = Logger.getLogger(
            "InitializationDataAccessImpl");

    public InitializationDataAccessImpl(DataSource dataSource) {
        super(dataSource);
    }

    public boolean installed() throws DataAccessException {
        Connection connection = newConnection();
        try {
            Statement stmt = connection.createStatement();
            stmt.executeQuery("SELECT * FROM jellyjolly_schema.jj_blog_info");
            stmt.executeBatch();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    public void initialize(String username, String password,
            String displayName, String email, String title, String subtitle)
            throws DataAccessException {
        Connection connection = newConnection();
        try {
            connection.setAutoCommit(false);

            Statement stmt = connection.createStatement();
            doPretreatment(stmt);
            createUsersTable(stmt);
            createCategoriesTable(stmt);
            createPostsTable(stmt);
            createPostMetaTable(stmt);
            createInfoTable(stmt);
            createCommentsTable(stmt);
            createLinksTable(stmt);
            createUserMetaTable(stmt);
            createBlogMetaTable(stmt);
            createPagesTable(stmt);
            createDeleteUserTrigger(stmt);
            createUpdatePostTrigger(stmt);
            createDeletePostTrigger(stmt);
            doPostTreatment(stmt);
            stmt.executeBatch();

            initPersonalInfo(connection, username, password, displayName, email,
                    title, subtitle);

            connection.commit();
        } catch (SQLException ex) {
            rollbackConnection(connection);
            throw new JdbcDataAccessException(ex);
        }
    }

    private void doPretreatment(Statement stmt) throws SQLException {
        stmt.addBatch("SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, "
                + "UNIQUE_CHECKS=0;");
        stmt.addBatch("SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, "
                + "FOREIGN_KEY_CHECKS=0;");
        stmt.addBatch("SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE="
                + "'TRADITIONAL,ALLOW_INVALID_DATES';");
        stmt.addBatch("DROP SCHEMA IF EXISTS `jellyjolly_schema` ;");
        stmt.addBatch("CREATE SCHEMA IF NOT EXISTS `jellyjolly_schema` "
                + "DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;");
        stmt.addBatch("USE `jellyjolly_schema` ;");
    }

    private void createUsersTable(Statement stmt) throws SQLException {
        stmt.addBatch("DROP TABLE IF EXISTS `jellyjolly_schema`.`jj_users` ;");
        stmt.addBatch("CREATE  TABLE IF NOT EXISTS "
                + "`jellyjolly_schema`.`jj_users` ("
                + "  `user_id` BIGINT NOT NULL AUTO_INCREMENT ,"
                + "  `user_name` VARCHAR(60) NOT NULL ,"
                + "  `user_pass` VARCHAR(64) NOT NULL ,"
                + "  `user_email` VARCHAR(120) NOT NULL ,"
                + "  `user_home_page_url` VARCHAR(200) NULL ,"
                + "  `display_name` VARCHAR(120) CHARACTER SET 'utf8' "
                + "COLLATE 'utf8_general_ci' NOT NULL ,"
                + "  `register_time` DATETIME NOT NULL ,"
                + "  `last_login_time` DATETIME NULL ,"
                + "  PRIMARY KEY (`user_id`) ,\n"
                + "  UNIQUE INDEX `user_name_UNIQUE` (`user_name` ASC) )"
                + "ENGINE = MyISAM;");
    }

    private void createCategoriesTable(Statement stmt)
            throws SQLException {
        stmt.addBatch("DROP TABLE IF EXISTS "
                + "`jellyjolly_schema`.`jj_categories` ;");
        stmt.addBatch("CREATE  TABLE IF NOT EXISTS "
                + "`jellyjolly_schema`.`jj_categories` ("
                + "  `category_id` INT NOT NULL AUTO_INCREMENT ,"
                + "  `name` VARCHAR(45) CHARACTER SET 'utf8' "
                + "COLLATE 'utf8_general_ci' NOT NULL ,"
                + "  PRIMARY KEY (`category_id`) )"
                + "ENGINE = MyISAM;");
    }

    private void createPostsTable(Statement stmt) throws SQLException {
        stmt.addBatch("DROP TABLE IF EXISTS "
                + "`jellyjolly_schema`.`jj_blog_posts` ;");
        stmt.addBatch("CREATE  TABLE IF NOT EXISTS "
                + "`jellyjolly_schema`.`jj_blog_posts` ("
                + "  `blog_post_id` BIGINT NOT NULL AUTO_INCREMENT ,"
                + "  `author_user_id` BIGINT NOT NULL ,"
                + "  `category_id` INT NOT NULL ,"
                + "  `post_date` DATETIME NOT NULL ,"
                + "  `post_title` TEXT CHARACTER SET 'utf8' "
                + "COLLATE 'utf8_general_ci' NOT NULL ,"
                + "  `post_content` LONGTEXT CHARACTER SET 'utf8' "
                + "COLLATE 'utf8_general_ci' NULL ,"
                + "  PRIMARY KEY (`blog_post_id`) ,"
                + "  FULLTEXT INDEX `kw_post` "
                + "(`post_title` ASC, `post_content` ASC) )"
                + "ENGINE = MyISAM;");
    }

    private void createPostMetaTable(Statement stmt) throws SQLException {
        stmt.addBatch("DROP TABLE IF EXISTS "
                + "`jellyjolly_schema`.`jj_blog_post_meta` ;");
        stmt.addBatch("CREATE  TABLE IF NOT EXISTS "
                + "`jellyjolly_schema`.`jj_blog_post_meta` ("
                + "  `meta_id` INT NOT NULL AUTO_INCREMENT ,"
                + "  `blog_post_id` BIGINT NOT NULL ,"
                + "  `meta_key` VARCHAR(255) CHARACTER SET 'utf8' "
                + "COLLATE 'utf8_general_ci' NOT NULL ,"
                + "  `meta_value` LONGTEXT CHARACTER SET 'utf8' "
                + "COLLATE 'utf8_general_ci' NULL ,"
                + "  PRIMARY KEY (`meta_id`) )"
                + "ENGINE = MyISAM;");
    }

    private void createInfoTable(Statement stmt) throws SQLException {
        stmt.addBatch("DROP TABLE IF EXISTS "
                + "`jellyjolly_schema`.`jj_blog_info` ;");
        stmt.addBatch("CREATE  TABLE IF NOT EXISTS "
                + "`jellyjolly_schema`.`jj_blog_info` ("
                + "  `blog_id` INT NOT NULL AUTO_INCREMENT ,"
                + "  `blog_title` VARCHAR(45) CHARACTER SET 'utf8' "
                + "COLLATE 'utf8_general_ci' NOT NULL ,"
                + "  `blog_subtitle` TEXT CHARACTER SET 'utf8' "
                + "COLLATE 'utf8_general_ci' NULL ,"
                + "  `blog_url` VARCHAR(200) NULL ,"
                + "  PRIMARY KEY (`blog_id`) )"
                + "ENGINE = MyISAM;");
    }

    private void createCommentsTable(Statement stmt) throws SQLException {
        stmt.addBatch("DROP TABLE IF EXISTS "
                + "`jellyjolly_schema`.`jj_blog_comments` ;");
        stmt.addBatch("CREATE  TABLE IF NOT EXISTS "
                + "`jellyjolly_schema`.`jj_blog_comments` ("
                + "  `comment_id` BIGINT NOT NULL AUTO_INCREMENT ,"
                + "  `parent_comment_id` BIGINT NOT NULL ,"
                + "  `blog_post_id` BIGINT NOT NULL ,"
                + "  `comment_user_author_id` BIGINT NULL ,"
                + "  `comment_author_name` VARCHAR(120) CHARACTER SET 'utf8' "
                + "COLLATE 'utf8_general_ci' NOT NULL ,"
                + "  `comment_author_email` VARCHAR(120) NOT NULL ,"
                + "  `comment_author_home_page_url` VARCHAR(200) NULL ,"
                + "  `comment_date` DATETIME NOT NULL ,"
                + "  `comment_content` TEXT CHARACTER SET 'utf8' "
                + "COLLATE 'utf8_unicode_ci' NOT NULL ,"
                + "  PRIMARY KEY (`comment_id`) )"
                + "ENGINE = MyISAM;");
    }

    private void createLinksTable(Statement stmt) throws SQLException {
        stmt.addBatch("DROP TABLE IF EXISTS `jellyjolly_schema`.`jj_links` ;");
        stmt.addBatch("CREATE  TABLE IF NOT EXISTS "
                + "`jellyjolly_schema`.`jj_links` ("
                + "  `link_id` BIGINT NOT NULL AUTO_INCREMENT ,"
                + "  `link_title` VARCHAR(45) CHARACTER SET 'utf8' "
                + "COLLATE 'utf8_general_ci' NOT NULL ,"
                + "  `link_image` VARCHAR(255) NULL ,"
                + "  `link_url` VARCHAR(200) NOT NULL ,"
                + "  PRIMARY KEY (`link_id`) )"
                + "ENGINE = MyISAM;");
    }

    private void createUserMetaTable(Statement stmt)
            throws SQLException {
        stmt.addBatch("DROP TABLE IF EXISTS "
                + "`jellyjolly_schema`.`jj_user_meta` ;");
        stmt.addBatch("CREATE  TABLE IF NOT EXISTS "
                + "`jellyjolly_schema`.`jj_user_meta` ("
                + "  `user_meta_id` BIGINT NOT NULL AUTO_INCREMENT ,"
                + "  `user_id` BIGINT NOT NULL ,"
                + "  `meta_key` VARCHAR(255) CHARACTER SET 'utf8' "
                + "COLLATE 'utf8_general_ci' NOT NULL ,"
                + "  `meta_value` TEXT CHARACTER SET 'utf8' "
                + "COLLATE 'utf8_general_ci' NOT NULL ,"
                + "  PRIMARY KEY (`user_meta_id`) )"
                + "ENGINE = MyISAM;");
    }

    private void createBlogMetaTable(Statement stmt) throws SQLException {
        stmt.addBatch("DROP TABLE IF EXISTS "
                + "`jellyjolly_schema`.`jj_blog_meta` ;");
        stmt.addBatch("CREATE  TABLE IF NOT EXISTS "
                + "`jellyjolly_schema`.`jj_blog_meta` ("
                + "  `blog_meta_id` BIGINT NOT NULL AUTO_INCREMENT ,"
                + "  `meta_key` VARCHAR(255) CHARACTER SET 'utf8' "
                + "COLLATE 'utf8_general_ci' NOT NULL ,"
                + "  `meta_value` LONGTEXT CHARACTER SET 'utf8' "
                + "COLLATE 'utf8_general_ci' NULL ,"
                + "  PRIMARY KEY (`blog_meta_id`) )"
                + "ENGINE = MyISAM;");
    }

    private void createPagesTable(Statement stmt) throws SQLException {
        stmt.addBatch("DROP TABLE IF EXISTS "
                + "`jellyjolly_schema`.`jj_blog_pages` ;");
        stmt.addBatch("CREATE  TABLE IF NOT EXISTS "
                + "`jellyjolly_schema`.`jj_blog_pages` ("
                + "  `blog_page_id` INT NOT NULL AUTO_INCREMENT ,"
                + "  `page_title` VARCHAR(45) CHARACTER SET 'utf8' "
                + "COLLATE 'utf8_general_ci' NOT NULL ,"
                + "  `page_content` LONGTEXT CHARACTER SET 'utf8' "
                + "COLLATE 'utf8_general_ci' NOT NULL ,"
                + "  PRIMARY KEY (`blog_page_id`) )"
                + "ENGINE = MyISAM;");
    }

    private void createDeleteUserTrigger(Statement stmt)
            throws SQLException {
        stmt.addBatch("DROP TRIGGER IF EXISTS "
                + "`jellyjolly_schema`.`delete_user`;");
        stmt.addBatch("CREATE TRIGGER delete_user\n"
                + "AFTER DELETE\n"
                + "ON jj_users\n"
                + "FOR EACH ROW\n"
                + "BEGIN\n"
                + "	DELETE FROM jj_blog_posts\n"
                + "WHERE author_user_id=OLD.user_id;\n"
                + "END;");
    }

    private void createUpdatePostTrigger(Statement stmt)
            throws SQLException {
        stmt.addBatch("DROP TRIGGER IF EXISTS "
                + "`jellyjolly_schema`.`update_post`;");
        stmt.addBatch("CREATE TRIGGER update_post\n"
                + "AFTER UPDATE\n"
                + "ON jj_blog_posts\n"
                + "FOR EACH ROW\n"
                + "BEGIN\n"
                + "	IF (SELECT COUNT(1) FROM jj_blog_posts "
                + "WHERE category_id=OLD.category_id) <= 0 THEN\n"
                + "		DELETE FROM jj_categories "
                + "WHERE category_id=OLD.category_id;\n"
                + "	END IF;\n"
                + "END;");
    }

    private void createDeletePostTrigger(Statement stmt)
            throws SQLException {
        stmt.addBatch("DROP TRIGGER IF EXISTS "
                + "`jellyjolly_schema`.`delete_post`;");
        stmt.addBatch("CREATE TRIGGER delete_post\n"
                + "AFTER DELETE\n"
                + "ON jj_blog_posts\n"
                + "FOR EACH ROW\n"
                + "BEGIN\n"
                + "	## delete comments to the post\n"
                + "	DELETE FROM jj_blog_comments "
                + "WHERE blog_post_id=OLD.blog_post_id;\n"
                + "	## delete the unused category\n"
                + "	IF (SELECT COUNT(1) FROM jj_blog_posts "
                + "WHERE category_id=OLD.category_id) <= 0 THEN\n"
                + "		DELETE FROM jj_categories "
                + "WHERE category_id=OLD.category_id;\n"
                + "	END IF;\n"
                + "END;");
    }

    private void doPostTreatment(Statement stmt) throws SQLException {
        stmt.addBatch("SET SQL_MODE=@OLD_SQL_MODE;");
        stmt.addBatch("SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;");
        stmt.addBatch("SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;");
    }

    private void initPersonalInfo(Connection connection, String username,
            String password, String displayName, String email, String title,
            String subtitle) throws SQLException {
        PreparedStatement ps1 = connection.prepareStatement(
                "INSERT INTO jj_blog_info (blog_title, blog_subtitle) "
                + "VALUES (?, ?)");
        ps1.setString(1, title);
        ps1.setString(2, subtitle);
        ps1.executeUpdate();

        Date now = new Date();
        PreparedStatement ps2 = connection.prepareStatement(
                "INSERT INTO jj_users(user_name, user_pass, user_email,  "
                + "display_name, register_time) "
                + "VALUES (?, PASSWORD(?), ?, ?, ?);");
        ps2.setString(1, username);
        ps2.setString(2, password);
        ps2.setString(3, email);
        ps2.setString(4, displayName);
        ps2.setDate(5, new java.sql.Date(now.getTime()));
        ps2.executeUpdate();
    }
}
