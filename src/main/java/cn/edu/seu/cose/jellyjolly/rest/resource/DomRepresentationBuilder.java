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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.restlet.ext.xml.DomRepresentation;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
public class DomRepresentationBuilder {

    private static final String DEFAULT_ENCODING = "UTF-8";

    private DocumentBuilder docBuilder;

    public DomRepresentationBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory docBuilderFactory =
                DocumentBuilderFactory.newInstance();
        docBuilder = docBuilderFactory.newDocumentBuilder();
    }

    public DomRepresentation getRepresentation(Object obj)
            throws JAXBException, IOException, SAXException,
            ParserConfigurationException {
        String xmlString = getXmlString(obj);
        Document outputDocument = getDocumentByXmlString(xmlString);

        // build domrepresentation by the document
        DomRepresentation outputRepresentation = new DomRepresentation();
        outputRepresentation.setDocument(outputDocument);
        return outputRepresentation;
    }

    private String getXmlString(Object obj) throws JAXBException,
            IOException {
        Writer stringWriter = new StringWriter();
        JAXBContext jaxbContext = JAXBContext.newInstance(obj.getClass());
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(obj, stringWriter);
        stringWriter.close();
        return stringWriter.toString();
    }

    private Document getDocumentByXmlString(String xmlString)
            throws SAXException, IOException {
        byte[] byteArrayInUtf8 = xmlString.getBytes(DEFAULT_ENCODING);
        ByteArrayInputStream in = new ByteArrayInputStream(byteArrayInUtf8);
        return docBuilder.parse(in);
    }

}