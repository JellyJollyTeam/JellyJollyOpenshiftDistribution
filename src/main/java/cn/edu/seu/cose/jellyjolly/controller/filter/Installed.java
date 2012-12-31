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
package cn.edu.seu.cose.jellyjolly.controller.filter;

import cn.edu.seu.cose.jellyjolly.dao.DataAccessException;
import cn.edu.seu.cose.jellyjolly.dao.InitializationDataAccess;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author rAy
 */
public class Installed extends HttpFilter {

    private static final Logger logger = Logger.getLogger(Installed.class.getName());
    private InitializationDataAccess initializationDataAccess;
    private boolean installed = false;

    @Override
    public void init(FilterConfig config) throws ServletException {
        ApplicationContext ctx = new ClassPathXmlApplicationContext(
                "cn/edu/seu/cose/jellyjolly/dist/persistenceContext.xml");
        initializationDataAccess = (InitializationDataAccess) ctx
                .getBean("initializationDataAccess");
    }

    @Override
    public void doHttpFilter(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            if (installed) {
                chain.doFilter(request, response);
                return;
            }
            
            String requestUri = request.getRequestURI();
            installed = initializationDataAccess.installed();
            if (installed
                    || requestUri.startsWith("/install")) {
            }

            response.sendError(503, "application not installed");
        } catch (DataAccessException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new ServletException(ex);
        }
    }
}
