package com.plabro.dao;

import java.util.Collections;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

import com.plabro.model.Record;

/**
 * This class will serve as my database and data access object as well.
 * 
 * @author gaurav
 */
public class DataStore {
	private DataStore() {
	};

	private static final DataStore instance = new DataStore();
	/*
	 * For storing all the records.
	 * 
	 * Choice of DS: For supporting time range queries on this data structure,
	 * leveraging the tail, head and subset functions. The thread safe part
	 * allows several producers to keep feeding data to this DS.
	 */
	private NavigableSet<Record> recordSet = new ConcurrentSkipListSet<Record>();
	/*
	 * For storing the query distribution by hour.
	 * 
	 * Choice of DS: Mapped by hour in year. Usually this DS will be queried for
	 * distribution over a given range of hours.
	 */
	private NavigableMap<Integer, Integer> queryFrequencyDistributionByHour = new ConcurrentSkipListMap<Integer, Integer>();

	public static DataStore getInstance() {
		return instance;
	}

	public void addRecord(final Record record) {
		this.recordSet.add(record);
	}

	public void flush() {
		this.recordSet.clear();
		this.queryFrequencyDistributionByHour.clear();
	}

	public SortedSet<Record> getTailSet(long timestamp) {
		Record record = new Record(timestamp);
		return this.recordSet.tailSet(record);
	}

	public SortedSet<Record> getHeadSet(long timestamp) {
		Record record = new Record(timestamp);
		return this.recordSet.headSet(record);
	}

	public SortedSet<Record> getSubSet(long minTimestamp, long maxTimestamp) {
		Record floorT = new Record(minTimestamp);
		Record cielT = new Record(maxTimestamp);
		return this.recordSet.subSet(floorT, cielT);
	}

	public Set<Record> getAllRecords() {
		return Collections.unmodifiableSet(this.recordSet);
	}

	public int getSize() {
		return this.recordSet.size();
	}
	
	
	public void addQueryFrquency(int hour) {
		if (queryFrequencyDistributionByHour.containsKey(hour)) {
			queryFrequencyDistributionByHour.put(hour, queryFrequencyDistributionByHour.get(hour) + 1);
		} else {
			queryFrequencyDistributionByHour.put(hour, 1);
		}
	}
	
	public SortedMap<Integer, Integer> getTrailDistribution(int hour) {
		return queryFrequencyDistributionByHour.tailMap(hour);
	}
	
	public SortedMap<Integer, Integer> getHeadDistribution(int hour) {
		return queryFrequencyDistributionByHour.headMap(hour);
	}
	
	public SortedMap<Integer, Integer> getSubsetDistribution(int minHour, int maxHour) {
		return queryFrequencyDistributionByHour.subMap(minHour, maxHour);
	}
	
	public Map<Integer, Integer> getAllDistributions() {
		return Collections.unmodifiableMap(queryFrequencyDistributionByHour);
	}
	
	public int getDistributionSize() {
		return queryFrequencyDistributionByHour.size();
	}
}