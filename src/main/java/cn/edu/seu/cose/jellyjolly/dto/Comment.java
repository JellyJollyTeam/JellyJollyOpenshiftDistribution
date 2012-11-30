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

/**
 *
 * @author rAy
 */
public class Comment {

    private long commentId;

    private Comment parentComment;

    private long postId;

    private String authorName;

    private String authorEmail;

    private String authorHomePageUrl;

    private Date date;

    private String content;

    private long authorUserId;

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }

    public void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public void setAuthorHomePageUrl(String authorHomePageUrl) {
        this.authorHomePageUrl = authorHomePageUrl;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAuthorUserId(Long authorUserId) {
        this.authorUserId = authorUserId;
    }

    public long getCommentId() {
        return commentId;
    }

    public Comment getParentComment() {
        return parentComment;
    }

    public long getPostId() {
        return postId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public String getAuthorHomePageUrl() {
        return authorHomePageUrl;
    }

    public Date getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public long getAuthorUserId() {
        return authorUserId;
    }

}
