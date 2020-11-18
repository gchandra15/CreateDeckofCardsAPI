package com.shs.api.utlities;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class CreateJsonRequestBody {
	public static String createJsonPayload(JSONObject obj, String valueMain, String newValue,Map<String,String> mp) throws Exception {
		// We need to know keys of Jsonobject
		JSONObject json = new JSONObject();
		Iterator iterator = obj.keys();
		String key = null;
		while (iterator.hasNext()) {
			key = (String) iterator.next();
			// if object is just string we change value in key
			if ((obj.optJSONArray(key) == null) && (obj.optJSONObject(key) == null)) {
				if ((obj.get(key).toString().equals(valueMain))) {
					// put new value
					obj.put(key, newValue);
					return obj.toString();
				}
			}

			// if it's jsonobject
			if (obj.optJSONObject(key) != null) {
				createJsonPayload(obj.getJSONObject(key), valueMain, newValue,mp);
			}

			// if it's jsonarray
			if (obj.optJSONArray(key) != null) {
				JSONArray jArray = obj.getJSONArray(key);
				for (int i = 0; i < jArray.length(); i++) {
					createJsonPayload(jArray.getJSONObject(i), valueMain, newValue,mp);
				}
			}
		}
		return obj.toString();
	}

	public static void main(String args[]) throws Exception {
		// String resourceName = "C:\\Home
		// Services\\Automation_Scripts\\parts\\PracticeJava\\src\\main\\resources\\sample.json";
		File initialFile = new File(
				"C:\\Home Services\\Automation_Scripts\\parts\\PracticeJava\\src\\main\\resources\\sample.json");
		InputStream targetStream = new FileInputStream(initialFile);
		JSONTokener tokener = new JSONTokener(targetStream);
		JSONObject object = new JSONObject(tokener);
		//function(object, "unitNumber", "00896");
	}

}
