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
package org.apache.sling.stanbol.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import org.apache.clerezza.rdf.core.MGraph;
import org.apache.clerezza.rdf.core.Resource;
import org.apache.clerezza.rdf.core.UriRef;
import org.apache.clerezza.rdf.core.serializedform.Serializer;
import org.apache.clerezza.rdf.core.serializedform.SupportedFormat;
import org.apache.clerezza.rdf.utils.GraphNode;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.stanbol.enhancer.servicesapi.ContentItem;
import org.apache.stanbol.enhancer.servicesapi.EngineException;
import org.apache.stanbol.enhancer.servicesapi.EnhancementException;
import org.apache.stanbol.enhancer.servicesapi.EnhancementJobManager;
import org.apache.stanbol.enhancer.servicesapi.helper.InMemoryContentItem;

/** Servlet that registers itself for specific paths */
@Component(immediate=true, metatype=false)
@Service(value=javax.servlet.Servlet.class)
@Properties({
    @Property(name="service.description", value="Paths Test Servlet"),
    @Property(name="service.vendor", value="The Apache Software Foundation"),
    @Property(name="sling.servlet.paths", value={
            "/slingstanbol/enhancer"
    })
})
@SuppressWarnings("serial")
public class EnhancerServlet extends SlingSafeMethodsServlet {

	@Reference
	EnhancementJobManager ejm;

	@Reference
	Serializer serializer;
	
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		final PrintWriter out =  response.getWriter();
		out.println("Enhancer running with "+ejm.getActiveEngines().size()+" active engines.");
		final String content = request.getParameter("content");
		out.println("<form action=\"enhancer\">");
		out.println("<textarea name=\"content\">");
		out.println(content == null? "" : content);
		out.println("</textarea><br/>");
		out.println("<input type=\"submit\" value=\"Enhance\"/>");
		if (content != null) {
			out.println("<br/>");
			UriRef contentUri = new UriRef("http://example.org/");
			ContentItem c = new InMemoryContentItem(contentUri.getUnicodeString(), content.getBytes() ,"text/plain");
			try {
				ejm.enhanceContent(c);
			} catch (EnhancementException ex) {
				throw new ServletException("Exception enhancing content", ex);
			}
			MGraph metadata = c.getMetadata();
			out.println("metadata size: "+metadata.size()+"<br/>");
			printEntities(out, contentUri, metadata);
			out.println("<textarea>");
			out.println(serializeToString(metadata));
			out.println("</textarea>");
		}
		out.println("</form>");	
	}
	
	
	private void printEntities(PrintWriter out, Resource resource, MGraph metadata) {
		boolean first = true;
		GraphNode node = new GraphNode(resource, metadata);
		Iterator<GraphNode> extractedAnnotations = node.getSubjectNodes(org.apache.stanbol.enhancer.servicesapi.rdf.Properties.ENHANCER_EXTRACTED_FROM);
		while (extractedAnnotations.hasNext()) {
			GraphNode extractedAnnotation = extractedAnnotations.next();
			Iterator<Resource> entityTypes = extractedAnnotation.getObjects(org.apache.stanbol.enhancer.servicesapi.rdf.Properties.ENHANCER_ENTITY_TYPE);
			while (entityTypes.hasNext()) {
				Resource entityType = entityTypes.next();
				if (entityType instanceof UriRef) {
					if (first) {
						out.println("<h3>Entities:</h3>");
						first = false;
					}
					out.println(((UriRef)entityType).getUnicodeString()+": ");
					out.println(extractedAnnotation.getLiterals(org.apache.stanbol.enhancer.servicesapi.rdf.Properties.ENHANCER_ENTITY_LABEL).next().getLexicalForm()+"<br/>");
				}
			}
		}
	}


	private String serializeToString(MGraph metadata) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		serializer.serialize(out, metadata, SupportedFormat.RDF_XML);
		try {
			return new String(out.toByteArray(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}


}
