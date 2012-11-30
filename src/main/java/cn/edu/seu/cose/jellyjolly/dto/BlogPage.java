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

package cn.edu.seu.cose.jellyjolly.dto;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
public class BlogPage {

    private int blogPageId;

    private String pageTitle;

    private String pageContent;

    public void setBlogPageId(int blogPageId) {
        this.blogPageId = blogPageId;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public void setPageContent(String pageContent) {
        this.pageContent = pageContent;
    }

    public int getBlogPageId() {
        return blogPageId;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public String getPageContent() {
        return pageContent;
    }

}
