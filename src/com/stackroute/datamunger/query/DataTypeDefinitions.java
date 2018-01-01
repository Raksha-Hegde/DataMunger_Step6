package com.stackroute.datamunger.query;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * implementation of DataTypeDefinitions class. This class contains a method getDataTypes() 
 * which will contain the logic for getting the datatype for a given field value. This
 * method will be called from QueryProcessors.   
 * In this assignment, we are going to use Regular Expression to find the 
 * appropriate data type of a field. 
 * Integers: should contain only digits without decimal point 
 * Double: should contain digits as well as decimal point 
 * Date: Dates can be written in many formats in the CSV file. 
 * However, in this assignment,we will test for the following date formats('dd/mm/yyyy',
 * 'mm/dd/yyyy','dd-mon-yy','dd-mon-yyyy','dd-month-yy','dd-month-yyyy','yyyy-mm-dd')
 */
public class DataTypeDefinitions {
	private String dataType;

	public String getDataType(String columnData) {

		String dateType = "java.util.Date";
		if (columnData.isEmpty()) {
			dataType = "java.lang.Object";

		} else {
			try {
				// checking for Integer
				Integer.parseInt(columnData);
				dataType = "java.lang.Integer";

			} catch (NumberFormatException e) {
				try {
					// checking for floating point numbers
					Float.parseFloat(columnData);
					dataType = "java.lang.Float";
				} catch (NumberFormatException e1) {

					// checking for date format dd/mm/yyyy
					if (columnData.matches("(0?[1-9]|[1-2][0-9]|3[0-1])/(0?[1-9]|1[0-2])/[0-2][0-9]{3}")) {
						dataType = dateType;
					}

					// checking for date format mm/dd/yyyy
					else if (columnData.matches("(0?[1-9]|1[0-2])/(0?[1-9]|[1-2][0-9]|3[0-1])/[0-2][0-9]{3}")) {
						dataType = dateType;
					}

					// checking for date format dd-mon-yy
					else if (columnData.matches(
							"(0?[1-9]|[1-2][0-9]|3[0-1])-(jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec)-[0-9]{2}")) {
						dataType = dateType;

					}

					// checking for date format dd-mon-yyyy
					else if (columnData.matches(
							"(0?[1-9]|[1-2][0-9]|3[0-1])-(jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec)-[0-2][0-9]{3}")) {
						dataType = dateType;

					}

					// checking for date format dd-month-yy
					else if (columnData.matches(
							"(0?[1-9]|[1-2][0-9]|3[0-1])-(january|febrauary|march|april|may|june|july|august|september|october|november|december)-[0-9]{2}")) {
						dataType = dateType;

					}
					// checking for date format dd-month-yyyy
					else if (columnData.matches(
							"(0?[1-9]|[1-2][0-9]|3[0-1])-(january|febrauary|march|april|may|june|july|august|september|october|november|december)-[0-2][0-9]{3}")) {
						dataType = dateType;

					}
					// checking for date format yyyy-mm-dd
					else if (columnData.matches("[0-2][0-9]{3}-(0?[1-9]|1[0-2])-(0?[1-9]|[1-2][0-9]|3[0-1])")) {
						dataType = dateType;
					} else {
						dataType = "java.lang.String";
					}

				}

			}
		}

		return dataType;

	}

}
