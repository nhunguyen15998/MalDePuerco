package models;

import java.sql.ResultSet;
import java.util.ArrayList;

import utils.CompareOperator;
import utils.DataMapping;
import utils.JoinCondition;

public class PaymentMethodModel extends BaseModel {
	private static String table = "payment_method";
	private static String[] columns = {"id, code, name, created_at, status"};
	
	private int id;
	private int sequence;
	private String paymentMethodCode;
	private String name;
	private String createdAt;
	private int status;

	//status
	public static final int ACTIVATED_PAYMENT_METHOD = 0;
	public static final int DEACTIVATED_PAYMENT_METHOD = 1;

	public static DataMapping isActivated = DataMapping.getInstance(ACTIVATED_PAYMENT_METHOD, "Activated"); 
	public static DataMapping isDeactivated = DataMapping.getInstance(DEACTIVATED_PAYMENT_METHOD, "Deactivated"); 

	public PaymentMethodModel() {
		super(table, columns);	
	}
	
	public PaymentMethodModel(int id, int sequence, String paymentMethodCode, String name, String createdAt, int status) {
		super(table, columns);	
		this.setId(id);
		this.setSequence(sequence);
		this.setPaymentMethodCode(paymentMethodCode);
		this.setName(name);
		this.setCreatedAt(createdAt);
		this.setStatus(status);
	}
	
	//get data 
	public ResultSet getPaymentMethod(ArrayList<CompareOperator> conditions) {
		try {
			return this.getData(columns, conditions, null, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//create
	public int createPaymentMethod(ArrayList<DataMapping> data) {
		try {
			return this.create(data);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	//update
	public boolean updatePaymentMethod(int id, ArrayList<DataMapping> data) {
		try {
			ArrayList<CompareOperator> condition = new ArrayList<CompareOperator>();
			condition.add(CompareOperator.getInstance("payment_method.id", "=", String.valueOf(id)));
			return this.update(data, condition);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
		
	//delete
	public boolean deletePaymentMethod(int id) {
		try {
			ArrayList<CompareOperator> condition = new ArrayList<CompareOperator>();
			condition.add(CompareOperator.getInstance("payment_method.id", "=", String.valueOf(id)));
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

	public String getPaymentMethodCode() {
		return paymentMethodCode;
	}

	public void setPaymentMethodCode(String paymentMethodCode) {
		this.paymentMethodCode = paymentMethodCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
}
