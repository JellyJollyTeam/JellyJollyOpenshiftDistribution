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

package cn.edu.seu.cose.jellyjolly.rest.auth;

import cn.edu.seu.cose.jellyjolly.dao.AdminUserDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.DataAccessFactory;
import java.io.IOException;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
public class ResourceAuthenticationFilter extends AuthenticationFilter {

    private static final long THIRTY_MINS_IN_MILLIS = 1000 * 60 * 60 * 30;

    private AdminUserDataAccess adminUserDataAccess;

    @Override
    public void init(FilterConfig config) throws ServletException {
        ServletContext ctx = config.getServletContext();
        adminUserDataAccess = (AdminUserDataAccess) ctx.getAttribute(
                "cn.edu.seu.cose.jellyjolly.adminUserDataAccess");
    }

    @Override
    protected boolean authenticate(String username, String password)
            throws Exception {
        return adminUserDataAccess.confirm(username, password);
    }

    @Override
    protected void doAfterAuthorized(HttpServletRequest request,
            HttpServletResponse response, AuthorizationBean authorizationBean)
            throws IOException, ServletException {
        // do nothing here
    }

    @Override
    protected boolean expired(AuthorizationBean authBean) throws Exception {
        long currentTimeMillis = System.currentTimeMillis();
        long expireTime = authBean.getAuthorizedTime() + THIRTY_MINS_IN_MILLIS;
        return expireTime > currentTimeMillis;
    }

}
