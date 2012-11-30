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

import cn.edu.seu.cose.jellyjolly.controller.servlet.AdminUserLogin;
import cn.edu.seu.cose.jellyjolly.model.session.UserAuthorization;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
public class AuthorityRequired extends HttpFilter {

    private static final String LOGIN_PAGE_URL = "../login.jsp";

    @Override
    public void doHttpFilter(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        UserAuthorization userAuth = (UserAuthorization)
                session.getAttribute(AdminUserLogin.SESSION_ATTRI_AUTH);
        if (!isValid(userAuth)) {
            response.sendRedirect(LOGIN_PAGE_URL);
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isValid(UserAuthorization userAuth) {
        if (userAuth == null) {
            return false;
        }

        long expiredTime = userAuth.getExpireTime();
        long currentTime = System.currentTimeMillis();
        return expiredTime > currentTime;
    }

}
