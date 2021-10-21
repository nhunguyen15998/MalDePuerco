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
	private static String[] columns = {"id, name, category_id, type, descriptions, quantity, price, created_at, status, thumbnail"};
	
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
	private int type;
	
	//status
	public static final int SERVING_ACTIVATED = 1;
	public static final int SERVING_DEACTIVATED = 0;
	public static DataMapping isActivated = DataMapping.getInstance(SERVING_ACTIVATED, "Activated");
	public static DataMapping isDeactivated = DataMapping.getInstance(SERVING_DEACTIVATED, "Deactivated");
	
	//type
	public static final int FOOD = 0;
	public static final int DRINK = 1;

	public static DataMapping isFood = DataMapping.getInstance(FOOD, "Food");
	public static DataMapping isDrink = DataMapping.getInstance(DRINK, "Drink");
	
	public static boolean isShown = false;
	
	public ServingModel() {
		super(table, columns);
		if(serModel != null) {
			ServingModel serModel = new ServingModel();
		}
	}
	public ServingModel (int sequence, int id, String name, String categoryName, 
			int type, String descriptions,  int quantity, double price, String createAt, int status, String path) {
		super(table, columns);
		this.sequence = sequence;
		this.id = id;
		this.name = name;
		this.categoryName = categoryName;
		this.type = type;
		this.descriptions = descriptions;
		this.quantity = quantity;
		this.price = price;
		this.createAt = createAt;
		this.status = status;
		this.path = path;
	}
	
	
	//get list
	public ResultSet getServingList(ArrayList<CompareOperator> conditions) {
		try {
			String[] selects = {"servings.*, sc.name as category_name, scs.name as category_parent_name, "
					+ "(servings.quantity - servings.quantity_sold) as stock_quantity, "
					+ "servings.name as nameSer", "sc.name as cateName"};
			ArrayList<CompareOperator> cateCondition = new ArrayList<CompareOperator>();
			cateCondition.add(CompareOperator.getInstance("servings.category_id", "=", "sc.id"));
			ArrayList<CompareOperator> cateParentCondition = new ArrayList<CompareOperator>();
			cateParentCondition.add(CompareOperator.getInstance("scs.id", "=", "sc.parent_id"));
			ArrayList<CompareOperator> attributeCondition = new ArrayList<CompareOperator>();
			attributeCondition.add(CompareOperator.getInstance("servings.id", "=", "serving_attributes.serving_id"));
			ArrayList<JoinCondition> joins = new ArrayList<JoinCondition>();
			joins.add(JoinCondition.getInstance("left join", "serving_categories sc", cateCondition));
			joins.add(JoinCondition.getInstance("left join", "serving_categories scs", cateParentCondition));

			return this.getData(selects, conditions, joins, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//get by id
	public ResultSet getSerById(int id) {
		try {
			String[] selects = {"servings.id", "servings.name as nameSer", "sc.name as cateName",
					"servings.type", "servings.descriptions","servings.quantity", 
					"servings.price", "servings.created_at", "servings.status"};
			ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
			conditions.add(CompareOperator.getInstance("servings.id", "=", String.valueOf(id)));
			
			ArrayList<CompareOperator> joinConditions = new ArrayList<CompareOperator>();
			joinConditions.add(CompareOperator.getInstance("sc.id", "=", "servings.id"));
			
			ArrayList<JoinCondition> joins = new ArrayList<JoinCondition>();
			joins.add(JoinCondition.getInstance("join", "serving_categories sc", joinConditions));
			
			return this.getData(selects, joinConditions, joins, null, null, null);
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
	
	public void setType(int type) {
		this.type = type;
	}
	
	public int getType() {
		return type;
	}
}
