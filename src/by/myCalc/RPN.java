//Deprecated (without inheritance)
package by.myCalc;

import java.text.ParseException;
import java.util.Collections;
import java.util.Stack;
import java.util.StringTokenizer;
import java.math.*;

public class RPN {
	private final String[] FUNCTIONS = { "cos", "sin", "sqrt" };
	private final String OPERATORS = "+-*/^";

	private Stack<String> stackOperations = new Stack<String>();
	private Stack<String> stackRPN = new Stack<String>();

	private boolean isNumber(String token) {
		try {
			Double.parseDouble(token);
		} catch (Exception ex) {
			return false;
		};

		return true;
	}

	private boolean isFunction(String token) {
		for (String item : FUNCTIONS) {
			if (item.equals(token)) {
				return true;
			}
		}
		return false;
	}

	private boolean isOpenBracket(String token) {
		return token.equals("(");
	}

	private boolean isCloseBracket(String token) {
		return token.equals(")");
	}

	private boolean isOperator(String token) {
		return OPERATORS.contains(token);
	}

	private byte getPrecedence(String token) {
		if (token.equals("+") || token.equals("-")) {
			return 1;
		} else if (token.equals("*") || token.equals("/")) {
			return 2;
		} else {
			return 3;
		}
	}

	public Stack parse(String expression) throws ParseException {
		stackOperations.clear();
		stackRPN.clear();

		// prepare input string: remove spaces, handle negative numbers
		expression = expression.replace(" ", "").replace("(-", "(0-");

		if (expression.charAt(0) == '-') {
			expression = "0" + expression;
		}

		StringTokenizer stringTokenizer = new StringTokenizer(expression, OPERATORS + "()", true);

		// shunting-yard algorithm
		while (stringTokenizer.hasMoreTokens()) {
			String token = stringTokenizer.nextToken();
			if (isOpenBracket(token)) {
				stackOperations.push(token);
			} else if (isCloseBracket(token)) {
				while (!stackOperations.empty() && !isOpenBracket(stackOperations.lastElement())) {
					stackRPN.push(stackOperations.pop());
				}
				stackOperations.pop();
				if (!stackOperations.empty() && isFunction(stackOperations.lastElement())) {
					stackRPN.push(stackOperations.pop());
				}
			} else if (isNumber(token)) {
				stackRPN.push(token);
			} else if (isOperator(token)) {
				while (!stackOperations.empty() && isOperator(stackOperations.lastElement())
						&& getPrecedence(token) <= getPrecedence(stackOperations.lastElement())) {
					stackRPN.push(stackOperations.pop());
				}
				stackOperations.push(token);
			} else if (isFunction(token)) {
				stackOperations.push(token);
			} else {
				String sException = "Unrecognized token: " + token;
				throw new ParseException(sException, 0);
			}

		}
		while (!stackOperations.empty()) {
			stackRPN.push(stackOperations.pop());
		}

		// reverse stack
		Collections.reverse(stackRPN);
		return (stackRPN);
	}

	public String calculateRPN(Stack stackRPN, String expression) {
		Stack<String> stackResult = new Stack<String>();
		// double top = 0;
		stackResult.clear();

		// loop calculating RPN
		while (!stackRPN.isEmpty()) {
			String sBuf = stackRPN.pop().toString();
			if (isNumber(sBuf)) {
				stackResult.push(sBuf);
			} else if (isOperator(sBuf)) {
				double num2 = Double.parseDouble(stackResult.pop());
				double num1 = Double.parseDouble(stackResult.pop());
				
				switch (sBuf) {
				case "+":
					stackResult.push(Double.toString(num1 + num2));
					break;
				case "-":
					stackResult.push(Double.toString(num1 - num2));
					break;
				case "*":
					stackResult.push(Double.toString(num1 * num2));
					break;
				case "/":
					try {
						Double divisionResult = num1 / num2;
						if (divisionResult == Double.POSITIVE_INFINITY || divisionResult == Double.NEGATIVE_INFINITY) {
							throw new ArithmeticException();
						}
					} catch (ArithmeticException e) {
						return "Division by zero";
					}
					stackResult.push(Double.toString(num1 / num2));
					break;
				case "^":
					stackResult.push(Double.toString(Math.pow(num1, num2)));
					break;
				}
			} else if (isFunction(sBuf)) {
				double num1 = Double.parseDouble(stackResult.pop());
				switch (sBuf) {
				case "sin":
					stackResult.push(Double.toString(Math.sin(Math.toRadians(num1))));
					break;
				case "cos":
					stackResult.push(Double.toString(Math.cos(Math.toRadians(num1))));
					break;
				case "sqrt":
					stackResult.push(Double.toString(Math.sqrt(num1)));
					break;
				}

			}

		}

		if (!stackResult.isEmpty()) {
			String s1 = stackResult.peek();
			String[] output = s1.split("\\.");

			BigDecimal x = new BigDecimal(Double.parseDouble(stackResult.pop()));
			x = x.setScale(5, BigDecimal.ROUND_HALF_UP);
			if (Double.parseDouble(output[1]) != 0) {
				return expression + "=" + x.toString();
			} else {
				return expression + "=" + output[0];
			}
		} else
			return null;
	}

}
