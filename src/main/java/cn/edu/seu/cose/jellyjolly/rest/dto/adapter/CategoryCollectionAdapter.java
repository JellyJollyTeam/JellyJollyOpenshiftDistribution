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

import cn.edu.seu.cose.jellyjolly.dto.Category;
import cn.edu.seu.cose.jellyjolly.rest.dto.Categories;
import cn.edu.seu.cose.jellyjolly.rest.dto.CategoryInstance;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
public class CategoryCollectionAdapter
        implements Adapter<Collection<Category>, Categories> {

    @Override
    public Categories adapt(Collection<Category> adaptee) {
        Categories target = new Categories();
        List<CategoryInstance> iLst = new LinkedList<CategoryInstance>();
        CategoryInstanceAdapter iadapter = new CategoryInstanceAdapter();
        for (Category c: adaptee) {
            CategoryInstance i = iadapter.adapt(c);
            iLst.add(i);
        }
        target.setCategory(iLst);
        return target;
    }

}
