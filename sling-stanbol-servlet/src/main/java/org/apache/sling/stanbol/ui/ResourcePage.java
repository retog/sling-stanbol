package org.apache.sling.stanbol.ui;

import javax.jcr.Node;

import org.apache.clerezza.rdf.utils.GraphNode;

public class ResourcePage {
	
	private Node jcrNode;
	private boolean vieEditorEnabled;
	private GraphNode node;

	protected ResourcePage(GraphNode node, Node jcrNode, boolean vieEditorEnabled) {
		super();
		this.vieEditorEnabled = vieEditorEnabled;
		this.node = node;
		this.jcrNode = jcrNode;
	}

	public boolean isVieEditorEnabled() {
		return vieEditorEnabled;
	}

	public GraphNode getNode() {
		return node;
	}

	public Node getJcrNode() {
		return jcrNode;
	}

}
