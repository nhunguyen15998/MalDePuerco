package utils;

import java.util.ArrayList;

public class JoinCondition {
	public String type;
	public String table;
	public ArrayList<CompareOperator> conditions;
	private static JoinCondition joinCondition;
	
	public static JoinCondition getInstance(String type, String table, ArrayList<CompareOperator> conditions) {
		if(joinCondition == null) {
			joinCondition = new JoinCondition();
			joinCondition.type = type;
			joinCondition.table = table;
			joinCondition.conditions = conditions;
			return joinCondition;
		}
		return joinCondition;
	}
}
