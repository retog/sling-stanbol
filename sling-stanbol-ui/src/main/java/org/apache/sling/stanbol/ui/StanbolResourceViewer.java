/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.sling.stanbol.ui;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.clerezza.rdf.core.UriRef;
import org.apache.clerezza.rdf.core.access.TcManager;
import org.apache.clerezza.rdf.utils.GraphNode;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceNotFoundException;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.jcr.api.SlingRepository;
import org.apache.sling.stanbol.commons.Utils;
import org.apache.stanbol.enhancer.servicesapi.EnhancementJobManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


@Component(immediate=true, metatype=false)
@Service(Object.class)
@Properties({
    @Property(name="service.description", value="Stanbol feature page"),
    @Property(name="service.vendor", value="The Apache Software Foundation"),
    @Property(name="sling.servlet.extensions", value="stanbol"),
    @Property(name="sling.servlet.resourceTypes", value="sling/servlet/default"),
    @Property(name="sling.servlet.methods", value={"GET","POST"}),
    @Property(name="sling.ws.rs", boolValue=true)
})
@SuppressWarnings("serial")
public class StanbolResourceViewer {

	@Reference
	private EnhancementJobManager ejm;

	@Reference
	private SlingRepository repository;
	
	@Reference
	private TcManager tcManager;
	
	@GET
	@Produces("*/*")
	public ResourcePage getPage(@Context UriInfo uriInfo, @Context SlingHttpServletRequest request) throws ValueFormatException, RepositoryException {
		//SlingHttpServletRequest slingRequest =
		Resource resource = request.getResource();
		if (ResourceUtil.isNonExistingResource(resource)) {
            throw new ResourceNotFoundException(
                resource.getPath(), "No resource found");
        }
		Node jcrNode = resource.adaptTo(Node.class);
		String mimeType = getMimeType(jcrNode);
		boolean vieEditorEnabled = false;
		if (mimeType.equals("text/html")) {
			vieEditorEnabled = true;
		}
		UriRef uri = Utils.getUri(resource.getPath());
		GraphNode gn = new GraphNode(uri, Utils.getEnhancementMGraph(tcManager));
		return new ResourcePage(gn, jcrNode, vieEditorEnabled);
	}
	
	@POST
	public Response saveContent(@Context UriInfo uriInfo, @Context SlingHttpServletRequest request, String content) 
			throws ValueFormatException, RepositoryException, IOException {
		//SlingHttpServletRequest slingRequest =
		Resource resource = request.getResource();
		if (ResourceUtil.isNonExistingResource(resource)) {
            throw new ResourceNotFoundException(
                resource.getPath(), "No resource found");
        }
		Node jcrNode = resource.adaptTo(Node.class);
		String mimeType = getMimeType(jcrNode);
		if (!mimeType.equals("text/html")) {
			throw new RuntimeException("currently only editing text/html resources is supported");
		}
		setContent(jcrNode, content);
		try {
			return Response.seeOther(new URI(Utils.getUri(resource.getPath()).getUnicodeString()+".stanbol")).build();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void setContent(Node jcrNode, String newContent) throws IOException, RepositoryException {

		try {
			Document doc = Utils.getXMLDocument(jcrNode);
			Element docElem = doc.getDocumentElement();
			if (docElem.getNodeName().equalsIgnoreCase("html")) {
				Element newBody = parseBody(newContent);
				Element body = (Element) doc.getElementsByTagName("body").item(0);
				org.w3c.dom.Node importedNewBody = doc.importNode(newBody, true);
				body.getParentNode().replaceChild(importedNewBody, body);
			} else {
				InputSource inputSource = new InputSource(new StringReader(newContent));
				Document newContentDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputSource);
				docElem = newContentDoc.getDocumentElement();
			}
			DOMSource domSource = new DOMSource(docElem);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			//StringWriter out = new StringWriter();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			StreamResult streamResult = new StreamResult(baos);
			transformer.transform(domSource, streamResult);
			//jcrNode.setProperty("jcr:content/jcr:data", out.toString());
			jcrNode.getProperty("jcr:content/jcr:data").setValue(new String(baos.toByteArray(), "utf-8"));
			jcrNode.save();
		} catch (SAXException e) {
			throw new RuntimeException(e);
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		} catch (TransformerConfigurationException e) {
			throw new RuntimeException(e);
		} catch (TransformerException e) {
			throw new RuntimeException(e);
		}
	}
	
	private Element parseBody(String string) throws SAXException, IOException, ParserConfigurationException {
		InputSource inputSource = new InputSource(new StringReader(string));
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputSource);
		Element docElem = doc.getDocumentElement();
		docElem.removeAttribute("id");
		doc.renameNode(docElem, null, "body");
		return docElem;
	}

	private String getMimeType(Node n) throws ValueFormatException, RepositoryException {
		try {
			return n.getProperty("jcr:content/jcr:mimeType").getString();
		} catch (PathNotFoundException ex) {
			return "application/octet-stream";
		}
	}

}
