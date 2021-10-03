package models;

import java.sql.ResultSet;
import java.util.ArrayList;

import utils.CompareOperator;
import utils.DataMapping;
import utils.JoinCondition;
import utils.Helpers;

public class ServingModel extends BaseModel {

	public static final String table = "servings";
	public static final String[] columns = {"no","id", "name", "category_id", "descriptions", "price", "is_new", "is_bestseller", "status"};
	
	private int sequence;
	private int id;
	private String name;
	private int category_id;
	private String descriptions;
	private int price;
	private int is_new;
	private int is_bestseller;
	private int status;
	
	//contructor - overloading
		public ServingModel() {
			super(table, columns);
			// TODO Auto-generated constructor stub
		}
		
		public ServingModel(int sequence, int id, String name, int category_id, String descriptions,
							int price, int is_new, int is_bestseller, int status) {
			super(table, columns);
			this.setSequence(sequence);
			this.setId(id);
			this.setName(name);
			this.setCategory_id(category_id);
			this.setDescriptions(descriptions);
			this.setPrice(price);
			this.setIs_new(is_new);
			this.setIs_bestseller(is_bestseller);
			this.setStatus(status);
			
		}
	
	public static final int SERVINGS_ACTIVATED = 1;
	public static final int SERVINGS_DEACTIVATED = 0;
	public static DataMapping isActivated = new DataMapping();
	public static DataMapping isDeactivated = new DataMapping();
	
	//get data
	public ResultSet getServingsList(ArrayList<CompareOperator> conditions) {
		try {
			return this.getData(columns, conditions, null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
//	//get by id
//	public ResultSet getServingById(int id) {
//		try {
//			ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
//			conditions.add(new CompareOperator("id", "=", String.valueOf(id)));
//			return this.getData(columns, conditions, null);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
	
	public int getSequence() {
		return sequence;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getCategory_id() {
		return category_id;
	}

	public String getDescriptions() {
		return descriptions;
	}

	public int getPrice() {
		return price;
	}

	public int getIs_new() {
		return is_new;
	}

	public int getIs_bestseller() {
		return is_bestseller;
	}

	public int getStatus() {
		return status;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}

	public void setDescriptions(String descriptions) {
		this.descriptions = descriptions;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public void setIs_new(int is_new) {
		this.is_new = is_new;
	}

	public void setIs_bestseller(int is_bestseller) {
		this.is_bestseller = is_bestseller;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	
}
