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

import cn.edu.seu.cose.jellyjolly.dao.DataAccessException;
import cn.edu.seu.cose.jellyjolly.dao.LinkDataAccess;
import cn.edu.seu.cose.jellyjolly.dto.Link;
import cn.edu.seu.cose.jellyjolly.util.Utils;
import java.io.IOException;
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
@WebServlet(name = "LinkListOperation", urlPatterns = {"/admin/link"})
public class LinkListOperation extends HttpServlet {

    private static final String PARAM_OPERATION = "op";
    private static final String PARAM_TITLE = "title";
    private static final String PARAM_IMAGE = "image";
    private static final String PARAM_URL = "url";
    private static final String PARAM_LINK_ID = "linkid";
    private static final String OP_ADD = "add";
    private static final String OP_EDIT = "edit";
    private static final String OP_DELETE = "del";
    private static final String LINK_PAGE_URL = "./links.jsp";
    private static final Logger logger = Logger.getLogger(
            LinkListOperation.class.getName());
    private LinkDataAccess linkDataAccess;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext ctx = config.getServletContext();
        linkDataAccess = (LinkDataAccess) ctx.getAttribute(
                "cn.edu.seu.cose.jellyjolly.linkDataAccess");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
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

    private void add(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String title = request.getParameter(PARAM_TITLE);
        String image = request.getParameter(PARAM_IMAGE);
        String url = request.getParameter(PARAM_URL);
        if (title == null || url == null) {
            response.sendError(400);
            return;
        }

        try {
            if (image == null) {
                linkDataAccess.createNewLink(title, url);
            } else {
                linkDataAccess.createNewLink(title, image, url);
            }
            response.sendRedirect(LINK_PAGE_URL);
        } catch (DataAccessException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            response.sendError(500, ex.getMessage());
        }
    }

    private void edit(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String linkIdParam = request.getParameter(PARAM_LINK_ID);
        String title = request.getParameter(PARAM_TITLE);
        String image = request.getParameter(PARAM_IMAGE);
        String url = request.getParameter(PARAM_URL);
        if (linkIdParam == null || !Utils.isNumeric(linkIdParam)) {
            response.sendError(400);
            return;
        }

        try {
            long linkId = Long.valueOf(linkIdParam);
            Link link = linkDataAccess.getLinkById(linkId);
            if (title != null) {
                link.setTitle(title);
            }
            if (image != null) {
                link.setImage(image);
            }
            if (url != null) {
                link.setUrl(url);
            }
            linkDataAccess.updateLink(link);
            response.sendRedirect(LINK_PAGE_URL);
        } catch (NumberFormatException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            response.sendError(400, ex.getMessage());
        } catch (DataAccessException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            response.sendError(500, ex.getMessage());
        }
    }

    private void delete(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String linkIdParam = request.getParameter(PARAM_LINK_ID);
        if (linkIdParam == null || !Utils.isNumeric(linkIdParam)) {
            response.sendError(400);
            return;
        }

        try {
            long linkId = Long.valueOf(linkIdParam);
            linkDataAccess.deleteLink(linkId);
            response.sendRedirect(LINK_PAGE_URL);
        } catch (NumberFormatException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            response.sendError(400, ex.getMessage());
        } catch (DataAccessException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            response.sendError(500, ex.getMessage());
        }
    }
}
