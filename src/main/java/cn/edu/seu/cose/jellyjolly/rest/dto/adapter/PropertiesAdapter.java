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

package cn.edu.seu.cose.jellyjolly.rest.dto.adapter;

import cn.edu.seu.cose.jellyjolly.rest.dto.Property;
import cn.edu.seu.cose.jellyjolly.rest.dto.Values;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
public class PropertiesAdapter
        implements Adapter<Map<String, String[]>, List<Property>> {
    
    private static Values parseToValues(String[] propertyValues) {
        List<String> strLst = new LinkedList<String>();
        strLst.addAll(Arrays.asList(propertyValues));
        Values values = new Values();
        values.setValue(strLst);
        return values;
    }
    
    private static Property parseToProperty(Entry<String, String[]> adapterPair) {
        String propertyKey = adapterPair.getKey();
        String[] propertyValues = adapterPair.getValue();
        Property targetProperty = new Property();
        targetProperty.setKey(propertyKey);
        targetProperty.setValues(parseToValues(propertyValues));
        return targetProperty;
    }

    @Override
    public List<Property> adapt(Map<String, String[]> adaptee) {
        List<Property> target = new LinkedList<Property>();
        for (Entry<String, String[]> adapterPair: adaptee.entrySet()) {
            target.add(parseToProperty(adapterPair));
        }
        return target;
    }

}
