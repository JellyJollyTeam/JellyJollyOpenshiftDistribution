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
package cn.edu.seu.cose.jellyjolly.controller;

import cn.edu.seu.cose.jellyjolly.dao.DataAccessException;
import cn.edu.seu.cose.jellyjolly.dao.InitializationDataAccess;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
@Controller
public class InstallationController {

    private InitializationDataAccess initializationDataAccess;

    public InstallationController(
            InitializationDataAccess initializationDataAccess) {
        this.initializationDataAccess = initializationDataAccess;
    }

    @RequestMapping(value = "/install", method = RequestMethod.GET)
    public String install() throws DataAccessException {
        if (initializationDataAccess.installed()) {
            return "redirect:/";
        }
        return "install";
    }

    @RequestMapping(value = "/install", method = RequestMethod.POST)
    public String install(@RequestParam String username,
            @RequestParam String password, @RequestParam String confirmPassword,
            @RequestParam String title, @RequestParam String subtitle,
            @RequestParam String email) throws DataAccessException {
        if (initializationDataAccess.installed()) {
            return "redirect:/";
        }

        if (!password.equals(confirmPassword)) {
            return "redirect:/install?error=1";
        }
        initializationDataAccess.initialize(username, password, username, email,
                title, subtitle);
        return "redirect:/";
    }
}
