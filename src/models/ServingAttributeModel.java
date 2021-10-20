package models;

import java.sql.ResultSet;
import java.util.ArrayList;

import utils.CompareOperator;
import utils.DataMapping;
import utils.JoinCondition;

public class ServingAttributeModel extends BaseModel {
	private static String table = "serving_attributes";
	private static String[] columns = {"id, serving_id, attribute_id, quantity, price, created_at"};
	
	private int id;
	private int servingId;
	private int attributeId;
	private int quantity;
	private double price;
	private String createdAt;

	public ServingAttributeModel() {
		super(table, columns);
	}
	
	public ServingAttributeModel(int id, int servingId, int attributeId, int quantity, double price, String createdAt) {
		super(table, columns);
		this.setId(id);
		this.setServingId(servingId);
		this.setAttributeId(attributeId);
		this.setQuantity(quantity);
		this.setPrice(price);
		this.setCreatedAt(createdAt);
	}
	
	//get data
	public ResultSet getServingAttributeList(ArrayList<CompareOperator> conditions) {
		try {
			String[] selects = {"serving_attributes.*, servings.name as serving_name, attributes.name as attribute_name,"
					+ "(servings.price + serving_attributes.price) as price_of_item_with_attribute"};
			
			ArrayList<CompareOperator> servingJoin = new ArrayList<CompareOperator>();
			servingJoin.add(CompareOperator.getInstance("serving_attributes.serving_id", "=", "servings.id"));
			ArrayList<CompareOperator> attributeJoin = new ArrayList<CompareOperator>();
			attributeJoin.add(CompareOperator.getInstance("serving_attributes.attribute_id", "=", "attributes.id"));
			
			ArrayList<JoinCondition> joins = new ArrayList<JoinCondition>();
			joins.add(JoinCondition.getInstance("left join", "servings", servingJoin));
			joins.add(JoinCondition.getInstance("inner join", "attributes", attributeJoin));
			return this.getData(selects, conditions, joins, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//get stock
	public ResultSet getStock(ArrayList<CompareOperator> conditions) {
		try {
			String[] selects = {"servings.id, servings.name, sum(serving_attributes.quantity) as qty"};
			
			ArrayList<CompareOperator> servingJoin = new ArrayList<CompareOperator>();
			servingJoin.add(CompareOperator.getInstance("serving_attributes.serving_id", "=", "servings.id"));
			
			ArrayList<JoinCondition> joins = new ArrayList<JoinCondition>();
			joins.add(JoinCondition.getInstance("left join", "servings", servingJoin));
			return this.getData(selects, conditions, joins, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	//get & set
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getServingId() {
		return servingId;
	}

	public void setServingId(int servingId) {
		this.servingId = servingId;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public int getAttributeId() {
		return attributeId;
	}

	public void setAttributeId(int attributeId) {
		this.attributeId = attributeId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	
	
	
}
