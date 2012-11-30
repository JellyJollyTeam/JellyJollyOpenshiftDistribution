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
package cn.edu.seu.cose.jellyjolly.controller.filter;

import cn.edu.seu.cose.jellyjolly.dao.AdminUserDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.DataAccessException;
import cn.edu.seu.cose.jellyjolly.dto.AdminUser;
import cn.edu.seu.cose.jellyjolly.util.Utils;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
public class AdminUserInstanceBuilder extends HttpFilter {

    private static final String PARAM_USER_ID = "userid";
    private static final String ARRT_ADMIN_USER = "adminUser";
    private AdminUserDataAccess adminUserDao;

    @Override
    public void init(FilterConfig config) throws ServletException {
        adminUserDao = (AdminUserDataAccess) config.getServletContext()
                .getAttribute("cn.edu.seu.cose.jellyjolly.adminUserDataAccess");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        String userIdParam = request.getParameter(PARAM_USER_ID);

        if (userIdParam == null || !Utils.isNumeric(userIdParam)) {
            response.sendError(400);
            return;
        }

        try {
            long userId = Long.valueOf(userIdParam);
            AdminUser adminUser = adminUserDao.getUser(userId);
            request.setAttribute(ARRT_ADMIN_USER, adminUser);
            chain.doFilter(request, response);
        } catch (NumberFormatException ex) {
            Logger.getLogger(AdminUserInstanceBuilder.class.getName())
                    .log(Level.SEVERE, ex.getMessage(), ex);
            response.sendError(500, ex.getMessage());
        } catch (DataAccessException ex) {
            Logger.getLogger(AdminUserInstanceBuilder.class.getName())
                    .log(Level.SEVERE, ex.getMessage(), ex);
            response.sendError(500, ex.getMessage());
        }
    }
}
