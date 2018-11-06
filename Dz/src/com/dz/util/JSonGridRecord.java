package com.dz.util;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class JSonGridRecord {

private Map < String, Object > columnsList = new HashMap();
	
	public void addColumn( String columnName, Object columnValue )
	{
		this.columnsList.put( columnName, columnValue );
	}

	public Map<String, Object> getColumnsList() 
	{
		return columnsList;
	}
}
