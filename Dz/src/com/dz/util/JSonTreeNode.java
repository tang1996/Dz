package com.dz.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class JSonTreeNode {
	
	private List < JSonTreeNode > children = new Vector< JSonTreeNode >();
	
	public void addChild( JSonTreeNode child ) {
		this.children.add( child );
	}
	
	private Map < String, Object > properties = new HashMap< String, Object>();
	
	public void addProperty( String propertyName, Object propertyValue ) {
		this.properties.put( propertyName, propertyValue );
	}
	
	public List<JSonTreeNode> getChildren() {
		return children;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}
}
