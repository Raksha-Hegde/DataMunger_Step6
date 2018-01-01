package com.stackroute.datamunger.query;

import java.util.List;

//this class contains methods to evaluate expressions
public class Filter {

	private Boolean flag;
	private Boolean conditionFlag;

	/*
	 * the evaluateExpression() method of this class is responsible for
	 * evaluating the expressions mentioned in the query.
	 */

	public Boolean evaluateExpression(String condition, String propertyValue, String columnValue,
			String columnDataType) {
		switch (condition) {
		case "=": {
			flag = equalTo(propertyValue, columnValue, columnDataType);
			break;
		}
		case "!=": {
			flag = notEqualTo(propertyValue, columnValue, columnDataType);
			break;
		}
		case "<=": {
			flag = lessThanOrEqualTo(propertyValue, columnValue, columnDataType);
			break;
		}
		case "<": {
			flag = lessThan(propertyValue, columnValue, columnDataType);
			break;
		}
		case ">=": {
			flag = greaterThanOrEqualTo(propertyValue, columnValue, columnDataType);
			break;
		}
		case ">": {
			flag = greaterThan(propertyValue, columnValue, columnDataType);
			break;
		}

		}
		return flag;
	}

	// method containing implementation of greaterThanOrEqualTo operator
	private Boolean greaterThanOrEqualTo(String propertyValue, String columnValue, String columnDataType) {
		if (columnDataType.equalsIgnoreCase("java.lang.String")) {
			conditionFlag = propertyValue.equals(columnValue);
		} else if (columnDataType.equalsIgnoreCase("java.lang.Integer")) {
			conditionFlag = (Integer.parseInt(columnValue) >= Integer.parseInt(propertyValue));
		} else if (columnDataType.equalsIgnoreCase("java.lang.Float")) {
			conditionFlag = (Float.parseFloat(columnValue) >= Float.parseFloat(propertyValue));
		} else if (columnDataType.equalsIgnoreCase("java.util.Date")) {

		}

		return conditionFlag;
	}

	// method containing implementation of equalTo operator
	private Boolean equalTo(String propertyValue, String columnValue, String columnDataType) {

		if (columnDataType.equalsIgnoreCase("java.lang.String")) {
			{
				conditionFlag = propertyValue.equals(columnValue);
			}
		} else if (columnDataType.equalsIgnoreCase("java.lang.Integer")) {
			conditionFlag = (Integer.parseInt(propertyValue) == Integer.parseInt(columnValue));
		} else if (columnDataType.equalsIgnoreCase("java.lang.Float")) {
			conditionFlag = (Float.parseFloat(propertyValue) == Float.parseFloat(columnValue));
		} else if (columnDataType.equalsIgnoreCase("java.util.Date")) {

		}
		return conditionFlag;

	}

	// method containing implementation of notEqualTo operator
	private Boolean notEqualTo(String propertyValue, String columnValue, String columnDataType) {
		if (columnDataType.equalsIgnoreCase("java.lang.String")) {
			conditionFlag = !propertyValue.equals(columnValue);
		} else if (columnDataType.equalsIgnoreCase("java.lang.Integer")) {
			conditionFlag = (Integer.parseInt(propertyValue) != Integer.parseInt(columnValue));
		} else if (columnDataType.equalsIgnoreCase("java.lang.Float")) {
			conditionFlag = (Float.parseFloat(propertyValue) != Float.parseFloat(columnValue));
		} else if (columnDataType.equalsIgnoreCase("java.util.Date")) {

		}
		return conditionFlag;

	}

	// method containing implementation of greaterThan operator
	private Boolean greaterThan(String propertyValue, String columnValue, String columnDataType) {
		if (columnDataType.equalsIgnoreCase("java.lang.String")) {
			conditionFlag = propertyValue.equals(columnValue);
		} else if (columnDataType.equalsIgnoreCase("java.lang.Integer")) {
			conditionFlag = (Integer.parseInt(columnValue) > Integer.parseInt(propertyValue));
		} else if (columnDataType.equalsIgnoreCase("java.lang.Float")) {
			conditionFlag = (Float.parseFloat(columnValue) > Float.parseFloat(propertyValue));
		} else if (columnDataType.equalsIgnoreCase("java.util.Date")) {

		}
		return conditionFlag;

	}

	// method containing implementation of lessThan operator
	private Boolean lessThan(String propertyValue, String columnValue, String columnDataType) {
		if (columnDataType.equalsIgnoreCase("java.lang.String")) {
			conditionFlag = propertyValue.equals(columnValue);
		} else if (columnDataType.equalsIgnoreCase("java.lang.Integer")) {
			conditionFlag = (Integer.parseInt(columnValue) < Integer.parseInt(propertyValue));
		} else if (columnDataType.equalsIgnoreCase("java.lang.Float")) {
			conditionFlag = (Float.parseFloat(columnValue) < Float.parseFloat(propertyValue));
		} else if (columnDataType.equalsIgnoreCase("java.util.Date")) {

		}
		return conditionFlag;

	}

	// method containing implementation of lessThanOrEqualTo operator
	private Boolean lessThanOrEqualTo(String propertyValue, String columnValue, String columnDataType) {
		if (columnDataType.equalsIgnoreCase("java.lang.String")) {
			conditionFlag = propertyValue.equals(columnValue);
		} else if (columnDataType.equalsIgnoreCase("java.lang.Integer")) {
			conditionFlag = (Integer.parseInt(columnValue) <= Integer.parseInt(propertyValue));
		} else if (columnDataType.equalsIgnoreCase("java.lang.Float")) {
			conditionFlag = (Float.parseFloat(columnValue) <= Float.parseFloat(propertyValue));
		} else if (columnDataType.equalsIgnoreCase("java.util.Date")) {

		}

		return conditionFlag;

	}

	// method to evaluate conditions when logical operator is present
	public Boolean evaluateConditions(List<Boolean> flagStatus, List<String> logicalOperators) {
		Boolean status = flagStatus.get(0);
		int size = logicalOperators.size();
		if (size == 1) {
			for (int i = 0; i < logicalOperators.size(); i++) {
				if (logicalOperators.get(i).equalsIgnoreCase("and")) {
					status = (status && flagStatus.get(i + 1));
				} else if (logicalOperators.get(i).equalsIgnoreCase("and")) {
					status = (status || flagStatus.get(i + 1));
				}
			}
		} else if (size == 2) {
			String operator1 = logicalOperators.get(0);
			String operator2 = logicalOperators.get(1);

			if (operator1.equals("and") && operator2.equals("or")) {
				if (flagStatus.get(0) && flagStatus.get(1) || flagStatus.get(2))
					flag = true;
			}
			if (operator1.equals("or") && operator2.equals("and")) {
				if (flagStatus.get(0) || flagStatus.get(1) && flagStatus.get(2))
					flag = true;
			}

		}
		return status;
	}
}
