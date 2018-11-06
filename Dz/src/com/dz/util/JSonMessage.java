package com.dz.util;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;

@SuppressWarnings("unchecked")
public class JSonMessage {
	
	@SuppressWarnings("rawtypes")
	private Map properties = new HashMap();
	
	public static String RESULT_FLAG = "success";
	
	public static boolean RESULT_OK = true;
	
	public static boolean RESULT_FAIL = false;

	public void addProperty( String propertyKey, Object propertyValue )
	{
		this.properties.put( propertyKey, propertyValue );
	}
	
	public String toJSonString() {
		JSONArray jsonObject =  JSONArray.fromObject( this.properties );
		return jsonObject.toString().substring( 1, jsonObject.toString().length() - 1 );
	}
}
