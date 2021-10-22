package models;

import java.sql.ResultSet;
import java.util.ArrayList;

import utils.CompareOperator;
import utils.DataMapping;
import utils.JoinCondition;

public class ReviewOrderModel extends BaseModel {
	private static String table = "review_order";
	private static String[] columns = {"id, order_id, rate, comment, created_at"};
	
	private int id;
	private int sequence;
	private int orderId;
	private int rate;
	private String comment;
	private String createdAt;
	
	public static final int EXTREMELY_DISAPPOINTED = 1;
	public static final int DISAPPOINTED = 2;
	public static final int NORMAL = 3;
	public static final int SATISFIED = 4;
	public static final int EXTREMELY_SATISFIED = 5;

	public ReviewOrderModel() {
		super(table, columns);
	}
	
	public ReviewOrderModel(int id, int sequence, int orderId, int rate, String comment, String createdAt) {
		super(table, columns);
		this.setId(id);
		this.setSequence(sequence);
		this.setOrderId(orderId);
		this.setRate(rate);
		this.setComment(comment);
		this.setCreatedAt(createdAt);
	}
	
	//get list
	public ResultSet getReviewOrderList(ArrayList<CompareOperator> conditions) {
		try {
			String[] selects = {"review_order.*, orders.code"};
			ArrayList<CompareOperator> order = new ArrayList<CompareOperator>();
			order.add(CompareOperator.getInstance("review_order.order_id", "=", "orders.id"));
			ArrayList<JoinCondition> orderJoins = new ArrayList<JoinCondition>();
			orderJoins.add(JoinCondition.getInstance("left join", "orders", order));
			return this.getData(selects, conditions, orderJoins, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//create
	public int createReviewOrder(ArrayList<DataMapping> data) {
		try {
			return this.create(data);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	//update
	public boolean updateReviewOrder(int id, ArrayList<DataMapping> data) {
		try {
			ArrayList<CompareOperator> condition = new ArrayList<CompareOperator>();
			condition.add(CompareOperator.getInstance("review_order.id", "=", String.valueOf(id)));
			return this.update(data, condition);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
		
	//delete
	public boolean deleteReviewOrder(int id) {
		try {
			ArrayList<CompareOperator> condition = new ArrayList<CompareOperator>();
			condition.add(CompareOperator.getInstance("review_order.id", "=", String.valueOf(id)));
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

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
}
