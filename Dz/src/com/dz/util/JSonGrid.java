package com.dz.util;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import net.sf.json.JSONArray;

@SuppressWarnings("unchecked")
public class JSonGrid {

	private Map < String, Object > propertiese = new Hashtable();
	
	public void addProperties( String propertyName, Object propertyValue )
	{
		this.propertiese.put( propertyName, propertyValue );
	}

	private List < JSonGridRecord > records = new Vector();
	
	public void addRecord( JSonGridRecord record ) {
		this.records.add( record );
	}
	
	public int size() {
		return this.records.size();
	}
	
	public String toJSonString( String rowsPropertyName )
	{
		Map result = new HashMap();

		Set < String > propertyNames = this.propertiese.keySet();
		for ( String propertyName: propertyNames )
		{
			Object propertyValue = this.propertiese.get( propertyName );
			result.put( propertyName, propertyValue );
		}

		List rows = new Vector();
		result.put( rowsPropertyName, rows );
		for ( JSonGridRecord record: this.records )
		{
			Map line = new HashMap();
			Map < String, Object >  columns = record.getColumnsList();
			Set < String > columnNames = columns.keySet();
			for ( String columnName: columnNames ) {
				Object columnValue = columns.get( columnName );
				line.put( columnName, columnValue );
			}
			rows.add( line );
		}

		JSONArray jsonObject =  JSONArray.fromObject( result );
		String jsonString = jsonObject.toString().substring( 1, jsonObject.toString().length() - 1 );
		return jsonString;
	}
	
	public String toJSonString( String rowsPropertyName ,int start, int limit)
	{
		Map result = new HashMap();

		Set < String > propertyNames = this.propertiese.keySet();
		for ( String propertyName: propertyNames )
		{
			Object propertyValue = this.propertiese.get( propertyName );
			result.put( propertyName, propertyValue );
		}

		List rows = new Vector();
		result.put( rowsPropertyName, rows );
		
		int temp = 0;
		for ( JSonGridRecord record: this.records )
		{
			if(temp >= start){
				if(rows.size() < limit){
					Map line = new HashMap();
					Map < String, Object >  columns = record.getColumnsList();
					Set < String > columnNames = columns.keySet();
					for ( String columnName: columnNames ) {
						Object columnValue = columns.get( columnName );
						line.put( columnName, columnValue );
					}
					rows.add( line );
				}
			}
			
			temp++;
		}

		JSONArray jsonObject =  JSONArray.fromObject( result );
		String jsonString = jsonObject.toString().substring( 1, jsonObject.toString().length() - 1 );
		return jsonString;
	}
}
