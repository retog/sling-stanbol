package org.apache.sling.stanbol.ui;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
		httpHeaders.add("Content-Type", "text/html; charset=utf-8");
		Writer writer = new OutputStreamWriter(entityStream, "utf-8");
		PrintWriter out = new PrintWriter(writer);
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
		{
			Reader in = new InputStreamReader(getClass().getResourceAsStream("resource-page-body-head.txt"));
			for (int ch = in.read(); ch != -1; ch = in.read()) {
				out.write(ch);
			}
		}
		if (t.isVieEditorEnabled()) {
			out.println("<h2>Edit and Enhance content</h2>");
			out.println("<div id=\"editArea\" xmlns:sioc     = \"http://rdfs.org/sioc/ns#\"");
			out.println("         xmlns:schema   = \"http://www.schema.org/\"");
			out.println("         xmlns:enhancer = \"http://fise.iks-project.eu/ontology/\"");
			out.println("         xmlns:dc       = \"http://purl.org/dc/terms/\">");
			out.println("        <div class=\"panel\" id=\"webview\">");
			out.println("");
			out.println("");
			out.println("            <button class=\"acceptAllButton\" style=\"display:none;\">Accept all</button>");
			out.println("            <article typeof=\"schema:CreativeWork\" about=\"http://stanbol.apache.org/enhancertest\">");
			out.println("                <div property=\"sioc:content\" id=\"content\">");
			out.println(getContent(t.getJcrNode()));
			out.println("                </div>");
			out.println("            </article>");
			out.println("            <button class=\"enhanceButton\">Enhance!</button>");
			out.println("            <button class=\"acceptAllButton\" style=\"display:none;\">Accept all</button>");
			out.println("            <br/><button class=\"saveButton\">Save</button>");
			out.println("");
			out.println("        </div>");
			out.println("        <div id=\"loadingDiv\"><img src=\"/stanbol/spinner.gif\"/></div>");
			out.println("</div>");
		}
		out.println("<h2>Pre Stored-Metadata</h2>");
		out.println("<iframe width=\"90%\" height=\"30%\" src=\""+t.getJcrNode().getPath()+".rdf?xPropSubj=http://fise.iks-project.eu/ontology/extracted-from\">Sorry your browser is too cool for us.</iframe>");
		{
			Reader in = new InputStreamReader(getClass().getResourceAsStream("resource-page-body-footer.txt"));
			for (int ch = in.read(); ch != -1; ch = in.read()) {
				out.write(ch);
			}
		}
	}

	private String getContent(Node jcrNode) throws IOException, RepositoryException {
		final String content = jcrNode.getProperty("jcr:content/jcr:data").getString();
		try {
			//StreamSource inputSource = new StreamSource(new StringReader(content));
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			inputFactory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);
			Transformer transformer = TransformerFactory.newInstance().newTransformer();

			StringReader xmlReader = new StringReader(content);
			XMLEventReader eventReader = inputFactory.createXMLEventReader(xmlReader);
			StAXSource inputSource = new StAXSource(eventReader);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			Document doc = factory.newDocumentBuilder().newDocument();
			DOMResult result = new DOMResult(doc);
			transformer.transform(inputSource, result);

			Element docElem = doc.getDocumentElement();
			DOMSource domSource;
			if (docElem.getNodeName().equalsIgnoreCase("html")) {
				Element body = (Element) doc.getElementsByTagName("body").item(0);
				doc.renameNode(body, null, "div");
				body.setAttribute("id", "body-replacement");
				/*Element replacementDiv = doc.createElement("div");
				replacementDiv.setAttribute("id", "body-replacement");
				NodeList bodyChildren = body.getChildNodes();
				for (int i = 0; i < bodyChildren.getLength(); i++) {
					body.removeChild(bodyChildren.item(i));
					replacementDiv.appendChild(bodyChildren.item(i));
				}
				domSource = new DOMSource(replacementDiv);*/
				domSource = new DOMSource(body);

			} else {
				domSource = new DOMSource(doc);
			}
			TransformerFactory tf = TransformerFactory.newInstance();
			//Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			//StringWriter out = new StringWriter();
			StreamResult streamResult = new StreamResult(baos);
			transformer.transform(domSource, streamResult);
			return new String(baos.toByteArray(), "utf-8");
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		} catch (TransformerConfigurationException e) {
			throw new RuntimeException(e);
		} catch (TransformerException e) {
			throw new RuntimeException(e);
		} catch (XMLStreamException e) {
			throw new RuntimeException(e);
		}
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
		return "Stanbol Resource Page";
	}

}
