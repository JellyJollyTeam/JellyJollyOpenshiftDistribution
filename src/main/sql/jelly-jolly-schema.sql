SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

DROP SCHEMA IF EXISTS `jellyjolly_schema` ;
CREATE SCHEMA IF NOT EXISTS `jellyjolly_schema` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `jellyjolly_schema` ;

-- -----------------------------------------------------
-- Table `jellyjolly_schema`.`jj_users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `jellyjolly_schema`.`jj_users` ;

CREATE  TABLE IF NOT EXISTS `jellyjolly_schema`.`jj_users` (
  `user_id` BIGINT NOT NULL AUTO_INCREMENT ,
  `user_name` VARCHAR(60) NOT NULL ,
  `user_pass` VARCHAR(64) NOT NULL ,
  `user_email` VARCHAR(120) NOT NULL ,
  `user_home_page_url` VARCHAR(200) NULL ,
  `display_name` VARCHAR(120) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL ,
  `register_time` DATETIME NOT NULL ,
  `last_login_time` DATETIME NULL ,
  PRIMARY KEY (`user_id`) ,
  UNIQUE INDEX `user_name_UNIQUE` (`user_name` ASC) )
ENGINE = MyISAM;


-- -----------------------------------------------------
-- Table `jellyjolly_schema`.`jj_categories`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `jellyjolly_schema`.`jj_categories` ;

CREATE  TABLE IF NOT EXISTS `jellyjolly_schema`.`jj_categories` (
  `category_id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL ,
  PRIMARY KEY (`category_id`) )
ENGINE = MyISAM;


-- -----------------------------------------------------
-- Table `jellyjolly_schema`.`jj_blog_posts`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `jellyjolly_schema`.`jj_blog_posts` ;

CREATE  TABLE IF NOT EXISTS `jellyjolly_schema`.`jj_blog_posts` (
  `blog_post_id` BIGINT NOT NULL AUTO_INCREMENT ,
  `author_user_id` BIGINT NOT NULL ,
  `category_id` INT NOT NULL ,
  `post_date` DATETIME NOT NULL ,
  `post_title` TEXT CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL ,
  `post_content` LONGTEXT CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NULL ,
  PRIMARY KEY (`blog_post_id`) ,
  FULLTEXT INDEX `kw_post` (`post_title` ASC, `post_content` ASC) )
ENGINE = MyISAM;


-- -----------------------------------------------------
-- Table `jellyjolly_schema`.`jj_blog_post_meta`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `jellyjolly_schema`.`jj_blog_post_meta` ;

CREATE  TABLE IF NOT EXISTS `jellyjolly_schema`.`jj_blog_post_meta` (
  `meta_id` INT NOT NULL AUTO_INCREMENT ,
  `blog_post_id` BIGINT NOT NULL ,
  `meta_key` VARCHAR(255) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL ,
  `meta_value` LONGTEXT CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NULL ,
  PRIMARY KEY (`meta_id`) )
ENGINE = MyISAM;


-- -----------------------------------------------------
-- Table `jellyjolly_schema`.`jj_blog_info`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `jellyjolly_schema`.`jj_blog_info` ;

CREATE  TABLE IF NOT EXISTS `jellyjolly_schema`.`jj_blog_info` (
  `blog_id` INT NOT NULL AUTO_INCREMENT ,
  `blog_title` VARCHAR(45) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL ,
  `blog_subtitle` TEXT CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NULL ,
  `blog_url` VARCHAR(200) NULL ,
  PRIMARY KEY (`blog_id`) )
ENGINE = MyISAM;


-- -----------------------------------------------------
-- Table `jellyjolly_schema`.`jj_blog_comments`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `jellyjolly_schema`.`jj_blog_comments` ;

CREATE  TABLE IF NOT EXISTS `jellyjolly_schema`.`jj_blog_comments` (
  `comment_id` BIGINT NOT NULL AUTO_INCREMENT ,
  `parent_comment_id` BIGINT NOT NULL ,
  `blog_post_id` BIGINT NOT NULL ,
  `comment_user_author_id` BIGINT NULL ,
  `comment_author_name` VARCHAR(120) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL ,
  `comment_author_email` VARCHAR(120) NOT NULL ,
  `comment_author_home_page_url` VARCHAR(200) NULL ,
  `comment_date` DATETIME NOT NULL ,
  `comment_content` TEXT CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NOT NULL ,
  PRIMARY KEY (`comment_id`) )
ENGINE = MyISAM;


-- -----------------------------------------------------
-- Table `jellyjolly_schema`.`jj_links`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `jellyjolly_schema`.`jj_links` ;

CREATE  TABLE IF NOT EXISTS `jellyjolly_schema`.`jj_links` (
  `link_id` BIGINT NOT NULL AUTO_INCREMENT ,
  `link_title` VARCHAR(45) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL ,
  `link_image` VARCHAR(255) NULL ,
  `link_url` VARCHAR(200) NOT NULL ,
  PRIMARY KEY (`link_id`) )
ENGINE = MyISAM;


-- -----------------------------------------------------
-- Table `jellyjolly_schema`.`jj_user_meta`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `jellyjolly_schema`.`jj_user_meta` ;

CREATE  TABLE IF NOT EXISTS `jellyjolly_schema`.`jj_user_meta` (
  `user_meta_id` BIGINT NOT NULL AUTO_INCREMENT ,
  `user_id` BIGINT NOT NULL ,
  `meta_key` VARCHAR(255) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL ,
  `meta_value` TEXT CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL ,
  PRIMARY KEY (`user_meta_id`) )
ENGINE = MyISAM;


-- -----------------------------------------------------
-- Table `jellyjolly_schema`.`jj_blog_meta`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `jellyjolly_schema`.`jj_blog_meta` ;

CREATE  TABLE IF NOT EXISTS `jellyjolly_schema`.`jj_blog_meta` (
  `blog_meta_id` BIGINT NOT NULL AUTO_INCREMENT ,
  `meta_key` VARCHAR(255) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL ,
  `meta_value` LONGTEXT CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NULL ,
  PRIMARY KEY (`blog_meta_id`) )
ENGINE = MyISAM;


-- -----------------------------------------------------
-- Table `jellyjolly_schema`.`jj_blog_pages`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `jellyjolly_schema`.`jj_blog_pages` ;

CREATE  TABLE IF NOT EXISTS `jellyjolly_schema`.`jj_blog_pages` (
  `blog_page_id` INT NOT NULL AUTO_INCREMENT ,
  `page_title` VARCHAR(45) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL ,
  `page_content` LONGTEXT CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL ,
  PRIMARY KEY (`blog_page_id`) )
ENGINE = MyISAM;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `jellyjolly_schema`.`jj_categories`
-- -----------------------------------------------------
START TRANSACTION;
USE `jellyjolly_schema`;
INSERT INTO `jellyjolly_schema`.`jj_categories` (`category_id`, `name`) VALUES (1, '默认');

COMMIT;

-- -----------------------------------------------------
-- Data for table `jellyjolly_schema`.`jj_blog_info`
-- -----------------------------------------------------
START TRANSACTION;
USE `jellyjolly_schema`;
INSERT INTO `jellyjolly_schema`.`jj_blog_info` (`blog_id`, `blog_title`, `blog_subtitle`, `blog_url`) VALUES (1, '我的博客', '欢迎使用Jelly Jolly博客引擎', NULL);
COMMIT;
