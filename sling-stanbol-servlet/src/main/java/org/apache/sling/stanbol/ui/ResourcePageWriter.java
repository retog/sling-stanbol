package org.apache.sling.stanbol.ui;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.jcr.RepositoryException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;

@Component(immediate=true, metatype=false)
@Service(Object.class)
@Property(name="javax.ws.rs", boolValue=true)
@Provider
public class ResourcePageWriter implements MessageBodyWriter<ResourcePage> {

	public boolean isWriteable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return ResourcePage.class.isAssignableFrom(type);
	}

	public long getSize(ResourcePage t, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	public void writeTo(ResourcePage t, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException,
			WebApplicationException {
		PrintWriter out = new PrintWriter(entityStream);
		try {
			writePage(t, out);
		} catch (RepositoryException e) {
			throw new WebApplicationException();
		}
		out.flush();
		
	}

	protected void writePage(ResourcePage t, PrintWriter out) throws IOException, RepositoryException {
		out.println("<html>");
		out.println("<head>");
		printHead(t, out);
		out.println("</head>");
		out.println("<body>");
		printBody(t, out);
		out.println("</body>");
		out.println("</html>");
	}

	protected void printBody(ResourcePage t, PrintWriter out) throws IOException, RepositoryException {
		if (t.isVieEditorEnabled()) {
			out.println("<div  xmlns:sioc     = \"http://rdfs.org/sioc/ns#\"");
			out.println("         xmlns:schema   = \"http://www.schema.org/\"");
			out.println("         xmlns:enhancer = \"http://fise.iks-project.eu/ontology/\"");
			out.println("         xmlns:dc       = \"http://purl.org/dc/terms/\">");
			out.println("        <div class=\"panel\" id=\"webview\">");
			out.println("");
			out.println("            <button class=\"enhanceButton\">Enhance!</button>");
			out.println("");
			out.println("            <button class=\"acceptAllButton\" style=\"display:none;\">Accept all</button>");
			out.println("            <article typeof=\"schema:CreativeWork\" about=\"http://stanbol.apache.org/enhancertest\">");
			out.println("                <div property=\"sioc:content\" id=\"content\">");
			final String content = t.getJcrNode().getProperty("jcr:content/jcr:data").getString();
			out.println(content);
			out.println("                </div>");
			out.println("            </article>");
			out.println("            <button class=\"enhanceButton\">Enhance!</button>");
			out.println("            <button class=\"acceptAllButton\" style=\"display:none;\">Accept all</button>");
			out.println("");
			out.println("        </div>");
			out.println("        <div id=\"loadingDiv\"><img src=\"spinner.gif\"/></div>");
			out.println("    </div>");
		}
		out.println("hello world");
		
	}

	protected void printHead(ResourcePage t, PrintWriter out) throws IOException {
		out.println("<title>"+getTitle(t)+"</title>");
		if (t.isVieEditorEnabled()) {
			Reader in = new InputStreamReader(getClass().getResourceAsStream("vie-html-head-section.txt"));
			for (int ch = in.read(); ch != -1; ch = in.read()) {
				out.write(ch);
			}
			
		}
	}

	protected String getTitle(ResourcePage t) {
		return "unknown title";
	}

}
