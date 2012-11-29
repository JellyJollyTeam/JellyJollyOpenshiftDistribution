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
package cn.edu.seu.cose.jellyjolly.dist.mysql;

import cn.edu.seu.cose.jellyjolly.dao.DataAccessFactory;
import cn.edu.seu.cose.jellyjolly.dao.jdbc.MysqlDataAccessFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
public class ContextDependencyInjection implements ServletContextListener {

    private static final Logger logger = Logger.getLogger(
            ContextDependencyInjection.class.getName());
    private static final ApplicationContext appCtx =
            new ClassPathXmlApplicationContext(
            "cn/edu/seu/cose/jellyjolly/dist/applicationContext.xml");

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.log(Level.INFO,
                "Injecting the context dependency");
        ServletContext servletCtx = sce.getServletContext();
        try {
            injectContextDependency(appCtx, servletCtx);
        } catch (Exception ex) {
            logger.log(Level.SEVERE,
                    "Failed during injecting the context dependency");
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
        logger.log(Level.INFO, "Context dependency Injected");
    }

    private void injectContextDependency(ApplicationContext appCtx,
            ServletContext servletCtx) {
        servletCtx.setAttribute(
                "cn.edu.seu.cose.jellyjolly.adminUserDataAccess",
                appCtx.getBean("adminUserDataAccess"));
        servletCtx.setAttribute(
                "cn.edu.seu.cose.jellyjolly.blogInfoDataAccess",
                appCtx.getBean("blogInfoDataAccess"));
        servletCtx.setAttribute(
                "cn.edu.seu.cose.jellyjolly.blogPageDataAccess",
                appCtx.getBean("blogPageDataAccess"));
        servletCtx.setAttribute(
                "cn.edu.seu.cose.jellyjolly.blogPostDataAccess",
                appCtx.getBean("blogPostDataAccess"));
        servletCtx.setAttribute(
                "cn.edu.seu.cose.jellyjolly.categoryDataAccess",
                appCtx.getBean("categoryDataAccess"));
        servletCtx.setAttribute(
                "cn.edu.seu.cose.jellyjolly.commentDataAccess",
                appCtx.getBean("commentDataAccess"));
        servletCtx.setAttribute(
                "cn.edu.seu.cose.jellyjolly.linkDataAccess",
                appCtx.getBean("linkDataAccess"));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
