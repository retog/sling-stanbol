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

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.apache.clerezza.rdf.core.UriRef;
import org.apache.clerezza.rdf.core.access.TcManager;
import org.apache.clerezza.rdf.utils.GraphNode;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceNotFoundException;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.jcr.api.SlingRepository;
import org.apache.sling.stanbol.commons.Utils;
import org.apache.stanbol.enhancer.servicesapi.EnhancementJobManager;


@Component(immediate=true, metatype=false)
@Service(Object.class)
@Properties({
    @Property(name="service.description", value="Request URI Opting Test Servlet 2"),
    @Property(name="service.vendor", value="The Apache Software Foundation"),
    @Property(name="sling.servlet.extensions", value="stanbol"),
    @Property(name="sling.servlet.resourceTypes", value="sling/servlet/default"),
    @Property(name="sling.ws.rs", boolValue=true)
})
@SuppressWarnings("serial")
public class StanbolResourceViewer extends SlingSafeMethodsServlet {

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
	
	private String getMimeType(Node n) throws ValueFormatException, RepositoryException {
		try {
			return n.getProperty("jcr:content/jcr:mimeType").getString();
		} catch (PathNotFoundException ex) {
			return "application/octet-stream";
		}
	}

}
