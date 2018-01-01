package com.stackroute.datamunger.writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonWriter {
	private boolean flag;
	private BufferedWriter bufferedWriter;

	/*
	 * this method will write the resultSet object into a JSON file. On
	 * successful writing, the method will return true, else will return false
	 */
	public boolean writeToJson(Map resultSet) {

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String result = gson.toJson(resultSet);

		/*
		 * write JSON string to data/result.json file. As we are performing File
		 * IO, consider handling exception
		 */

		try {
			bufferedWriter = new BufferedWriter(new FileWriter("data/result.json"));
			bufferedWriter.write(result);
			System.out.println("Result Set");
			System.out.println(result);
			/*
			 * return true if file writing is successful
			 */
			flag = true;
			/*
			 * close BufferedWriter object
			 */
			bufferedWriter.flush();
			bufferedWriter.close();

		} catch (IOException e) {
			/* return false if file writing is failed */
			flag = false;
		}
		return flag;
	}
}
