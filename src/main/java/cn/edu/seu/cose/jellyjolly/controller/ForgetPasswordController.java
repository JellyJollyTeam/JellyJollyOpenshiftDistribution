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
import cn.edu.seu.cose.jellyjolly.dao.DataAccessException;
import cn.edu.seu.cose.jellyjolly.model.AdminUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
@Controller
public class ForgetPasswordController {

    private AdminUserDataAccess adminUserDataAccess;

    public ForgetPasswordController(
            AdminUserDataAccess adminUserDataAccess) {
        this.adminUserDataAccess = adminUserDataAccess;
    }

    @RequestMapping(value = "/forgetpass", method = RequestMethod.GET)
    public String forgetMyPassword() {
        return "forgetpass";
    }

    @RequestMapping(value = "/forgetpass", method = RequestMethod.POST)
    public String getMyNewPasswordByEmail(@RequestParam String username)
            throws DataAccessException {
        AdminUser adminUser = adminUserDataAccess.getUserByUsername(username);
        if (adminUser == null) {
            return "redirect:/forgetpass?error=1";
        }

        String email = adminUser.getEmail();
        // TODO send email
        return "redirect:/forgetpass?done=1";
    }
}
