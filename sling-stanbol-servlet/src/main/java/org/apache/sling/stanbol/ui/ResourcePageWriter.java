package org.apache.sling.stanbol.ui;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

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
		writePage(t, out);
		out.flush();
		
	}

	protected void writePage(ResourcePage t, PrintWriter out) throws IOException {
		out.println("<html>");
		out.println("<head>");
		printHead(t, out);
		out.println("</head>");
		out.println("<body>");
		printBody(t, out);
		out.println("</body>");
		out.println("</html>");
	}

	protected void printBody(ResourcePage t, PrintWriter out) {
		if (t.isVieEditorEnabled()) {
			out.println("hello vie");
			out.println("<form class=\"hform\" method=\"POST\" action=\".\" enctype=\"multipart/form-data\">	   ");
			out.println("				<p><label>Title</label>");
			out.println("					<input name=\"title\" id=\"title\" type=\"text\" size=\"80\" value=\"TESTTITLE\"></p>");
			/*out.println("					<% //there seems to be a problem with empty text");
			out.println("					var posttext = getCurrentNodeValue(\"posttext\")");
			out.println("					if (posttext == \"\") {");
			out.println("						posttext = \" \"");
			out.println("					}");
			out.println("					%>");*/
			out.println("					<div id=\"myarticle\" typeof=\"http://rdfs.org/sioc/ns#Post\"");
			out.println("						about=\"TESTURI\">");
			out.println("				        <div property=\"sioc:content\"><%=  posttext %></div>");
			out.println("				    </div>");
			out.println("					");
			out.println("					<input name=\"posttext\" id=\"posttext\" type=\"hidden\" size=\"80\" value=\"<%= posttext %>\">");
			out.println("					");
			out.println("					<p><label>File</label><input type=\"file\" name=\"attachments/*\"/></p>");
			out.println("					<input type=\"hidden\" name=\"created\"/>");
			/*out.println("					<input name=\":redirect\" type=\"hidden\" value=\"/content/espblog/posts.admin.html\"/>");*/
			out.println("				<input type=\"submit\" value=\"Post\" class=\"button\">");
			out.println("			</form>");
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
