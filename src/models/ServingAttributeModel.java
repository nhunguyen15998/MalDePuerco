package models;

import java.sql.ResultSet;
import java.util.ArrayList;

import utils.CompareOperator;
import utils.DataMapping;
import utils.JoinCondition;

public class ServingAttributeModel extends BaseModel {
	private static String table = "serving_attributes";
	private static String[] columns = {"id, serving_id, attribute, price, created_at"};
	
	private int id;
	private int servingId;
	private String attribute;
	private double price;
	private String createdAt;
	
	//sml,1,2,4,..
	public static final int SIZE_S = 0;
	public static final int SIZE_M = 1;
	public static final int SIZE_L = 2;

	public static final int ONE_PERSON = 3;
	public static final int TWO_PEOPLE = 4;
	public static final int FOUR_PEOPLE = 5;

	public static DataMapping isSizeS = DataMapping.getInstance(SIZE_S, "Size S");
	public static DataMapping isSizeM = DataMapping.getInstance(SIZE_M, "Size M");
	public static DataMapping isSizeL = DataMapping.getInstance(SIZE_L, "Size L");
	
	public static DataMapping OnePerson = DataMapping.getInstance(ONE_PERSON, "For 1 person");
	public static DataMapping TwoPeople = DataMapping.getInstance(TWO_PEOPLE, "For 2 people");
	public static DataMapping FourPeople = DataMapping.getInstance(FOUR_PEOPLE, "For 4 people");

	public ServingAttributeModel() {
		super(table, columns);
	}
	
	public ServingAttributeModel(int id, int servingId, String attribute, double price, String createdAt) {
		super(table, columns);
		this.setId(id);
		this.setServingId(servingId);
		this.setAttribute(attribute);
		this.setPrice(price);
		this.setCreatedAt(createdAt);
	}
	
	//get data
	public ResultSet getServingAttributeList(ArrayList<CompareOperator> conditions) {
		try {
			String[] selects = {"serving_attributes.*, servings.name, "
					+ "(servings.price + serving_attributes.price) as price_of_item_with_attribute"};
			ArrayList<CompareOperator> servingJoin = new ArrayList<CompareOperator>();
			servingJoin.add(CompareOperator.getInstance("serving_attributes.serving_id", "=", "servings.id"));
			ArrayList<JoinCondition> joins = new ArrayList<JoinCondition>();
			joins.add(JoinCondition.getInstance("left join", "servings", servingJoin));
			return this.getData(selects, conditions, joins, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	//get & set
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getServingId() {
		return servingId;
	}

	public void setServingId(int servingId) {
		this.servingId = servingId;
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

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	
	
	
	
}
