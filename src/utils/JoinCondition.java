package utils;

import java.util.ArrayList;

public class JoinCondition {
	public String type;
	public String table;
	public ArrayList<CompareOperator> conditions;
	private static JoinCondition joinCondition;
	
	public static JoinCondition getInstance(String type, String table, ArrayList<CompareOperator> conditions) {
		if(joinCondition == null) {
			JoinCondition item = new JoinCondition();
			item.type = type;
			item.table = table;
			item.conditions = conditions;
			return item;
		}
		return joinCondition;
	}
}
