package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import utils.CompareOperator;
import utils.DataMapping;
import utils.Helpers;
import utils.JoinCondition;

public class MySQLJDBC {
	//db, username, password
	private static final String DATABASE_URL = "jdbc:mysql://ec2-3-15-8-122.us-east-2.compute.amazonaws.com:3306/mal_de_puerco";
	private static final String USER_NAME = "restaurant";
	private static final String PASSWORD = "password";
	public static Connection connection = null;
	public static MySQLJDBC db = null;
	
	public String table;
	public String[] columns;
	
	public MySQLJDBC() {
		if(connection == null) {
			connection = MySQLJDBC.connect();
		}
	}
	
	public static Connection connect() {
		try {
			//if connection exists, do not instantiate
			connection = DriverManager.getConnection(DATABASE_URL, USER_NAME, PASSWORD);
			if(connection != null){
				System.out.println("Database connected");
				return connection;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Fail to connect");
			return null;
		}
	}
	
	//get columns
	public String[] getColumns(ArrayList<DataMapping> data) {
		String[] columns = new String[data.size()];
		for(int i = 0; i < data.size(); i++) {
			columns[i] = data.get(i).key;
		}
		return columns;
	}
	
	//get values
	public String[] getValues(ArrayList<DataMapping> data) {
		String[] values = new String[data.size()];
		for(int i = 0; i < data.size(); i++) {
			values[i] = data.get(i).value;
		}
		return values;
	}
	
	//1366 x
	//get data
	public ResultSet getData(String[] selects, ArrayList<CompareOperator> conditions, ArrayList<JoinCondition> joins, String[] groupBys, String orderBys, String limit) {
		try {
			String conditionsStr = "";
    		String joinStr = "";
			//if selects = 0 => * 
			if(selects.length == 0) {
				selects = columns;
			}
			//conditions
    		if(conditions != null && conditions.size() > 0) {
    			int i = 0;
    			for(CompareOperator item : conditions) {
    				conditionsStr += " " + item.key+" "+ item.operator +" "+ "'" + item.value + "' ";
    				i++;
    				if(i < conditions.size()) {
    					conditionsStr += " and ";
    				}
    				
    			}
    		}
    		//joins
    		if(joins != null && joins.size() > 0) {
    			int i = 0;
    			for(JoinCondition item : joins) {
    				String subConditionStr = "";
    				int j = 0;
        			for(CompareOperator cp : item.conditions) {
        				subConditionStr += " "+cp.key + cp.operator + cp.value;
        				j++;
        				if(j < item.conditions.size()) {
        					subConditionStr+=" and ";
        				}
        			}
    				joinStr += " " + item.type + " " + item.table + " on " + subConditionStr + " ";	
    			}
    		}
    		
    		String query = "select " + String.join(",", selects)  + " from " + this.table +" "+joinStr+ (conditionsStr.length() > 0 ? " where "+conditionsStr : "");
    		
    		//groupby
    		if(groupBys != null) {
    			query += " group by " +  String.join(", ", groupBys);
    		}
    		
    		//orderby 
    		if(orderBys != null) {
    			query += " order by " + orderBys;
    		}
    		    		
    		//limit
    		if(limit != null) {
    			query += " limit " + limit;
    		}
    		
    		System.out.println(query);
			Statement stmt = MySQLJDBC.connection.createStatement();
			return stmt.executeQuery(query);
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	 }
	
	//create
	public int create(ArrayList<DataMapping> data) {
    	try {
    		String columns = "("+ String.join(",", this.getColumns(data)) +")";
    		String valuesStr = "";
    		String[] dataValues = this.getValues(data);

    		for(int i = 0; i<dataValues.length; i++) {
    			String item = dataValues[i];
    			if(item != null) {
    				if(item.matches("^[0-9]*$")) {
    					if(item.startsWith("0") && item.length() > 2) {
    						item = "'"+ item +"'";
    					}
    				} else {
    					item = "'"+ item +"'";
    				}
    			}
    			valuesStr += item;
				
				if(i<dataValues.length-1) {
					valuesStr+=",";
				}
    		}
    		String values = " values("+valuesStr+")";
    		String query = "insert into " + this.table + columns + values;
    		PreparedStatement stmt = MySQLJDBC.connection.prepareStatement(query,
    		        Statement.RETURN_GENERATED_KEYS);

    		System.out.println(query);
    		stmt.execute();
    		
    		//return id
    		ResultSet rs = stmt.getGeneratedKeys();
    		int generatedKey = 0;
    		if (rs.next()) {
    		    generatedKey = rs.getInt(1);
    		}
    		 
    		System.out.println("Inserted record's ID: " + generatedKey);

    		return generatedKey;
		} catch (Exception eCreate) {
			eCreate.printStackTrace();
			return 0;
		}
    }
	
	//update
	public boolean update(ArrayList<DataMapping> data, ArrayList<CompareOperator> conditions) {
    	try {
    		String conditionStr = "";
    		String dataUpdated = "";
    		for(int i=0; i< data.size(); i++) {
    			DataMapping item = data.get(i);
    			
    			if(item.value != null) {
    				if(item.value.matches("^[0-9]*$")) {
    					if(item.value.startsWith("0") && item.value.length() > 2) {
    						item.value = "'"+ item.value +"'";
    					}
    				} else {
    					item.value = "'"+ item.value +"'";
    				}
    			}
    			
    			dataUpdated += " " + item.key + " = " + item.value;
    			if(i <data.size()-1) {
    				dataUpdated += ", ";
    			}
    		}
    		
    		for(int i=0; i< conditions.size(); i++) {
    			CompareOperator item = conditions.get(i);
    			conditionStr += " " + item.key + item.operator + (!Helpers.isNumeric(item.value) ? "'" + item.value + "'" : item.value);
    			if(i < conditions.size()-1) {
    				conditionStr += " and ";
    			}
    		}
    		
    		String query = "update " + this.table + " set " + dataUpdated + " where" + conditionStr ;
    		System.out.println(query);
    		Statement stmt = MySQLJDBC.connection.createStatement();
    		stmt.execute(query);
			return true;
		} catch (Exception eShow) {
			eShow.printStackTrace();
			return false;
		}
    }
	
	//delete
	public boolean delete(ArrayList<CompareOperator> conditions) {
    	try {
    		String conditionStr = "";
    		if(conditions.size() > 0) {
    			int i = 1;
    			for(CompareOperator item : conditions) {
    				conditionStr += " " + item.key + " " + item.operator + " " + "'" + item.value + "'";
    				if(i < conditions.size()) {
    					conditionStr += " and ";
    				}
    				i++;
    			}
    		}else {
    			return false;
    		}
    		String query = "delete from " + this.table + " where " + conditionStr;
//    		System.out.println(query);
    		Statement stmt = MySQLJDBC.connection.createStatement();
    		stmt.execute(query);
			return true;
		} catch (Exception eShow) {
			eShow.printStackTrace();
			return false;
		}
    }

}
