package utils;

public class CompareOperator {
	 public String key;
	 public String operator;
	 public String value;
	 private static CompareOperator compareOperator;
	
	public static CompareOperator getInstance(String key, String operator, String value) {
		if(compareOperator == null) {
			compareOperator = new CompareOperator();
			compareOperator.key = key;
			compareOperator.operator = operator;
			compareOperator.value = value;
			return compareOperator;
		}
		return compareOperator;
	}
	 
}
