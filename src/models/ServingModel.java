package models;

import java.sql.ResultSet;
import java.util.ArrayList;

import utils.CompareOperator;
import utils.DataMapping;
import utils.JoinCondition;

public class ServingModel extends BaseModel {
	private static String table = "servings";
	private static String[] columns = {"id, name, thumbnail, category_id, descriptions, quantity, price, is_new, is_best_seller, created_at, status"};
	
	private int id;
	private int sequence;
	private String name;
	private String thumbnail;
	private String categoryName;
	private String descriptions;
	private int quantity;
	private double price;
	private int isNew;
	private int isBestSeller;
	private String createdAt;
	private String status;	
	
	private static final int SERVING_ACTIVATED = 1;
	private static final int SERVING_DEACTIVATED = 0;
	public static DataMapping isActivated = DataMapping.getInstance(SERVING_ACTIVATED, "Activated");
	public static DataMapping isDeactivated = DataMapping.getInstance(SERVING_DEACTIVATED, "Deactivated");
	
	//type
	private static final int FOOD = 0;
	private static final int DRINK = 1;
	public static DataMapping isFood = DataMapping.getInstance(FOOD, "Food");
	public static DataMapping isDrink = DataMapping.getInstance(DRINK, "Drink");
	
	public ServingModel() {
		super(table, columns);
	}
	
	public ServingModel(int id, int sequence, String name, String thumbnail, String categoryName, String descriptions, 
						int quantity, double price, int isNew, int isBestSeller, String createdAt, String status) {
		super(table, columns);
		this.setId(id);
		this.setSequence(sequence);
		this.setName(name);
		this.setThumbnail(thumbnail);
		this.setCategoryName(categoryName);
		this.setDescriptions(descriptions);
		this.setQuantity(quantity);
		this.setPrice(price);
		this.setIsNew(isNew);
		this.setIsBestSeller(isBestSeller);
		this.setCreatedAt(createdAt);
		this.setStatus(status);
	}
	
	//get list
	public ResultSet getServingList(ArrayList<CompareOperator> conditions) {
		try {
			String[] selects = {"servings.*, sc.name as category_name, scs.name as category_parent_name, "
					+ "serving_attributes.*, (servings.price + serving_attributes.price) as price_of_item_with_attribute"};
			ArrayList<CompareOperator> cateCondition = new ArrayList<CompareOperator>();
			cateCondition.add(CompareOperator.getInstance("servings.category_id", "=", "sc.id"));
			ArrayList<CompareOperator> cateParentCondition = new ArrayList<CompareOperator>();
			cateParentCondition.add(CompareOperator.getInstance("scs.id", "=", "sc.parent_id"));
			ArrayList<CompareOperator> attributeCondition = new ArrayList<CompareOperator>();
			attributeCondition.add(CompareOperator.getInstance("servings.id", "=", "serving_attributes.serving_id"));
			ArrayList<JoinCondition> joins = new ArrayList<JoinCondition>();
			joins.add(JoinCondition.getInstance("left join", "serving_categories sc", cateCondition));
			joins.add(JoinCondition.getInstance("left join", "serving_categories scs", cateParentCondition));
			joins.add(JoinCondition.getInstance("left join", "serving_attributes", attributeCondition));
			return this.getData(selects, conditions, joins, null, null);
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

	public int getIsNew() {
		return isNew;
	}

	public void setIsNew(int isNew) {
		this.isNew = isNew;
	}

	public int getIsBestSeller() {
		return isBestSeller;
	}

	public void setIsBestSeller(int isBestSeller) {
		this.isBestSeller = isBestSeller;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
