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

import javax.servlet.ServletException;

import org.apache.clerezza.rdf.core.Graph;
import org.apache.clerezza.rdf.core.TripleCollection;
import org.apache.clerezza.rdf.core.UriRef;
import org.apache.clerezza.rdf.core.access.TcManager;
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
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceNotFoundException;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.stanbol.commons.Utils;
import org.apache.stanbol.enhancer.servicesapi.EnhancementJobManager;


@Component(immediate=true, metatype=false)
@Service(value=javax.servlet.Servlet.class)
@Properties({
    @Property(name="service.description", value="Request URI Opting Test Servlet 2"),
    @Property(name="service.vendor", value="The Apache Software Foundation"),
    @Property(name="sling.servlet.extensions", value="meta"),
    @Property(name="sling.servlet.resourceTypes", value="sling/servlet/default"),
    @Property(name="sling.servlet.methods", value={"GET"})
})
@SuppressWarnings("serial")
public class MetaDataViewerServlet extends SlingSafeMethodsServlet {

	@Reference
	EnhancementJobManager ejm;

	@Reference
	Serializer serializer;
	
	@Reference
	TcManager tcManager;
	
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
		// olny handle requests for existing resources

        Resource resource = request.getResource();
		if (ResourceUtil.isNonExistingResource(resource)) {
            throw new ResourceNotFoundException(
                resource.getPath(), "No resource found");
        }
		UriRef uri = Utils.getUri(resource.getPath());
		GraphNode gn = new GraphNode(uri, Utils.getEnhancementMGraph(tcManager));
		Graph metadata = gn.getNodeContext();
		response.setContentType("text/html");
		final PrintWriter out =  response.getWriter();
		out.println("<h1>Metadata</h1>");
		out.println("<textarea cols='80' rows='30'>");
		
		out.println(serializeToString(metadata));
		out.println("</textarea>");
	}
	
	//code duplication, but hopefully thid servlet can be replaced soon with a 
	//jax-rs resource where writing the rdf is the business of MBW
	private String serializeToString(TripleCollection metadata) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		serializer.serialize(out, metadata, SupportedFormat.RDF_XML);
		try {
			return new String(out.toByteArray(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

}
