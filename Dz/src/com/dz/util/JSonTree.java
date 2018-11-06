package com.dz.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import net.sf.json.JSONArray;

@SuppressWarnings("unchecked")
public class JSonTree {

	  private List < JSonTreeNode > nodeList = new Vector();
		
		public void addNode( JSonTreeNode node ) 
		{
			this.nodeList.add( node );
		}
		
		public String toJSonString()
		{
			List list = new Vector();
			for ( JSonTreeNode node: this.nodeList ) {
				this.dealWithNode( node, list );
			}
			JSONArray jsonObject =  JSONArray.fromObject( list );
			String jsonString = jsonObject.toString();
			return jsonString;
		}
		
		private void dealWithNode( JSonTreeNode node, List < Map < String, Object > > childrenList ) 
		{
			Map nodeMap = new HashMap();
			childrenList.add( nodeMap );
			Set < String > propertyNames = node.getProperties().keySet();
			for ( String propertyName: propertyNames ) {
				Object propertyValue = node.getProperties().get( propertyName );
				nodeMap.put( propertyName, propertyValue );
			}
			List < JSonTreeNode > children = node.getChildren();
			if ( children.size() == 0 )
			{
				if ( false == nodeMap.containsKey( "leaf" ) )
				{
					nodeMap.put( "leaf", true );
				}
			} else {
				if ( false == nodeMap.containsKey( "leaf" ) ) 
				{
					nodeMap.put( "leaf", false );
				}
				List < Map < String, Object > > curChildrenList = new Vector();
				nodeMap.put( "children", curChildrenList );

				for ( JSonTreeNode childNode: children )
				{
					this.dealWithNode( childNode, curChildrenList );
				}
		  }
}
}
