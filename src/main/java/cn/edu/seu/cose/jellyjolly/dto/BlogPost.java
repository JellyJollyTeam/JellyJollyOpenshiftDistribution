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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author rAy
 */
public class BlogPost {

    private long postId;

    private AdminUser author;

    private Category category;

    private Date date;

    private String title;

    private String content;

    private List<Comment> comments;

    private Map<String, String[]> otherProperties;

    public BlogPost() {
        otherProperties = new HashMap<String, String[]>();
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public void setAuthor(AdminUser author) {
        this.author = author;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setOtherProperty(String name, String[] property) {
        otherProperties.put(name, property);
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public long getPostId() {
        return postId;
    }

    public AdminUser getAuthor() {
        return author;
    }

    public Category getCategory() {
        return category;
    }

    public Date getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String[] getOtherProperty(String name) {
        return otherProperties.get(name);
    }

    public List<Comment> getComments() {
        return comments;
    }

    public Map<String, String[]> getOtherProperties() {
        return otherProperties;
    }

}
