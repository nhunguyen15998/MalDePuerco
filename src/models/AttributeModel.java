package models;

import java.sql.ResultSet;
import java.util.ArrayList;
import utils.CompareOperator;
import utils.DataMapping;
import utils.JoinCondition;

public class AttributeModel extends BaseModel {
	private static String table = "attributes";
	private static String[] columns = {"id, name, parent_id, created_at"};

	private int id;
	private int sequence, parent_id;
	private String name;
	private String parent;
	private String createdAt;
	
	public static final int SUGAR = 21;
	public static final int ICE = 22;

	public AttributeModel() {
		super(table, columns);
	}
	
	public AttributeModel(int id, int sequence, String name, String parent, String createdAt) {
		super(table, columns);
		this.setId(id);
		this.setSequence(sequence);
		this.setName(name);
		this.setParent(parent);
		this.setCreatedAt(createdAt);
	}
	
	//get list
	public ResultSet getAttributeList(ArrayList<CompareOperator> conditions) {
		try {
			String[] selects = {"attributes.*, att.name as parent_name", "attributes.parent_id"};

			ArrayList<CompareOperator> parentCondition = new ArrayList<CompareOperator>();
			parentCondition.add(CompareOperator.getInstance("attributes.parent_id", "=", "att.id"));
			ArrayList<JoinCondition> joins = new ArrayList<JoinCondition>();
			joins.add(JoinCondition.getInstance("left join", "attributes att", parentCondition));
			return this.getData(selects, conditions, joins, null, null, null);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	//create
	public int createAttribute(ArrayList<DataMapping> data) {
		try {
			return this.create(data);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	//update
	public boolean updateAttribute(int id, ArrayList<DataMapping> data) {
		try {
			ArrayList<CompareOperator> condition = new ArrayList<CompareOperator>();
			condition.add(CompareOperator.getInstance("attributes.id", "=", String.valueOf(id)));
			return this.update(data, condition);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	
	public boolean updateAttributeById(int id) {
		try {
			ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
			conditions.add(CompareOperator.getInstance("id", "=", String.valueOf(id)));
			return this.update(null, conditions);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

		
	//delete
	public boolean deleteAttribute(int id) {
		try {
			ArrayList<CompareOperator> condition = new ArrayList<CompareOperator>();
			condition.add(CompareOperator.getInstance("attributes.id", "=", String.valueOf(id)));
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

}
