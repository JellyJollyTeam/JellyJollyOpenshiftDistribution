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
import cn.edu.seu.cose.jellyjolly.dto.AdminUser;
import cn.edu.seu.cose.jellyjolly.model.session.UserAuthorization;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
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
    public String logOut(HttpServletRequest request) {
        return logOut(null, request);
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
}
