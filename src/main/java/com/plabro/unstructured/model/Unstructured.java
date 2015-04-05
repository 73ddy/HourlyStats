package com.plabro.unstructured.model;

/**
 * For keeping all the unstructured data.
 * 
 * @author gaurav
 */
public class Unstructured {
	private String value;

	public Unstructured() {
	}

	public Unstructured(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
