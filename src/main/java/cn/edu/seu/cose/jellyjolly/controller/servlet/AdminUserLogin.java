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
package cn.edu.seu.cose.jellyjolly.controller.servlet;

import cn.edu.seu.cose.jellyjolly.dao.AdminUserDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.DataAccessException;
import cn.edu.seu.cose.jellyjolly.dto.AdminUser;
import cn.edu.seu.cose.jellyjolly.model.session.UserAuthorization;
import java.io.IOException;
import java.util.Date;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
@WebServlet(name = "AdminUserLogin", urlPatterns = {"/login"})
public class AdminUserLogin extends HttpServlet {

    public static final String SESSION_ATTRI_AUTH = "userAuth";
    private static final String PARAM_USERNAME = "username";
    private static final String PARAM_PASSWORD = "password";
    private static final String PARAM_REMEMBER = "rememberMe";
    private static final String REMEMBER_VALUE = "true";
    private static final String COOKIE_USERNAME = "admin_username";
    private static final String COOKIE_PASSWORD = "admin_password";
    private static final String LOGIN_PAGE_WITH_ERROR = "./login.jsp?error=1";
    private static final String ADMIN_HOME_PAGE_URL = "./admin/admin.jsp";
    private static final int A_DAY_SECONDS = 24 * 60 * 60;
    private static final long THIRTY_MINUTES_MILLIS = 1000 * 60 * 30;
    private static final long DEFAULT_EXPIRE_DELTA = THIRTY_MINUTES_MILLIS;
    private AdminUserDataAccess adminUserDataAccess;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext ctx = config.getServletContext();
        adminUserDataAccess = (AdminUserDataAccess) ctx.getAttribute(
                "cn.edu.seu.cose.jellyjolly.adminUserDataAccess");
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter(PARAM_USERNAME);
        String password = request.getParameter(PARAM_PASSWORD);
        String rememberMe = request.getParameter(PARAM_REMEMBER);
        try {
            AdminUser user = adminUserDataAccess.getUserIfConfirmed(username,
                    password);
            if (user == null) {
                doIfNotConfirmed(request, response);
                return;
            }

            HttpSession session = request.getSession();
            if (REMEMBER_VALUE.equalsIgnoreCase(rememberMe)) {
                addCookie(response, user);
            }
            Date currentDate = new Date();
            adminUserDataAccess.setLastLoginTime(user.getUserId(), currentDate);
            doIfConfirmed(session, request, response, user);
        } catch (DataAccessException ex) {
            response.sendError(500, ex.getCause().getMessage());
        }
    }

    private void addCookie(HttpServletResponse response, AdminUser user) {
        // ...
    }

    private void doIfConfirmed(HttpSession session,
            HttpServletRequest request, HttpServletResponse response,
            AdminUser user) throws ServletException, IOException {
        long currentTimeMillis = System.currentTimeMillis();
        long expireTime = currentTimeMillis + DEFAULT_EXPIRE_DELTA;
        UserAuthorization userAuth = new UserAuthorization(user,
                currentTimeMillis, expireTime);
        session.setAttribute(SESSION_ATTRI_AUTH, userAuth);
        response.sendRedirect(ADMIN_HOME_PAGE_URL);
    }

    private void doIfNotConfirmed(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(LOGIN_PAGE_WITH_ERROR);
    }
}
