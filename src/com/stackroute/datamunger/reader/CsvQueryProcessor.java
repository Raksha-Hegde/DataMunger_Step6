package com.stackroute.datamunger.reader;

import java.io.BufferedReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.stackroute.datamunger.query.DataSet;
import com.stackroute.datamunger.query.DataTypeDefinitions;
import com.stackroute.datamunger.query.Filter;
import com.stackroute.datamunger.query.Header;
import com.stackroute.datamunger.query.Row;
import com.stackroute.datamunger.query.RowDataTypeDefinitions;
import com.stackroute.datamunger.query.parser.QueryParameter;
import com.stackroute.datamunger.query.parser.Restriction;

//this class will read from CSV file and process and return the resultSet
public class CsvQueryProcessor implements QueryProcessingEngine {
	private static BufferedReader bufferedReader;
	private static String line;
	private Header header;
	private RowDataTypeDefinitions rowDataTypeDefinitions;
	DataTypeDefinitions dataTypeDefinitions;
	DataSet dataSet = new DataSet();
	Row row;
	private long rowID = 1;
	Filter filter = new Filter();
	List<Boolean> flag;
	String condition, propertyName, propertyValue;
	List<String> logicalOperators;
	Boolean logicFlag = true;

	/*
	 * This method will take QueryParameter object as a parameter which contains
	 * the parsed query and will process and populate the ResultSet
	 */
	public DataSet getResultSet(QueryParameter queryParameter) {

		try {
			bufferedReader = new BufferedReader(new FileReader(queryParameter.getFile()));
			/*
			 * read the first line which contains the header and populate the
			 * header Map object
			 */
			String[] headerValue = getHeader(bufferedReader.readLine());

			/*
			 * read the next line which contains the first row of data and
			 * populate the RowDataTypeDefinition Map object.
			 */
			getColumnType(bufferedReader.readLine());
			/*
			 * reset the buffered reader and read first line
			 */
			bufferedReader = new BufferedReader(new FileReader(queryParameter.getFile()));
			bufferedReader.readLine();

			/*
			 * read one line at a time from the CSV file
			 */

			while ((line = bufferedReader.readLine()) != null) {
				/*
				 * once we have read one line, we will split it into a String[]
				 */
				String[] columnValue = line.split(",", -1);

				/*
				 * if there are where condition(s) in the query, test the row
				 * fields against those conditions to check whether the selected
				 * row satisfies the conditions
				 */
				if (queryParameter.getQUERY_TYPE().equalsIgnoreCase("where")) {

					List<Restriction> restrictions = queryParameter.getRestrictions();
					Iterator<Restriction> itr = restrictions.iterator();
					Restriction restriction = new Restriction();
					/*
					 * from QueryParameter object, read one condition at a time
					 * and evaluate the same.
					 */
					flag = new ArrayList<Boolean>();
					boolean flags = false;
					while (itr.hasNext()) {
						restriction = itr.next();
						propertyName = restriction.getPropertyName();
						condition = restriction.getCondition();
						propertyValue = restriction.getPropertyValue();

						flags = filter.evaluateExpression(condition, propertyValue,
								columnValue[header.get(propertyName) - 1],
								rowDataTypeDefinitions.get(header.get(propertyName)));
						flag.add(flags);

					}

					/*
					 * check for multiple conditions
					 */
					logicalOperators = queryParameter.getLogicalOperators();
					if (logicalOperators != null) {
						logicFlag = filter.evaluateConditions(flag, logicalOperators);
					} else {
						logicFlag = flags;
					}

				}
				// Select Clause
				if (logicFlag) {

					row = new Row();
					if (queryParameter.getFields().isEmpty() | queryParameter.getFields().contains("*")) {
						for (int i = 0; i < headerValue.length; i++) {
							row.put(headerValue[i], columnValue[i]);
						}

					} else {
						Iterator<String> itrString = queryParameter.getFields().iterator();

						while (itrString.hasNext()) {
							String field = itrString.next();
							row.put(field, columnValue[header.get(field) - 1]);
						}

					}

					dataSet.put(rowID++, row);
				}

			}

		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		}

		/* return dataset object */
		return dataSet;
	}

	/*
	 * implementation of getHeader() method. We will have to extract the headers
	 * from the first line of the file.
	 */
	private String[] getHeader(String line) throws IOException {

		int counter = 1;
		header = new Header();
		String[] headerValue = line.split(",");
		for (String head : headerValue) {
			header.put(head, counter++);
		}
		return headerValue;
	}

	/*
	 * implementation of getColumnType() method. To find out the data types, we
	 * will read the first line from the file and extract the field values from
	 * it.
	 */
	public void getColumnType(String line) throws IOException {
		int counter = 1;
		String[] columnData = line.split(",", -1);
		rowDataTypeDefinitions = new RowDataTypeDefinitions();
		dataTypeDefinitions = new DataTypeDefinitions();
		for (String column : columnData) {
			rowDataTypeDefinitions.put(counter++, dataTypeDefinitions.getDataType(column));

		}
		return;
	}

}
