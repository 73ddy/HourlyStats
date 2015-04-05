package com.plabro.unstructured.deserializer;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.plabro.unstructured.model.Unstructured;
import com.plabro.unstructured.model.UnstructuredJson;

/**
 * A deserializer class for handing the ambiguous and unstructured portions of
 * data.
 * 
 * @author gaurav
 */
public class UnstructuredSerializer implements JsonDeserializer<Unstructured> {

	@Override
	public Unstructured deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		if (json.isJsonPrimitive()) {
			return new Unstructured(json.getAsString());
		}

		return context.deserialize(json, UnstructuredJson.class);
	}

}
