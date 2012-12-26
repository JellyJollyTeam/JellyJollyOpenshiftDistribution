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
package cn.edu.seu.cose.jellyjolly.controller;

import cn.edu.seu.cose.jellyjolly.dao.CommentDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.DataAccessException;
import cn.edu.seu.cose.jellyjolly.model.session.UserAuthorization;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
@Controller
public class CommentController {

    private CommentDataAccess commentDataAccess;

    public CommentController(CommentDataAccess commentDataAccess) {
        this.commentDataAccess = commentDataAccess;
    }

    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public String addComment(@RequestParam long postId,
            @RequestParam String authorName, @RequestParam String email,
            @RequestParam String homePage, @RequestParam String content)
            throws DataAccessException {
        Date now = new Date();
        commentDataAccess.addNewComment(postId, authorName, email, homePage,
                content, now);
        return "redirect:/post/" + postId;
    }

    @RequestMapping(value = "/admin/comment", method = RequestMethod.POST)
    public String addComment(@RequestParam long postId,
            @RequestParam String content, HttpServletRequest request)
            throws DataAccessException {
        Date now = new Date();
        HttpSession currentSession = request.getSession();
        UserAuthorization userAuth = (UserAuthorization) currentSession
                .getAttribute(AdminUserController.SESSION_ATTRI_AUTH);
        long userId = userAuth.getUser().getUserId();
        commentDataAccess.addNewComment(postId, userId, content, now);
        return "redirect:/post/" + postId;
    }

    @RequestMapping(value = "/admin/comment",
    method = RequestMethod.DELETE)
    public String deleteComment(@RequestParam List<Long> commentIds,
            @RequestParam String redirect) throws DataAccessException {
        for (long commentId : commentIds) {
            commentDataAccess.deleteCommentById(commentId);
        }
        return "redirect:" + redirect;
    }

    @RequestMapping(value = "/admin/comment/{commentId}",
    method = RequestMethod.DELETE)
    public String deleteComment(@PathVariable long commentId,
            @RequestParam String redirect) throws DataAccessException {
        commentDataAccess.deleteCommentById(commentId);
        return "redirect:" + redirect;
    }
}
