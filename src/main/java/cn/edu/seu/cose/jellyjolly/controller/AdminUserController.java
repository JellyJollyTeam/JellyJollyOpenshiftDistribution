/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.seu.cose.jellyjolly.controller;

import cn.edu.seu.cose.jellyjolly.dao.AdminUserDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.DataAccessException;
import cn.edu.seu.cose.jellyjolly.dto.AdminUser;
import cn.edu.seu.cose.jellyjolly.model.session.UserAuthorization;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
@Controller
public class AdminUserController {

    public static final String SESSION_ATTRI_AUTH = "userAuth";
    private static final long THIRTY_MINUTES_MILLIS = 1000 * 60 * 30;
    private AdminUserDataAccess adminUserDataAccess;
    private long expireDelta = THIRTY_MINUTES_MILLIS;

    public AdminUserController(AdminUserDataAccess adminUserDataAccess) {
        this.adminUserDataAccess = adminUserDataAccess;
    }

    public void setExpireDelta(long expireDelta) {
        this.expireDelta = expireDelta;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String logIn() {
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String logIn(@RequestParam String username,
            @RequestParam String password, HttpServletRequest request)
            throws DataAccessException {
        AdminUser userConfirmed =
                adminUserDataAccess.getUserIfConfirmed(username, password);
        if (userConfirmed == null) {
            return "redirect:/login";
        }
        HttpSession currentSession = request.getSession();
        long currentTimeMillis = System.currentTimeMillis();
        long expireTime = currentTimeMillis + expireDelta;
        UserAuthorization userAuth = new UserAuthorization(userConfirmed,
                currentTimeMillis, expireTime);
        currentSession.setAttribute(SESSION_ATTRI_AUTH, userAuth);
        return "redirect:/admin";
    }

    @RequestMapping(value = "logout", method = RequestMethod.POST)
    public String logOut(@RequestParam String redirect,
            HttpServletRequest request) {
        HttpSession currentSession = request.getSession();
        currentSession.setAttribute(SESSION_ATTRI_AUTH, null);
        return (redirect == null)
                ? "redirect:/"
                : "redirect:" + redirect;
    }

    @RequestMapping(value = "/admin", method = RequestMethod.PUT)
    public String addNewAdminUser(@RequestParam String username,
            @RequestParam String password, @RequestParam String email,
            @RequestParam String homePage) throws DataAccessException {
        Date currentDate = new Date();
        adminUserDataAccess.addNewUser(username, password, email, username,
                homePage, currentDate);
        return "redirect:/admin/admins";
    }

    @RequestMapping(value = "/admin/{userId}", method = RequestMethod.DELETE)
    public String deleteAdminUser(@PathVariable long userId)
            throws DataAccessException {
        adminUserDataAccess.deleteUser(userId);
        return "redirect:/admin/admins";
    }

    @RequestMapping(value = "/admin/{userId}/username",
    method = RequestMethod.PUT)
    public String changeUsername(@PathVariable long userId,
            @RequestParam String username) throws DataAccessException {
        adminUserDataAccess.changeUserName(userId, username);
        return "redirect:/admin/admins";
    }

    @RequestMapping(value = "/admin/{userId}/password",
    method = RequestMethod.PUT)
    public String changePassword(@PathVariable long userId,
            @RequestParam String oldPassword, @RequestParam String newPassword,
            @RequestParam String confirmPassword) throws DataAccessException {
        AdminUser userToBeConfirmed = adminUserDataAccess.getUser(userId);
        String username = userToBeConfirmed.getUsername();
        boolean confirmed = adminUserDataAccess.confirm(username, oldPassword);
        if (!confirmed) {
            // indicate not authorized
            return "redirect:/admin/admins/" + userId + "?err=1";
        }
        if (!newPassword.equals(confirmPassword)) {
            // indicate passwords not same
            return "redirect:/admin/admins/" + userId + "?err=2";
        }
        adminUserDataAccess.changePassword(userId, newPassword);
        return "redirect:/admin/admins" + userId;
    }

    @RequestMapping(value = "/admin/{userId}/displayName",
    method = RequestMethod.PUT)
    public String changeDisplayName(@PathVariable long userId,
            @RequestParam String displayName) throws DataAccessException {
        AdminUser userToBeUpdated = adminUserDataAccess.getUser(userId);
        userToBeUpdated.setDisplayName(displayName);
        adminUserDataAccess.updateUser(userToBeUpdated);
        return "redirect:/admin/admins" + userId;
    }

    @RequestMapping(value = "/admin/{userId}/email",
    method = RequestMethod.PUT)
    public String changeEmail(@PathVariable long userId,
            @RequestParam String email) throws DataAccessException {
        AdminUser userToBeUpdated = adminUserDataAccess.getUser(userId);
        userToBeUpdated.setEmail(email);
        adminUserDataAccess.updateUser(userToBeUpdated);
        return "redirect:/admin/admins" + userId;
    }

    @RequestMapping(value = "/admin/{userId}/homePage",
    method = RequestMethod.PUT)
    public String changeHomePage(@PathVariable long userId,
            @RequestParam String homePage) throws DataAccessException {
        AdminUser userToBeUpdated = adminUserDataAccess.getUser(userId);
        userToBeUpdated.setHomePageUrl(homePage);
        adminUserDataAccess.updateUser(userToBeUpdated);
        return "redirect:/admin/admins" + userId;
    }
}
