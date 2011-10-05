package org.apache.sling.stanbol.ui;

import org.apache.clerezza.rdf.utils.GraphNode;

public class ResourcePage {
	
	protected ResourcePage(GraphNode node, boolean vieEditorEnabled) {
		super();
		this.vieEditorEnabled = vieEditorEnabled;
		this.node = node;
	}

	private boolean vieEditorEnabled;
	private GraphNode node;

	public boolean isVieEditorEnabled() {
		return vieEditorEnabled;
	}

	public GraphNode getNode() {
		return node;
	}

}
