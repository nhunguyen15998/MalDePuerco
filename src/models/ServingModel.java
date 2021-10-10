package models;

import java.sql.ResultSet;
import java.util.ArrayList;

import javafx.scene.image.ImageView;
import utils.CompareOperator;
import utils.DataMapping;
import utils.JoinCondition;
import utils.Helpers;

public class ServingModel extends BaseModel {
	private static String table = "servings";
	private static String[] columns = {"id, name, category_id, descriptions, price, created_at, status, quantity, thumbnail"};
	
	private static ServingModel serModel;
	private int id;
	private int sequence;
	private String name;
	private String categoryName;
	private String descriptions;
	private double price;
	private String createAt;
	private int status;	
	private int quantity;
	private String path;
	
	public static final int SERVING_ACTIVATED = 1;
	public static final int SERVING_DEACTIVATED = 0;
	public static DataMapping isActivated = DataMapping.getInstance(SERVING_ACTIVATED, "Activated");
	public static DataMapping isDeactivated = DataMapping.getInstance(SERVING_DEACTIVATED, "Deactivated");
	
	public static boolean isShown = false;
	
	public ServingModel() {
		super(table, columns);
		if(serModel != null) {
			ServingModel serModel = new ServingModel();
		}
	}
	
	public static ServingModel getInstance(int sequence, int id, String name, String categoryName, String descriptions, double price, String createAt, int status, int quantity, String path) {
		if(serModel==null) {
			ServingModel item = new ServingModel();
			item.sequence = sequence;
			item.id = id;
			item.name = name;
			item.categoryName = categoryName;
			item.descriptions = descriptions;
			item.price = price;
			item.createAt = createAt;
			item.status = status;
			item.quantity = quantity;
			item.path = path;
		} return serModel;
	}
	
	
	//get list
	public ResultSet getServingList(ArrayList<CompareOperator> conditions) {
		try {
			String[] selects = {"servings.id", "servings.name as name", "sc.name as cateName",
					"servings.descriptions", "servings.price", "servings.created_at", "servings.status", "servings.quantity"};
			ArrayList<CompareOperator> joinCondition = new ArrayList<CompareOperator>();
			joinCondition.add(CompareOperator.getInstance("servings.category_id", "=", "sc.id"));
//			joinCondition.add(CompareOperator.getInstance("servings.thumbnail", "=", "si.path"));
			
			ArrayList<JoinCondition> joins = new ArrayList<JoinCondition>();
			joins.add(JoinCondition.getInstance("join", "serving_categories sc", joinCondition));
			
//			joins.add(JoinCondition.getInstance("join", "serving_image si", joinCondition));
			
			return this.getData(selects, conditions, joins);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//get by id
	public ResultSet getSerById(int id) {
		try {
			String[] selects = {"servings.id", "servings.name as name", "sc.name as cateName", 
								"servings.descriptions", "servings.price", "servings.created_at",
								"servings.status", "servings.thumbnail"};
			ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
			conditions.add(CompareOperator.getInstance("servings.id", "=", String.valueOf(id)));
			
			ArrayList<CompareOperator> joinConditions = new ArrayList<CompareOperator>();
			joinConditions.add(CompareOperator.getInstance("sc.id", "=", "servings.id"));
			
			ArrayList<JoinCondition> joins = new ArrayList<JoinCondition>();
			joins.add(JoinCondition.getInstance("join", "serving_categories sc", joinConditions));
			
			return this.getData(selects, joinConditions, joins);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//create
	public int createServing(ArrayList<DataMapping> data) {
		try {
			return this.create(data);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	//update
	public boolean updateServing(int id, ArrayList<DataMapping> data) {
		try {
			ArrayList<CompareOperator> condition = new ArrayList<CompareOperator>();
			condition.add(CompareOperator.getInstance("servings.id", "=", String.valueOf(id)));
			return this.update(data, condition);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
		
	//delete
	public boolean deleteServing(int id) {
		try {
			ArrayList<CompareOperator> condition = new ArrayList<CompareOperator>();
			condition.add(CompareOperator.getInstance("servings.id", "=", String.valueOf(id)));
			return this.delete(condition);
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
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(String descriptions) {
		this.descriptions = descriptions;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
}
