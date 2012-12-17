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
package cn.edu.seu.cose.jellyjolly.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
public class Utils {

    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    public static boolean isNumericOrNull(String str) {
        return (str == null) || Utils.isNumeric(str);
    }

    public static String truncateHtml(String html, int maxLength) {
        Pattern p = Pattern.compile("<[^>]+>([^<]*)");

        int i = 0;
        List<String> tags = new ArrayList<String>();

        Matcher m = p.matcher(html);
        while (m.find()) {
            if (m.start(0) - i >= maxLength) {
                break;
            }

            String t = StringUtils.split(m.group(0), " \t\n\r\0\u000B>")[0].substring(1);
            if (t.charAt(0) != '/') {
                tags.add(t);
            } else if (tags.get(tags.size() - 1).equals(t.substring(1))) {
                tags.remove(tags.size() - 1);
            }
            i += m.start(1) - m.start(0);
        }

        Collections.reverse(tags);
        return html.substring(0, Math.min(html.length(), maxLength + i))
                + ((tags.size() > 0) ? "</" + StringUtils.join(tags, "></") + ">" : "")
                + ((html.length() > maxLength) ? "\u2026" : "");

    }
}
