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
import cn.edu.seu.cose.jellyjolly.dao.CommentDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.DataAccessException;
import cn.edu.seu.cose.jellyjolly.dao.DataAccessFactory;
import cn.edu.seu.cose.jellyjolly.dto.AdminUser;
import cn.edu.seu.cose.jellyjolly.util.Utils;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
@WebServlet(name = "AdminUserOperation", urlPatterns = {"/admin/user"})
public class AdminUserOperation extends HttpServlet {

    private static final String PARAM_OPERATION = "op";
    private static final String PARAM_USER_ID = "userid";
    private static final String PARAM_USERNAME = "username";
    private static final String PARAM_PREVIOUS_PASS = "prvpass";
    private static final String PARAM_NEW_PASS = "newpass";
    private static final String PARAM_EMAIL = "email";
    private static final String PARAM_HOME_PAGE = "homepage";
    private static final String PARAM_DISPLAY_NAME = "displayname";
    private static final String OP_ADD = "add";
    private static final String OP_EDIT = "edit";
    private static final String OP_DELETE = "del";
    private static final String USERS_URL = "./users.jsp";
    private static final String HOME_URL = "../home.jsp";
    private static final Logger logger = Logger.getLogger(
            AdminUserOperation.class.getName());
    private AdminUserDataAccess adminUserDataAccess;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext ctx = config.getServletContext();
        adminUserDataAccess = (AdminUserDataAccess) ctx.getAttribute(
                "cn.edu.seu.cose.jellyjolly.adminUserDataAccess");
    }

    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
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
            HttpServletResponse response)
            throws ServletException, IOException {
        String operation = request.getParameter(PARAM_OPERATION);

        if (OP_ADD.equalsIgnoreCase(operation)) {
            add(request, response);
            return;
        }
        if (OP_EDIT.equalsIgnoreCase(operation)) {
            edit(request, response);
            return;
        }
        if (OP_DELETE.equalsIgnoreCase(operation)) {
            delete(request, response);
            return;
        }

        response.sendError(400);
    }

    private void add(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter(PARAM_USERNAME);
        String password = request.getParameter(PARAM_NEW_PASS);
        String email = request.getParameter(PARAM_EMAIL);
        String homePage = request.getParameter(PARAM_HOME_PAGE);
        String displayName = request.getParameter(PARAM_DISPLAY_NAME);
        if (username == null || password == null || email == null
                || displayName == null) {
            response.sendError(400);
            return;
        }

        try {
            Date currentDate = new Date();
            if (homePage == null) {
                adminUserDataAccess.addNewUser(username, password, email,
                        displayName, currentDate);
            } else {
                adminUserDataAccess.addNewUser(username, password, email,
                        homePage, displayName, currentDate);
            }
            response.sendRedirect(USERS_URL);
        } catch (DataAccessException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            response.sendError(500, ex.getMessage());
        }
    }

    private void edit(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        String userIdParam = request.getParameter(PARAM_USER_ID);
        String username = request.getParameter(PARAM_USERNAME);
        String previousPass = request.getParameter(PARAM_PREVIOUS_PASS);
        String newPassword = request.getParameter(PARAM_NEW_PASS);
        String email = request.getParameter(PARAM_EMAIL);
        String homePage = request.getParameter(PARAM_HOME_PAGE);
        String displayName = request.getParameter(PARAM_DISPLAY_NAME);

        if (userIdParam == null || !Utils.isNumeric(userIdParam)) {
            response.sendError(400);
            return;
        }

        try {
            long userId = Long.valueOf(userIdParam);
            AdminUser currentUser = adminUserDataAccess.getUser(userId);

            if (newPassword != null && (previousPass == null
                    || !adminUserDataAccess.confirm(currentUser.getUsername(),
                    previousPass))) {
                response.sendError(401);
            }

            if (username != null) {
                currentUser.setUsername(username);
            }
            if (newPassword != null) {
                currentUser.setPassword(newPassword);
            }
            if (displayName != null) {
                currentUser.setDisplayName(displayName);
            }
            if (email != null) {
                currentUser.setEmail(email);
            }
            if (homePage != null) {
                currentUser.setHomePageUrl(homePage);
            }

            adminUserDataAccess.updateUser(currentUser);
            response.sendRedirect(USERS_URL);
        } catch (NumberFormatException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            response.sendError(400, ex.getMessage());
        } catch (DataAccessException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            response.sendError(500, ex.getMessage());
        }
    }

    private void delete(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        String userIdParam = request.getParameter(PARAM_USER_ID);

        if (userIdParam == null || !Utils.isNumeric(userIdParam)) {
            response.sendError(400);
            return;
        }

        try {
            long userId = Long.valueOf(userIdParam);
            adminUserDataAccess.deleteUser(userId);
            response.sendRedirect(USERS_URL);
        } catch (NumberFormatException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            response.sendError(400, ex.getMessage());
        } catch (DataAccessException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            response.sendError(500, ex.getMessage());
        }
    }
}
