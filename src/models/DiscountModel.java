package models;

import java.sql.ResultSet;
import java.util.ArrayList;

import utils.CompareOperator;
import utils.DataMapping;

public class DiscountModel extends BaseModel{
	private static String table = "discounts";
	private static String[] columns = {"id, code, name, descriptions, order_total, start_date, end_date, created_at, decrease, status"};

	private int id;
	private int sequence;
	private String code;
	private String name;
	private String descriptions;
	private double orderTotal;
	private String start_date;
	private String end_date;
	private String createdAt;
	private int status;
	private float decrease;

	//status
	public static final int DISCOUNT_ACTIVATED = 1;
	public static final int DISCOUNT_DEACTIVATED = 0;
	public static DataMapping isActivated = DataMapping.getInstance(DISCOUNT_ACTIVATED, "Activated");
	public static DataMapping isDeactivated = DataMapping.getInstance(DISCOUNT_DEACTIVATED, "Deactivated");

	public DiscountModel() {
		super(table, columns);
	}

	public DiscountModel( int id, int sequence, String code, String name, double orderTotal,
			String descriptions, String start_date, String end_date, String createdAt, int status, float decrease) {
		super(table, columns);	
		this.setId(id);
		this.setSequence(sequence);
		this.setCode(code);
		this.setName(name);
		this.setOrderTotal(orderTotal);
		this.setDescriptions(descriptions);
		this.setStart_date(start_date);
		this.setEnd_date(end_date);
		this.setCreatedAt(createdAt);
		this.setStatus(status);
		this.setDecrease(decrease);
	}
	
	//get
	public ResultSet getDiscountList(ArrayList<CompareOperator> conditions) {
		try {	
			return this.getData(columns, conditions, null, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public int createDiscount(ArrayList<DataMapping> data) {
		try {
			return this.create(data);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	//update
	public boolean updateDiscountById(int id, ArrayList<DataMapping> data) {
		try {
			
			ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
			conditions.add(CompareOperator.getInstance("id", "=", String.valueOf(id)));
			return this.update(data, conditions);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean updateDiscount(ArrayList<DataMapping> data, ArrayList<CompareOperator> conditions) {
		try {
			return this.update(data, conditions);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//delete
	public boolean deleteDiscountById(int id) {
		try {
			ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
			conditions.add(CompareOperator.getInstance("id", "=", String.valueOf(id)));
			
			return this.delete(conditions);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//get &set
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(String descriptions) {
		this.descriptions = descriptions;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}


	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
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

	public float getDecrease() {
		return decrease;
	}

	public void setDecrease(float decrease) {
		this.decrease = decrease;
	}

	public double getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal(double orderTotal) {
		this.orderTotal = orderTotal;
	}

		
}
