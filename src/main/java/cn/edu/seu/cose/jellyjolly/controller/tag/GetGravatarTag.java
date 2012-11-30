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
package cn.edu.seu.cose.jellyjolly.controller.tag;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 *
 * @author rAy
 */
public class GetGravatarTag extends SimpleTagSupport {

    private String email;

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void doTag() throws IOException {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] input = email.getBytes();
            byte[] byteArray = messageDigest.digest(input);
            StringBuilder md5StrBuff = new StringBuilder();
            for (int i = 0; i < byteArray.length; i++) {
                String hexStr = Integer.toHexString(0xFF & byteArray[i]);
                if (hexStr.length() == 1) {
                    md5StrBuff.append("0").append(hexStr);
                } else {
                    md5StrBuff.append(hexStr);
                }
            }
            JspWriter out = getJspContext().getOut();
            out.print("http://www.gravatar.com/avatar/");
            out.print(md5StrBuff.toString());
            out.print(".jpg?s=40");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(GetGravatarTag.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }
}
