package models;

import java.sql.ResultSet;
import java.util.ArrayList;

import utils.CompareOperator;
import utils.DataMapping;
import utils.JoinCondition;

public class ServingAttributeModel extends BaseModel {

	private static String table = "serving_attributes";
	private static String[] columns = {"id", "serving_id", "attribute_id", "quantity", "price", "created_at"};
	private static ServingAttributeModel SerAttModel;
	
	private int id;
	private int sequence;
	private String servingName;
	private String attribute;
	private double price;
	private String createAt;
	private int quantity;
	
	
	public static boolean isShown = false;

	//constructor - overloading
	public ServingAttributeModel() {
		super(table, columns);
		if(SerAttModel != null) {
			ServingAttributeModel item = new ServingAttributeModel();
		}
	}

	public ServingAttributeModel (int sequence, int id, String servingName, String attribute, int quantity, double price, String createAt) {
		super(table, columns);
		this.sequence = sequence;
		this.id = id;
		this.servingName = servingName;
		this.attribute = attribute;
		this.quantity = quantity;
		this.price = price;
		this.createAt = createAt;
		
	}
	
	//get data
	public ResultSet getSerAttList(ArrayList<CompareOperator> conditions) {
		try {
			String[] selects = {"serving_attributes.id", "servings.name as servingID",
								"attributes.name as attName", "serving_attributes.quantity",
								"serving_attributes.price", "serving_attributes.created_at"};
			ArrayList<CompareOperator> joinServings = new ArrayList<CompareOperator>();
			joinServings.add(CompareOperator.getInstance("serving_attributes.serving_id", "=", "servings.id"));
			ArrayList<CompareOperator> joinAttribute = new ArrayList<CompareOperator>();
			joinAttribute.add(CompareOperator.getInstance("serving_attributes.attribute_id", "=", "attributes.id"));
			
			ArrayList<JoinCondition> joins = new ArrayList<JoinCondition>();
			joins.add(JoinCondition.getInstance("join", "servings", joinServings));
			joins.add(JoinCondition.getInstance("join", "attributes", joinAttribute));
			
			return this.getData(selects, conditions, joins, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//get by id
	public ResultSet getSerAttById(int id) {
		try {
			String[] selects = {"serving_attributes.*", "servings.id as serName"};
			ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
			conditions.add(CompareOperator.getInstance("serving_attributes.id", "=", String.valueOf(id)));
			
			ArrayList<CompareOperator> joinConditions = new ArrayList<CompareOperator>();
			joinConditions.add(CompareOperator.getInstance("serving_attributes.serving_id", "=", "servings.id"));
			
			ArrayList<JoinCondition> joins = new ArrayList<JoinCondition>();
			joins.add(JoinCondition.getInstance("join", "servings", joinConditions));
			
			return this.getData(selects, conditions, joins, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//create
	public int createSerAtt(ArrayList<DataMapping> data) {
		try {
			return this.create(data);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	//update
	public boolean updateSerAtt(int id, ArrayList<DataMapping> data) {
		try {
			ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
			conditions.add(CompareOperator.getInstance("id", "=", String.valueOf(id)));
			
			return this.update(data, conditions);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//delete
	public boolean deleteSerAtt(int id) {
		try {
			ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
			conditions.add(CompareOperator.getInstance("id", "=", String.valueOf(id)));
			
			return this.delete(conditions);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public String getServingName() {
		return servingName;
	}

	public void setServingName(String servingName) {
		this.servingName = servingName;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getCreateAt() {
		return createAt;
	}

	public void setCreateAt(String createAt) {
		this.createAt = createAt;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	
}
