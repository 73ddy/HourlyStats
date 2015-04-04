package com.plabro.model;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.Entity;

import com.google.gson.annotations.SerializedName;

@Entity
public class RecordDetails implements Serializable {
	private static final long serialVersionUID = 3521323304553995121L;
	@SerializedName("D")
	private Double responseTimestamp;
	@SerializedName("T")
	private Double responseTime;
	@SerializedName("Q")
	private Map<String, String> query;
	@SerializedName("R")
	private Map<String, String> response;

	public RecordDetails() {

	}

	public Map<String, String> getQuery() {
		return query;
	}

	public void setQuery(Map<String, String> query) {
		this.query = query;
	}

	public Map<String, String> getResponse() {
		return response;
	}

	public void setResponse(Map<String, String> response) {
		this.response = response;
	}

	public Double getResponseTimestamp() {
		return responseTimestamp;
	}

	public void setResponseTimestamp(Double responseTimestamp) {
		this.responseTimestamp = responseTimestamp;
	}

	public Double getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(Double responseTime) {
		this.responseTime = responseTime;
	}
}
