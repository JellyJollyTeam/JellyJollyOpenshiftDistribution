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
package cn.edu.seu.cose.jellyjolly.rest.resource;

import cn.edu.seu.cose.jellyjolly.dao.CategoryDataAccess;
import cn.edu.seu.cose.jellyjolly.dao.DataAccessException;
import cn.edu.seu.cose.jellyjolly.dto.Category;
import cn.edu.seu.cose.jellyjolly.rest.dto.Categories;
import cn.edu.seu.cose.jellyjolly.rest.dto.CategoryInstance;
import cn.edu.seu.cose.jellyjolly.rest.dto.adapter.Adapter;
import cn.edu.seu.cose.jellyjolly.rest.dto.adapter.CategoryCollectionAdapter;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
public class CategoriesResource extends ServerResource {

    private static final Logger logger = Logger.getLogger(
            CategoriesResource.class.getName());
    private CategoryDataAccess categoryDao;

    public CategoriesResource() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext(
                "cn/edu/seu/cose/jellyjolly/dist/applicationContext.xml");
        categoryDao = (CategoryDataAccess) ctx.getBean("categoryDataAccess");
    }

    @Get("xml")
    public Representation getAllCategories() throws DataAccessException {
        try {
            List<Category> categoryList = categoryDao.getAllCategories();
            Adapter<Collection<Category>, Categories> adapter =
                    new CategoryCollectionAdapter();
            Categories categories = adapter.adapt(categoryList);
            return ResourceUtils.getRepresentationOfXmlObject(categories);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            return ResourceUtils.getFailureRepresentation(ex);
        }
    }

    @Post("xml:xml")
    public Representation createNewCategory(Representation newCategory) {
        try {
            DomRepresentation domCategory = new DomRepresentation(newCategory);
            DomRepresentationReader reader = new DomRepresentationReader();
            CategoryInstance categoryInstance = (CategoryInstance) reader
                    .getXmlObject(domCategory, CategoryInstance.class);
            categoryDao.createNewCategory(categoryInstance.getName());
            return ResourceUtils.getUpdateSuccessRepresentation();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            return ResourceUtils.getFailureRepresentation(ex);
        }
    }
}
