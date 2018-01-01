package com.stackroute.datamunger.query.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class QueryParser {

	private QueryParameter queryParameter = null;

	/*
	 * this method will parse the queryString and will return the object of
	 * QueryParameter class
	 */
	public QueryParameter parseQuery(String queryString) {

		if (!queryString.isEmpty()) {
			queryParameter = new QueryParameter();
			queryParameter.setFile(getFileName(replaceCharacters(queryString)));
			queryParameter.setBaseQuery(getBaseQuery(replaceCharacters(queryString)));
			queryParameter.setFields(getFields(replaceCharacters(queryString)));
			queryParameter.setQUERY_TYPE("Simple query");
			if (queryString.contains("where")) {
				queryParameter.setRestrictions(getConditions(replaceCharacters(queryString)));
				queryParameter.setLogicalOperators(getLogicalOperators(replaceCharacters(queryString)));
				queryParameter.setQUERY_TYPE("where");
			}

			if (queryString.contains(" order by")) {
				queryParameter.setOrderByFields(getOrderByFields(replaceCharacters(queryString)));
				queryParameter.setQUERY_TYPE("order by");
			}
			if (queryString.contains(" group by")) {
				queryParameter.setGroupByFields(getGroupByFields(replaceCharacters(queryString)));
				queryParameter.setQUERY_TYPE("group by");
			}
			if (queryString.contains("(")) {
				queryParameter.setAggregateFunctions(getAggregateFunctions(replaceCharacters(queryString)));
				String queryType = queryParameter.getQUERY_TYPE();
				if (queryType.equalsIgnoreCase("where") || queryType.equalsIgnoreCase("Simple query")) {
					queryParameter.setQUERY_TYPE("Aggregate");
				} else {
					queryParameter.setQUERY_TYPE(queryType + " with Aggregate");
				}
			}

		}

		return queryParameter;
	}
	/*
	 * this methods will add white spaces into query
	 */

	public String replaceCharacters(String queryString) {
		queryString = queryString.replace(";", "");
		queryString = queryString.replace("='", " = '");
		queryString = queryString.replace(">'", " > '");
		queryString = queryString.replace("<'", " < '");
		queryString = queryString.replace(">=", " >= ");
		queryString = queryString.replace("<=", " <= ");
		queryString = queryString.replace("!=", " != ");

		return queryString;

	}

	/*
	 * This method is used to extract the baseQuery from the query string.
	 * BaseQuery contains from the beginning of the query till the where clause
	 */
	public String getBaseQuery(String queryString) {

		String[] temp = null;
		temp = queryString.split("where|order\\s+by|group\\s+by");
		String baseQueryString = temp[0].trim();

		return baseQueryString;

	}

	/*
	 * this method will split the query string based on space into an array of
	 * words
	 */
	public String[] getSplitStrings(String queryString) {

		return queryString.split("\\s+");
	}

	/*
	 * extract the name of the file from the query.
	 */

	public String getFileName(String queryString) {

		String fileName = null;
		try {
			String[] fileNameField = queryString.split("from")[1].split("(where)|(order by)|(group by)");
			if (!fileNameField[0].trim().isEmpty()) {
				fileName = fileNameField[0].trim();
			}
		} catch (ArrayIndexOutOfBoundsException e) {

		}
		System.out.println(fileName);
		return fileName;
	}

	/*
	 * extract the order by fields from the query string.
	 */

	public List<String> getOrderByFields(String queryString) {

		List<String> orderBy = null;
		if (queryString.contains(" order by ")) {
			orderBy = new ArrayList<String>();
			String[] orderByFields = queryString.trim().split("\\s+order\\s+by\\s+")[1].trim().split(",");
			for (int i = 0; i < orderByFields.length; i++) {
				orderBy.add(orderByFields[i]);

			}
		}
		return orderBy;
	}

	/*
	 * extract the group by fields from the query string.
	 */
	public List<String> getGroupByFields(String queryString) {

		List<String> groupBy = null;

		// Check if Group by clause is present
		if (queryString.contains(" group by ")) {
			groupBy = new ArrayList<String>();
			String groupByPart = queryString.trim().split("\\s+group\\s+by\\s+")[1].trim();

			// Check if Order by clause is present
			if (groupByPart.contains(" order by ")) {
				groupByPart = groupByPart.split("\\s+order\\s+by\\s+")[0].trim();
			}
			String[] groupByFields = groupByPart.trim().split(",");
			for (int i = 0; i < groupByFields.length; i++) {
				groupBy.add(groupByFields[i]);
			}
		}

		/* Set the groupBy to QueryParamter variable */
		queryParameter.setGroupByFields(groupBy);
		return groupBy;
	}

	/*
	 * extract the selected fields from the query string.
	 */
	public List<String> getFields(String queryString) {

		String fields1[] = null;
		List<String> fields = new ArrayList<String>();

		fields1 = getBaseQuery(queryString).split("select\\s+")[1].split("\\s+from");
		fields1 = fields1[0].trim().split(",");

		for (int i = 0; i < fields1.length; i++) {
			fields.add(fields1[i].trim());
		}

		return fields;

	}

	/*
	 * This method is used to extract the conditions part from the query string.
	 */
	public String getConditionsPartQuery(String queryString) {

		String conditionPart = null;
		String[] temp = null;
		if (queryString.contains("where")) {
			temp = queryString.split("where")[1].trim().split("(order)|(group)\\s+by");
			conditionPart = temp[0].trim();
		}
		return conditionPart;

	}

	/*
	 * extract the conditions from the query string(if exists)
	 */

	public List<Restriction> getConditions(String queryString) {

		String[] conditions = null;
		List<Restriction> restrictList = new ArrayList<Restriction>();
		if (getConditionsPartQuery(queryString) != null) {
			String conditionPart = getConditionsPartQuery(queryString).trim();
			if (conditionPart.toLowerCase().contains(" and ") || conditionPart.toLowerCase().contains(" or ")) {
				conditions = conditionPart.trim().split("( and )|( or )");
			} else {
				conditions = new String[] { conditionPart };
			}
			restrictList = new ArrayList<Restriction>();
			for (int i = 0; i < conditions.length; i++) {
				String[] temp = conditions[i].split("\\s+");
				Restriction restriction = new Restriction();
				restriction.setPropertyName(temp[0].trim());
				restriction.setCondition(temp[1].trim());
				System.out.println(temp[1]);
				restriction.setPropertyValue(temp[2].trim());
				restrictList.add(restriction);
			}
		}
		return restrictList;
	}

	/*
	 * extract the logical operators(AND/OR) from the query, if at all it is
	 * present.
	 */
	public List<String> getLogicalOperators(String queryString) {

		List<String> logicalOperator = null;
		String conditionsPart = getConditionsPartQuery(queryString);
		if ((conditionsPart != null) && conditionsPart.contains(" and ") | conditionsPart.contains(" or ")) {
			logicalOperator = new ArrayList<String>();
			String[] splitCondition = getSplitStrings(conditionsPart.trim());
			for (int i = 0; i < splitCondition.length; i++) {
				if (splitCondition[i].equals("and") | splitCondition[i].equals("or")) {
					logicalOperator.add(splitCondition[i]);
				}
			}

		}

		return logicalOperator;

	}

	/*
	 * extract the aggregate functions from the query.
	 */
	public List<AggregateFunction> getAggregateFunctions(String queryString) {

		List<AggregateFunction> aggregateList = null;
		AggregateFunction aggregate;
		List<String> fieldsString = getFields(queryString);
		aggregateList = new ArrayList<AggregateFunction>();
		if (fieldsString.size() != 1 && (!fieldsString.get(0).equals("*"))) {
			for (int i = 0; i < fieldsString.size(); i++) {
				if (fieldsString.get(i).contains("(")) {
					aggregate = new AggregateFunction();
					aggregate.setFunction(fieldsString.get(i).split("\\(")[0].trim());
					aggregate.setField(fieldsString.get(i).split("\\(")[1].trim().split("\\)")[0]);
					aggregateList.add(aggregate);
				}

			}

		}

		return aggregateList;
	}

}
