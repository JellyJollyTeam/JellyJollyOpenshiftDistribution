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

import cn.edu.seu.cose.jellyjolly.dao.AdminUserDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.DataAccessException;
import cn.edu.seu.cose.jellyjolly.model.AdminUser;
import cn.edu.seu.cose.jellyjolly.model.session.UserAuthorization;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
@Controller
public class AdminUserController {

    public static final String SESSION_ATTRI_AUTH =
            "cn.edu.seu.cose.jellyjolly.userAuth";
    private static final long THIRTY_MINUTES_MILLIS = 1000 * 60 * 30;
    private AdminUserDataAccess adminUserDataAccess;
    private long expireDelta = THIRTY_MINUTES_MILLIS;

    public AdminUserController(AdminUserDataAccess adminUserDataAccess) {
        this.adminUserDataAccess = adminUserDataAccess;
    }

    public void setExpireDelta(long expireDelta) {
        this.expireDelta = expireDelta;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String logIn() {
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String logIn(@RequestParam String username,
            @RequestParam String password, HttpServletRequest request)
            throws DataAccessException {
        AdminUser userConfirmed =
                adminUserDataAccess.getUserIfConfirmed(username, password);
        if (userConfirmed == null) {
            return "redirect:/login?error=1";
        }
        // update the last login time
        Date now = new Date();
        adminUserDataAccess.setLastLoginTime(userConfirmed.getUserId(), now);

        // register the login info in the session
        HttpSession currentSession = request.getSession();
        long currentTimeMillis = System.currentTimeMillis();
        long expireTime = currentTimeMillis + expireDelta;
        UserAuthorization userAuth = new UserAuthorization(userConfirmed,
                currentTimeMillis, expireTime);
        currentSession.setAttribute(SESSION_ATTRI_AUTH, userAuth);
        return "redirect:/admin";
    }

    @RequestMapping(value = "/logout", method = {
        RequestMethod.GET, RequestMethod.POST})
    public String logOut(@RequestParam String redirect,
            HttpServletRequest request) {
        HttpSession currentSession = request.getSession();
        currentSession.setAttribute(SESSION_ATTRI_AUTH, null);
        return (redirect == null)
                ? "redirect:/"
                : "redirect:" + redirect;
    }

    @RequestMapping(value = "/admin/user", method = RequestMethod.POST)
    public String createNewAdminUser(@RequestParam String username,
            @RequestParam String password, @RequestParam String confirmPassword,
            @RequestParam String displayName, @RequestParam String email,
            @RequestParam String homePage) throws DataAccessException {
        if (!password.equals(confirmPassword)) {
        return "redirect:/admin/users?err=1";
        }
        Date now = new Date();
        if (homePage != null && !homePage.trim().equals("")) {
            adminUserDataAccess.addNewUser(username, password, email, homePage,
                    displayName, now);
        } else {
            adminUserDataAccess.addNewUser(username, password, email,
                    displayName, now);
        }
        return "redirect:/admin/users";
    }

    @RequestMapping(value = "/admin/user/{userId}", method = RequestMethod.PUT)
    public String editAdminUser(@PathVariable long userId,
            @RequestParam String username, @RequestParam String displayName,
            @RequestParam String email, @RequestParam String homePage,
            @RequestParam String redirect) throws DataAccessException {
        AdminUser adminUserToBeUpdated = adminUserDataAccess.getUser(userId);
        adminUserToBeUpdated.setUsername(username);
        adminUserToBeUpdated.setDisplayName(displayName);
        adminUserToBeUpdated.setEmail(email);
        adminUserToBeUpdated.setHomePageUrl(homePage);
        adminUserDataAccess.updateUserExceptPassword(adminUserToBeUpdated);
        return "redirect:" + redirect;
    }

    @RequestMapping(value = "/admin/user/{userId}/password",
            method = RequestMethod.PUT)
    public String changePassword(@PathVariable long userId,
            @RequestParam String oldPassword, @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            @RequestParam String redirect) throws DataAccessException {
        AdminUser adminUser = adminUserDataAccess.getUser(userId);
        String username = adminUser.getUsername();

        boolean confirmed = adminUserDataAccess.confirm(username, oldPassword);
        if (!confirmed) {
            return "redirect:/admin/users/" + adminUser.getUserId() + "/password?err=1";
        }

        boolean empty = newPassword.trim().equals("");
        if (empty) {
            return "redirect:/admin/users/" + adminUser.getUserId() + "/password?err=2";
        }

        boolean same = newPassword.equals(confirmPassword);
        if (!same) {
            return "redirect:/admin/users/" + adminUser.getUserId() + "/password?err=3";
        }
        adminUserDataAccess.changePassword(userId, newPassword);
        return "redirect:" + redirect;
    }

    @RequestMapping(value = "/admin/user/{userId}",
            method = RequestMethod.DELETE)
    public String deleteAdminUser(@PathVariable long userId,
            HttpServletRequest request) throws DataAccessException {
        HttpSession currentSession = request.getSession();
        UserAuthorization userAuth = (UserAuthorization) currentSession
                .getAttribute(SESSION_ATTRI_AUTH);
        long currentUserId = userAuth.getUser().getUserId();

        boolean suicide = (currentUserId == userId);
        if (suicide) {
            return "redirect:/admin/users?err=1";
        }

        adminUserDataAccess.deleteUser(userId);
        return "redirect:/admin/users";
    }

    @RequestMapping(value = "/admin/user",
            method = RequestMethod.DELETE)
    public String deleteAdminUser(@RequestParam List<Long> userIds,
            @RequestParam String redirect,
            HttpServletRequest request) throws DataAccessException {
        HttpSession currentSession = request.getSession();
        UserAuthorization userAuth = (UserAuthorization) currentSession
                .getAttribute(SESSION_ATTRI_AUTH);
        long currentUserId = userAuth.getUser().getUserId();

        for (long userId : userIds) {
            boolean suicide = (currentUserId == userId);
            if (suicide) {
                continue;
            }
            adminUserDataAccess.deleteUser(userId);
        }
        return "redirect:" + redirect;
    }
}
