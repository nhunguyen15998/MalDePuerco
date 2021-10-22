package utils;

public class CompareOperator {
	public String key;
	 public String operator;
	 public String value;
	 private static CompareOperator compareOperator;
	
	public static CompareOperator getInstance(String key, String operator, String value) {
		if(compareOperator == null) {
			CompareOperator item = new CompareOperator();
			item.key = key;
			item.operator = operator;
			item.value = value;
			return item;
		}

		return compareOperator;
	}

}
