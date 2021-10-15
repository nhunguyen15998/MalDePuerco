package models;

import java.sql.ResultSet;
import java.util.ArrayList;
import utils.CompareOperator;
import utils.DataMapping;
import utils.JoinCondition;

public class ServingCategoryModel extends BaseModel {
	private static String table = "serving_categories";
	private static String[] columns = {"id, name, thumbnail, parent_id, created_at, status"};
	
	private int id;
	private int sequence;
	private String name;
	private String thumbnail;
	private String parent;
	private String createdAt;
	private String status;	

	private static final int SERVING_CATEGORY_ACTIVATED = 1;
	private static final int SERVING_CATEGORY_DEACTIVATED = 0;
	public static DataMapping isActivated = DataMapping.getInstance(SERVING_CATEGORY_ACTIVATED, "Activated");
	public static DataMapping isDeactivated = DataMapping.getInstance(SERVING_CATEGORY_DEACTIVATED, "Deactivated");
	
	//type

	public ServingCategoryModel() {
		super(table, columns);
	}
	
	public ServingCategoryModel(int id, int sequence, String name, String thumbnail, String parent, String createdAt, String status) {
		super(table, columns);
		this.setId(id);
		this.setSequence(sequence);
		this.setName(name);
		this.setThumbnail(thumbnail);
		this.setParent(parent);
		this.setCreatedAt(createdAt);
		this.setStatus(status);
	}
	
	//get list
	public ResultSet getServingCategoryList(ArrayList<CompareOperator> conditions) {
		try {
			String[] selects = {"serving_categories.*, sc.id, sc.name as parent_name, sc.*"};
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
			condition.add(CompareOperator.getInstance("serving_categories.id", "=", String.valueOf(id)));
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
			condition.add(CompareOperator.getInstance("serving_categories.id", "=", String.valueOf(id)));
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
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

}
