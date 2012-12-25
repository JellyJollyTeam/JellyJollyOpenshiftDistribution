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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
@Controller
public class InstallationController {

    @RequestMapping(value = "/install", method = RequestMethod.GET)
    public String install() {
//        if (installed) {
//            return "redirect:/";
//        }
        return "install";
    }

    @RequestMapping(value = "/install", method = RequestMethod.POST)
    public String install(String username, String password,
            String confirmPassword, String title, String subtitle,
            String email, String homePage) {
//        if (installed) {
//            return "redirect:/";
//        }
        return "install";
    }
}
