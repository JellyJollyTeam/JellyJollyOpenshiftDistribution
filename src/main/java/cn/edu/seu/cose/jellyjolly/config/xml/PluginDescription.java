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

package cn.edu.seu.cose.jellyjolly.config.xml;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "plugins")
public class PluginDescription {
    
    @XmlElement(name = "plugin")
    private List<Plugin> plugins;

    public List<Plugin> getPlugins() {
        return plugins;
    }

    public void setPlugins(List<Plugin> plugins) {
        this.plugins = plugins;
    }

}
