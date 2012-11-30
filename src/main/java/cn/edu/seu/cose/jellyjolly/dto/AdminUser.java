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
package cn.edu.seu.cose.jellyjolly.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author rAy
 */
public class AdminUser {

    private long userId;

    private String username;

    private String password;

    private String email;

    private String homePageUrl;

    private String displayName;

    private Date registerTime;

    private Date lastLoginTime;

    private Map<String, String[]> otherProperties;

    public AdminUser() {
        otherProperties = new HashMap<String, String[]>();
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setHomePageUrl(String homePageUrl) {
        this.homePageUrl = homePageUrl;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public void setOtherProperty(String name, String[] property) {
        otherProperties.put(name, property);
    }

    public long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getHomePageUrl() {
        return homePageUrl;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public String[] getOtherProperty(String name) {
        return otherProperties.get(name);
    }

    public Map<String, String[]> getOtherProperties() {
        return otherProperties;
    }

}
