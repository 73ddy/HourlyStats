package com.plabro.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.concurrent.Callable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.plabro.model.Record;
import com.plabro.model.RecordDetails;

public class DataGeneratorThread implements Callable<Boolean> {

	private static final Type GSON_LOG_TYPE = new TypeToken<Map<String, RecordDetails>>() {
	}.getType();
	private static final DateFormat dateFormater = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss,SSS");

	private static final DataStore dataStore = DataStore.getInstance();
	
	private String resourceFileName;
	
	public DataGeneratorThread(final String resourceFileName) {
		this.resourceFileName = resourceFileName;
	}
	
	@Override
	public Boolean call() throws Exception {
		boolean status = false; 
		try {
			populateData();
			status = true;
		} catch (ParseException e) {
			// not using a logger due to limited time
			System.out.println("Fatal level log: Exception occurred while parsing the timestamp string.");
			e.printStackTrace();
		} finally {
			System.out.println("Resource file - " + resourceFileName + " has been proessed with status - " + status);
		}
		return status;
	}

	/**
	 * Throwing the parse exception as it is unexpected. And data needs to be
	 * validated if this occurs.
	 * 
	 * @throws ParseException
	 */
	private void populateData() throws ParseException {
		final Gson gson = new Gson();
		final InputStream inputStream = Record.class
				.getResourceAsStream(resourceFileName);
		final BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputStream));

		String jsonString;
		JsonReader jsonReader = null;
		Map<String, RecordDetails> map = null;
		Record record = null;

		try {
			while ((jsonString = reader.readLine()) != null) {
				jsonReader = new JsonReader(new StringReader(jsonString));
				jsonReader.setLenient(true);
				map = gson.fromJson(jsonReader, GSON_LOG_TYPE);

				record = new Record();

				for (final String key : map.keySet()) {
					record.setTimestamp(getTimeMillis(key));
					record.setRecordDetails(map.get(key));
				}

				dataStore.addRecord(record);
				dataStore.addQueryFrquency(record.getTimestamp()/3600000); // hour since epoch
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Total records - " + dataStore.getSize());
		System.out.println("Total hours for hour distribution - " + dataStore.getDistributionSize());
	}

	/**
	 * This method returns the timestamp string as millis.
	 * 
	 * @param key
	 * @return
	 * @throws ParseException
	 */
	private long getTimeMillis(final String timestamp) throws ParseException {
		return dateFormater.parse(timestamp).getTime();
	}
	
	public String getResourceFileName() {
		return resourceFileName;
	}

	public void setResourceFileName(String resourceFileName) {
		this.resourceFileName = resourceFileName;
	}
}
