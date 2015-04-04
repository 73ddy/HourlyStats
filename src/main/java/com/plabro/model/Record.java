package com.plabro.model;

import java.io.Serializable;

import javax.persistence.Entity;

@Entity
public class Record implements Serializable, Comparable<Record> {
	private static final long serialVersionUID = -8324366382592740797L;
	Long timestamp;
	RecordDetails recordDetails;

	public Record() {

	}
	
	public Record(long timestamp) {
		this.timestamp = Long.valueOf(timestamp);
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public RecordDetails getRecordDetails() {
		return recordDetails;
	}

	public void setRecordDetails(RecordDetails recordDetails) {
		this.recordDetails = recordDetails;
	}

	@Override
	public int compareTo(Record o) {
		int returnVal;
		if (null == timestamp && null == o.getTimestamp()) {
			returnVal = 0;
		} else {
			if (null != timestamp) {
				returnVal = timestamp.compareTo(o.getTimestamp());
			} else {
				returnVal = o.getTimestamp().compareTo(timestamp);
			}
		}
		return returnVal;
	}
}
