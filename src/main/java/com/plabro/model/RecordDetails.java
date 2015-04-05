package com.plabro.model;

import java.io.Serializable;

import javax.persistence.Entity;

import com.google.gson.annotations.SerializedName;
import com.plabro.unstructured.model.Unstructured;

@Entity
public class RecordDetails implements Serializable {
	private static final long serialVersionUID = 3521323304553995121L;
	@SerializedName("D")
	private Double responseTimestamp;
	@SerializedName("T")
	private Double responseTime;
	@SerializedName("Q")
	private Unstructured query;
	@SerializedName("R")
	private Unstructured response;

	public RecordDetails() {

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

	public Unstructured getQuery() {
		return query;
	}

	public void setQuery(Unstructured query) {
		this.query = query;
	}

	public Unstructured getResponse() {
		return response;
	}

	public void setResponse(Unstructured response) {
		this.response = response;
	}
}
