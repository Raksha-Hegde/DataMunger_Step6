package com.stackroute.datamunger;

import java.util.Scanner;

import com.stackroute.datamunger.query.Query;
import com.stackroute.datamunger.writer.JsonWriter;

public class DataMunger {
	private static final Scanner SCANNER_OBJ = new Scanner(System.in);

	public static void main(String[] args) {
		// read the query from the user
		String queryString = SCANNER_OBJ.nextLine();

		Query query = new Query();
		JsonWriter writer = new JsonWriter();
		/*
		 * call executeQuery() method of Query class to get the resultSet. Pass
		 * this resultSet as parameter to writeToJson() method of JsonWriter
		 * class to write the resultSet into a JSON file
		 */
		if (writer.writeToJson(query.executeQuery(queryString))) {
			System.out.println("Output written to data/result.json");
		} else {
			System.out.println("Output not written to data/result.json");
		}

	}
}
