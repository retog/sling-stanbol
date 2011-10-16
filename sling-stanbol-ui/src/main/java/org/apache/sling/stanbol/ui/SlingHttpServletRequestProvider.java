package org.apache.sling.stanbol.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;

@Component(immediate=true, metatype=false)
@Service(Object.class)
@Property(name="javax.ws.rs", boolValue=true)
@Provider
public class SlingHttpServletRequestProvider implements ContextResolver<SlingHttpServletRequest> {

	@Context HttpServletRequest httpServletRequest;
	
    public SlingHttpServletRequest getContext(Class<?> type) {
        if (type.equals(SlingHttpServletRequest.class)) {
        	HttpServletRequestWrapper wrapper = (HttpServletRequestWrapper)httpServletRequest;
        	SlingHttpServletRequest slingHttpServletRequest = (SlingHttpServletRequest) wrapper.getRequest();
            return slingHttpServletRequest;
        }
        return null;
    }
}
