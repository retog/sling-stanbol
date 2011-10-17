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
package org.apache.sling.stanbol.commons;

import java.io.IOException;
import java.io.StringReader;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.clerezza.rdf.core.UriRef;
import org.apache.clerezza.rdf.core.access.LockableMGraph;
import org.apache.clerezza.rdf.core.access.NoSuchEntityException;
import org.apache.clerezza.rdf.core.access.TcManager;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Utils {

	public static LockableMGraph getEnhancementMGraph(TcManager tcManager) {
		try {
			return tcManager.getMGraph(Constants.enhancementMGraphUri);
		} catch (NoSuchEntityException e) {
			synchronized (tcManager) {
				try {
					return tcManager.getMGraph(Constants.enhancementMGraphUri);
				} catch (NoSuchEntityException e1) {
					return tcManager.createMGraph(Constants.enhancementMGraphUri);
				}
			}
		}
	}
	
	public static UriRef getUri(String path) {
		return new UriRef("urn:x-localinstance:"+path);
	}
	
	public static Document getXMLDocument(Node node) throws  RepositoryException, IOException {
		final String content = node.getProperty("jcr:content/jcr:data").getString();
		InputSource inputSource = new InputSource(new StringReader(content));
		Document doc;
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputSource);
		} catch (SAXException e) {
			throw new IOException(e);
		} catch (ParserConfigurationException e) {
			throw new IOException(e);
		}
		return doc;
	}
}
