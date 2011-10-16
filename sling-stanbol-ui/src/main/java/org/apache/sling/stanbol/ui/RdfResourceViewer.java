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
import org.apache.sling.stanbol.commons.Utils;


@Component(immediate=true, metatype=false)
@Service(Object.class)
@Properties({
    @Property(name="service.description", value="A slingrs handler for the .rdf extension returning the context from the enhancement graph"),
    @Property(name="service.vendor", value="The Apache Software Foundation"),
    @Property(name="sling.servlet.extensions", value="rdf"),
    @Property(name="sling.servlet.resourceTypes", value="sling/servlet/default"),
    @Property(name="sling.ws.rs", boolValue=true)
})
@SuppressWarnings("serial")
public class RdfResourceViewer extends SlingSafeMethodsServlet {

	
	@Reference
	private TcManager tcManager;
	
	@GET
	@Produces("application/rdf+xml")
	public GraphNode getPage(@Context UriInfo uriInfo, @Context SlingHttpServletRequest request) {
		//SlingHttpServletRequest slingRequest =
		Resource resource = request.getResource();
		if (ResourceUtil.isNonExistingResource(resource)) {
            throw new ResourceNotFoundException(
                resource.getPath(), "No resource found");
        }
		UriRef uri = Utils.getUri(resource.getPath());
		GraphNode gn = new GraphNode(uri, Utils.getEnhancementMGraph(tcManager));
		return gn;
	}
	

}
