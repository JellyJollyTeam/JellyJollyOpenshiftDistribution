/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
