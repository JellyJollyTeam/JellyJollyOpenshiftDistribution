/*
 * Copyright (C) 2013 College of Software Engineering, Southeast University
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
import cn.edu.seu.cose.jellyjolly.dao.BlogInfoDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.DataAccessException;
import cn.edu.seu.cose.jellyjolly.model.BlogInfo;
import java.util.Date;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Ray
 */
@Controller
public class InstallationController {

    private AdminUserDataAccess adminUserDataAccess;
    private BlogInfoDataAccess blogInfoDataAccess;

    public InstallationController(AdminUserDataAccess adminUserDataAccess,
            BlogInfoDataAccess blogInfoDataAccess) {
        this.adminUserDataAccess = adminUserDataAccess;
        this.blogInfoDataAccess = blogInfoDataAccess;
    }

    @RequestMapping(value = "/install", method = RequestMethod.GET)
    public String install(Model model) throws DataAccessException {
        if (installed()) {
            return "redirect:/";
        }
        BlogInfo blogInfo = blogInfoDataAccess.getBlogInfoInstance();
        model.addAttribute("blogInfo", blogInfo);
        return "install";
    }

    @RequestMapping(value = "/install", method = RequestMethod.POST)
    public String install(@RequestParam String title,
            @RequestParam String subtitle, @RequestParam String email,
            @RequestParam String displayName, @RequestParam String username,
            @RequestParam String password, @RequestParam String confirmPassword)
            throws DataAccessException {
        if (installed()) {
            return "redirect:/";
        }
        if (password.equals("")) {
            return "redirect:/install?err=1";
        }
        if (!password.equals(confirmPassword)) {
            return "redirect:/install?err=2";
        }
        Date now = new Date();
        adminUserDataAccess.addNewUser(username, password, email, displayName,
                now);
        blogInfoDataAccess.setBlogTitle(title);
        blogInfoDataAccess.setBlogSubTitle(subtitle);
        return "redirect:/";
    }

    private boolean installed() throws DataAccessException {
        return (adminUserDataAccess.getAllUsers().size() > 0);
    }
}
