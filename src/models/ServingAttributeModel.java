package models;

import java.sql.ResultSet;
import java.util.ArrayList;

import utils.CompareOperator;
import utils.DataMapping;
import utils.JoinCondition;

public class ServingAttributeModel extends BaseModel {

	private static String table = "serving_attributes";
	private static String[] columns = {"id", "serving_id", "attribute", "price", "created_at"};
	private static ServingAttributeModel SerAttModel;
	
	private int id;
	private int sequence;
	private String servingName;
	private String attribute;
	private double price;
	private String createAt;
	
	//status
	public static final int SERVING_ATTRIBUTE_ACTIVATED = 1;
	public static final int SERVING_ATTRIBUTE_DEACTIVATED = 0;
	public static DataMapping isActivated = DataMapping.getInstance(SERVING_ATTRIBUTE_ACTIVATED, "Activated");
	public static DataMapping isDeactivated = DataMapping.getInstance(SERVING_ATTRIBUTE_DEACTIVATED, "Deactivated");

	public static boolean isShown = false;

	//constructor - overloading
	public ServingAttributeModel() {
		super(table, columns);
		if(SerAttModel != null) {
			ServingAttributeModel item = new ServingAttributeModel();
		}
	}

	public static ServingAttributeModel getInstance(int sequence, int id, String servingName, String attribute, double price, String createAt) {
		if (SerAttModel == null) {
			ServingAttributeModel item = new ServingAttributeModel();
			item.sequence = sequence;
			item.id = id;
			item.servingName = servingName;
			item.attribute = attribute;
			item.price = price;
			item.createAt = createAt;
			return item;
		} 
		return SerAttModel;
	}
	
	//get data
	public ResultSet getSerAttList(ArrayList<CompareOperator> conditions) {
		try {
			String[] selects = {"serving_attributes.id", "servings.name as servingID",
								"serving_attributes.attribute", "serving_attributes.price", "serving_attributes.created_at"};
			ArrayList<CompareOperator> joinServings = new ArrayList<CompareOperator>();
			joinServings.add(CompareOperator.getInstance("serving_attributes.serving_id", "=", "servings.id"));
			
			ArrayList<JoinCondition> joins = new ArrayList<JoinCondition>();
			joins.add(JoinCondition.getInstance("join", "servings", joinServings));
			
			return this.getData(selects, conditions, joins);
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
			
			return this.getData(selects, conditions, joins);
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
	
	
}
