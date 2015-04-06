package com.plabro.unstructured.model;

import java.util.Map;


/**
 * This class represents any unstructured json which may or may not be a json.
 * 
 * @author gaurav
 */
public class UnstructuredJson extends Unstructured {
	Map<Object, Object> jsonValue;

	public Map<Object, Object> getJsonValue() {
		return jsonValue;
	}

	public void setJsonValue(Map<Object, Object> jsonValue) {
		this.jsonValue = jsonValue;
	}
}
