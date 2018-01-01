package com.stackroute.datamunger.query;

import java.util.HashMap;

import com.stackroute.datamunger.query.parser.QueryParameter;
import com.stackroute.datamunger.query.parser.QueryParser;
import com.stackroute.datamunger.reader.CsvAggregateQueryProcessor;
import com.stackroute.datamunger.reader.CsvGroupByAggregateQueryProcessor;
import com.stackroute.datamunger.reader.CsvGroupByQueryProcessor;
import com.stackroute.datamunger.reader.CsvQueryProcessor;
import com.stackroute.datamunger.reader.QueryProcessingEngine;

public class Query {
	HashMap resultset;

	/*
	 * This method will: 1.parse the query and populate the QueryParameter
	 * object 2.Based on the type of query, it will select the appropriate Query
	 * processor. In this example, we are going to work with only one Query
	 * Processor which is CsvQueryProcessor, which can work with select queries
	 * containing zero, one or multiple conditions
	 */
	public HashMap executeQuery(String queryString) {

		/* instantiate QueryParser class */
		QueryParser queryParser = new QueryParser();
		/*
		 * call parseQuery() method of the class by passing the queryString
		 * which will return object of QueryParameter
		 */
		QueryParameter queryParameter = queryParser.parseQuery(queryString);

		/*
		 * Check for Type of Query based on the QueryParameter object. In this
		 * assignment, we will process queries containing zero, one or multiple
		 * where conditions i.e. conditions, aggregate functions, order by,
		 * group by clause
		 */

		switch (queryParameter.getQUERY_TYPE()) {
		case "Simple query":
		case "where": {
			CsvQueryProcessor queryProcessor = new CsvQueryProcessor();
			resultset = queryProcessor.getResultSet(queryParameter);
			break;
		}
		case "group by": {
			CsvGroupByQueryProcessor queryProcessor = new CsvGroupByQueryProcessor();
			resultset = queryProcessor.getResultSet(queryParameter);
			break;
		}
		case "group by with Aggregate": {
			CsvGroupByAggregateQueryProcessor queryProcessor = new CsvGroupByAggregateQueryProcessor();
			resultset = queryProcessor.getResultSet(queryParameter);
			break;
		}
		case "Aggregate": {
			CsvAggregateQueryProcessor queryProcessor = new CsvAggregateQueryProcessor();
			resultset = queryProcessor.getResultSet(queryParameter);
			break;
		}
		}
		/*
		 * call the getResultSet() method of CsvQueryProcessor class by passing
		 * the QueryParameter Object to it. This method is supposed to return
		 * resultSet which is a HashMap
		 */

		return resultset;
	}

}
