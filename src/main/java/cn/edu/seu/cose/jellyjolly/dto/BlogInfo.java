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

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author rAy
 */
public class BlogInfo {

    private int blogId;

    private String blogTitle;

    private String blogSubTitle;

    private String blogUrl;

    private Map<String, String[]> otherProperties;

    public BlogInfo() {
        otherProperties = new HashMap<String, String[]>();
    }

    public int getBlogId() {
        return blogId;
    }

    public void setBlogId(int blogId) {
        this.blogId = blogId;
    }

    public String getBlogTitle() {
        return blogTitle;
    }

    public void setBlogTitle(String blogTitle) {
        this.blogTitle = blogTitle;
    }

    public String getBlogSubTitle() {
        return blogSubTitle;
    }

    public void setBlogSubTitle(String blogSubTitle) {
        this.blogSubTitle = blogSubTitle;
    }

    public String getBlogUrl() {
        return blogUrl;
    }

    public void setBlogUrl(String blogUrl) {
        this.blogUrl = blogUrl;
    }

    public String[] getOtherProperty(String name) {
        return otherProperties.get(name);
    }

    public void setOtherProperty(String name, String[] property) {
        otherProperties.put(name, property);
    }

    public Map<String, String[]> getOtherProperties() {
        return otherProperties;
    }

}
