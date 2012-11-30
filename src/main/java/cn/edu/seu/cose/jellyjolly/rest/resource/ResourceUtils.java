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

import cn.edu.seu.cose.jellyjolly.rest.dto.ExceptionInstance;
import cn.edu.seu.cose.jellyjolly.rest.dto.Success;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.xml.sax.SAXException;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
final class ResourceUtils {
    
    public static Representation getRepresentationOfXmlObject(Object obj)
            throws ParserConfigurationException, JAXBException, IOException,
            SAXException {
        DomRepresentationBuilder builder = new DomRepresentationBuilder();
        return builder.getRepresentation(obj);
    }

    public static Representation getUpdateSuccessRepresentation()
            throws ParserConfigurationException, JAXBException, IOException,
            SAXException {
        DomRepresentationBuilder builder = new DomRepresentationBuilder();
        Success success = new Success();
        return builder.getRepresentation(success);
    }
    
    private static final String DEFAULT_FAILURE_XML_MSG =
            "<?xml version=\"1.0\" ?>"
            + "<exception>"
            + "<type />"
            + "<message>Unknown exception occurred.</message>"
            + "</exception>";
    
    public static Representation getDefaultFailureRepresentation() {
        return new StringRepresentation(DEFAULT_FAILURE_XML_MSG);
    }
    
    public static Representation getFailureRepresentation(Exception ex) {
        String type = ex.getClass().getName();
        String message = ex.getMessage();
        ExceptionInstance exceptionInstance = new ExceptionInstance();
        exceptionInstance.setMessage(message);
        exceptionInstance.setType(type);
        try {
            DomRepresentationBuilder builder = new DomRepresentationBuilder();
            return builder.getRepresentation(exceptionInstance);
        } catch (Exception innerEx) {
            Logger.getLogger(ResourceUtils.class.getName())
                    .log(Level.SEVERE, innerEx.getMessage(), innerEx);
            return getDefaultFailureRepresentation();
        }
    }

}
