package models;

import java.sql.ResultSet;
import java.util.ArrayList;

import utils.CompareOperator;
import utils.DataMapping;
import utils.JoinCondition;

public class ServingImageModel extends BaseModel {
	private static String table = "serving_image";
	private static String[] columns = {"id, serving_id, path, created_at, status"};
	
	private int id;
	private int sequence;
	private String servingName;
	private String path;
	private String createdAt;
	private String status;	
	
	private static final int SERVING_IMAGE_ACTIVATED = 1;
	private static final int SERVING_IMAGE_DEACTIVATED = 0;
	public static DataMapping isActivated = DataMapping.getInstance(SERVING_IMAGE_ACTIVATED, "Activated");
	public static DataMapping isDeactivated = DataMapping.getInstance(SERVING_IMAGE_DEACTIVATED, "Deactivated");
	
	public ServingImageModel() {
		super(table, columns);
	}
	
	public ServingImageModel(int id, int sequence, String servingName, String path, String createdAt, String status) {
		super(table, columns);
		this.setId(id);
		this.setSequence(sequence);
		this.setServingName(servingName);
		this.setPath(path);
		this.setCreatedAt(createdAt);
		this.setStatus(status);
	}
	
	//get list
	public ResultSet getServingImageList(ArrayList<CompareOperator> conditions) {
		try {
			String[] selects = {"s.name as serving_name, serving_image.*"};
			ArrayList<CompareOperator> joinCondition = new ArrayList<CompareOperator>();
			joinCondition.add(CompareOperator.getInstance("serving_image.serving_id", "=", "s.id"));
			ArrayList<JoinCondition> joins = new ArrayList<JoinCondition>();
			joins.add(JoinCondition.getInstance("left join", "servings s", joinCondition));
			return this.getData(selects, conditions, joins, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//create
	public int createServingImage(ArrayList<DataMapping> data) {
		try {
			return this.create(data);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	//update
	public boolean updateServingImage(int id, ArrayList<DataMapping> data) {
		try {
			ArrayList<CompareOperator> condition = new ArrayList<CompareOperator>();
			condition.add(CompareOperator.getInstance("serving_image.id", "=", String.valueOf(id)));
			return this.update(data, condition);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
		
	//delete
	public boolean deleteServingImage(int id) {
		try {
			ArrayList<CompareOperator> condition = new ArrayList<CompareOperator>();
			condition.add(CompareOperator.getInstance("serving_image.id", "=", String.valueOf(id)));
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

	public String getServingName() {
		return servingName;
	}

	public void setServingName(String servingName) {
		this.servingName = servingName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
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

}
