package models;


import java.sql.ResultSet;
import java.util.ArrayList;

import db.MySQLJDBC;
import utils.CompareOperator;
import utils.DataMapping;
import utils.JoinCondition;

public class BaseModel {
	public MySQLJDBC db = new MySQLJDBC();
	public BaseModel(String table, String[] columns) {
		this.db.table = table;
		this.db.columns = columns;
	}
	
	//create
	public int create(ArrayList<DataMapping> data){
		return db.create(data);
	}
	
	//get data
	public ResultSet getData(String[] selects, ArrayList<CompareOperator> conditions, ArrayList<JoinCondition> joins) {
		return db.getData(selects, conditions, joins);
	}
	
	//edit
	public boolean update(ArrayList<DataMapping> data, ArrayList<CompareOperator> conditions) {
		return db.update(data, conditions);
	}
	
	//delete
	public boolean delete(ArrayList<CompareOperator> conditions) {
		return db.delete(conditions);
	}
	
}
