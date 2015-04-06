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
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.plabro.model.Record;
import com.plabro.model.RecordDetails;
import com.plabro.unstructured.deserializer.UnstructuredSerializer;
import com.plabro.unstructured.model.Unstructured;

public class DataGeneratorThread implements Callable<Boolean> {

	private static final Type GSON_LOG_TYPE = new TypeToken<Map<String, RecordDetails>>() {
	}.getType();
	private static final DateFormat dateFormater = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss,SSS");
	// must be calculated by the Timezone difference from GMT ofthe server which is generating logs.
	private static long TIMESTAMP_CORRECTION = 19800000;
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
			System.out
					.println("Fatal level log: Exception occurred while parsing the timestamp string.");
		} finally {
			System.out.println("Resource file - " + resourceFileName
					+ " has been proessed with status - " + status);
		}
		return status;
	}

	/**
	 * Throwing the parse exception as it is unexpected. And data needs to be
	 * validated if this occurs.
	 * 
	 * @throws ParseException
	 * @throws IOException
	 */
	private void populateData() throws ParseException, IOException {
		Gson gson;
		final InputStream inputStream = Record.class.getClassLoader()
				.getResourceAsStream(resourceFileName);
		final BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputStream));

		String jsonString = null;
		JsonReader jsonReader = null;
		Map<String, RecordDetails> map = null;
		Record record = null;
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Unstructured.class, new UnstructuredSerializer());
		gson = gsonBuilder.create();
		try {
			while ((jsonString = reader.readLine()) != null) {
				jsonReader = new JsonReader(new StringReader(jsonString));
				jsonReader.setLenient(true);

				try {
					map = gson.fromJson(jsonReader, GSON_LOG_TYPE);
				} catch (JsonSyntaxException e) {
					System.out
							.println("The following entry failed to be processed - "
									+ jsonString);
					// resetting gson
					gson = gsonBuilder.create();
					continue;
				}

				
				if (null != map && !map.isEmpty()) {
					record = new Record();
					for (final String key : map.keySet()) {
						if (null != key && !key.isEmpty()) {
							record.setTimestamp(getTimeMillis(key));
							record.setRecordDetails(map.get(key));
						}
					}
					
					dataStore.addRecord(record);
					// hour since epoch
					dataStore.addQueryFrquency((int)(TimeUnit.MILLISECONDS.toHours(record.getTimestamp() + TIMESTAMP_CORRECTION)));				
				} else {
					System.out.println("Please be warned that the following data couldn't be processed - " + jsonString);
				}
			}
		} catch (ParseException e) {
			System.out
					.println("The value under the scanner is - " + jsonString);
			throw e;
		} finally {
			try {
				reader.close();
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Total records - " + dataStore.getSize());
		System.out.println("Total hours for hour distribution - "
				+ dataStore.getDistributionSize());
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
