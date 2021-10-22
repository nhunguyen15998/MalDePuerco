package models;

import java.sql.ResultSet;
import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utils.CompareOperator;
import utils.DataMapping;
import utils.JoinCondition;

public class ServingCategoryModel extends BaseModel {
	private static String table = "serving_categories";
	private static String[] columns = {"id, name, parent_id, created_at, status, thumbnail"};
	private static ServingCategoryModel SerCateModel;
	
	private int id;
	private int sequence;
	private String name;
	private String parentName;
	private String createdAt;
	private int status;	
	private String path;
	
	public static final int SERVING_CATEGORY_ACTIVATED = 1;
	public static final int SERVING_CATEGORY_DEACTIVATED = 0;
	public static DataMapping isActivated = DataMapping.getInstance(SERVING_CATEGORY_ACTIVATED, "Activated");
	public static DataMapping isDeactivated = DataMapping.getInstance(SERVING_CATEGORY_DEACTIVATED, "Deactivated");
	
	public static boolean isShown = false;
	
	public ServingCategoryModel() {
		super(table, columns);
		if(SerCateModel != null ) {
			ServingCategoryModel item = new ServingCategoryModel();
		}
	}
	public ServingCategoryModel (int sequence, int id, String name, String parentName, String createdAt, int status, String path) {
		super(table, columns);
		this.sequence = sequence;
		this.id = id;
		this.name = name;
		this.parentName = parentName;
		this.createdAt = createdAt;
		this.status = status;
		this.path = path;
	}
	
	//get list
	public ResultSet getServingCategoryList(ArrayList<CompareOperator> conditions) {
		try {
			String[] selects = {"serving_categories.id", "serving_categories.name", "sc.name as parentID",
					"serving_categories.created_at", "serving_categories.status", "serving_categories.thumbnail"};
			
			ArrayList<CompareOperator> joinCondition = new ArrayList<CompareOperator>();
			joinCondition.add(CompareOperator.getInstance("serving_categories.parent_id", "=", "sc.id"));
			
			ArrayList<JoinCondition> joins = new ArrayList<JoinCondition>();
			joins.add(JoinCondition.getInstance("left join", "serving_categories sc", joinCondition));
			return this.getData(selects, conditions, joins, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//get by id
	public ResultSet getSerCateById(int id) {
		try {
			String[] selects = {"serving_categories.*", "sc.name as parentID"};
			
			ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
			conditions.add(CompareOperator.getInstance("serving_categories.id", "=", String.valueOf(id)));
			
			ArrayList<CompareOperator> joinConditions = new ArrayList<CompareOperator>();
			joinConditions.add(CompareOperator.getInstance("sc.id", "=", "serving_categories.id")); 
			
			ArrayList<JoinCondition> joins = new ArrayList<JoinCondition>();
			joins.add(JoinCondition.getInstance("left join", "serving_categories sc", joinConditions));
			
			return this.getData(selects, conditions, joins, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//create
	public int createServingCategory(ArrayList<DataMapping> data) {
		try {
			return this.create(data);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	//update
	public boolean updateServingCategory(int id, ArrayList<DataMapping> data) {
		try {
			ArrayList<CompareOperator> condition = new ArrayList<CompareOperator>();
			condition.add(CompareOperator.getInstance("id", "=", String.valueOf(id)));
			return this.update(data, condition);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
		
	//delete
	public boolean deleteServingCategory(int id) {
		try {
			ArrayList<CompareOperator> condition = new ArrayList<CompareOperator>();
			condition.add(CompareOperator.getInstance("id", "=", String.valueOf(id)));
			return this.delete(condition);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}	
	
	//get & set
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
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
}
