package com.stackroute.datamunger.reader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.stackroute.datamunger.query.DataSet;
import com.stackroute.datamunger.query.DataTypeDefinitions;
import com.stackroute.datamunger.query.Filter;
import com.stackroute.datamunger.query.Header;
import com.stackroute.datamunger.query.Row;
import com.stackroute.datamunger.query.RowDataTypeDefinitions;
import com.stackroute.datamunger.query.parser.QueryParameter;

/* this is the CsvGroupByAggregateQueryProcessor class used for evaluating queries with 
 * aggregate functions and group by clause*/
public class CsvGroupByAggregateQueryProcessor implements QueryProcessingEngine {
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
	public HashMap getResultSet(QueryParameter queryParameter) {

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
				 * row satifies the conditions
				 */

				/*
				 * from QueryParameter object, read one condition at a time and
				 * evaluate the same. For evaluating the conditions, we will use
				 * evaluateExpressions() method of Filter class. Please note
				 * that evaluation of expression will be done differently based
				 * on the data type of the field. In case the query is having
				 * multiple conditions, you need to evaluate the overall
				 * expression i.e. if we have OR operator between two
				 * conditions, then the row will be selected if any of the
				 * condition is satisfied. However, in case of AND operator, the
				 * row will be selected only if both of them are satisfied.
				 */

				/*
				 * check for multiple conditions in where clause for eg: where
				 * salary>20000 and city=Bangalore for eg: where salary>20000 or
				 * city=Bangalore and dept!=Sales
				 */

				/*
				 * if the overall condition expression evaluates to true, then
				 * we need to check for the existence for group by clause in the
				 * Query Parameter. The dataSet generated after processing a
				 * group by clause is completely different from a dataSet
				 * structure(which contains multiple rows of data). In case of
				 * queries containing group by clause, the resultSet will
				 * contain multiple dataSets, each of which will be assigned to
				 * the group by column value i.e. for all unique values of the
				 * group by column, there can be multiple rows associated with
				 * it. Hence, we will use GroupedDataSet<String,Object> to store
				 * the same and not DataSet<Long,Row>. Please note we will
				 * process queries containing one group by column only for this
				 * example.
				 */

				// return groupedDataSet object

			}
		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		}

		// return groupedDataSet object

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
