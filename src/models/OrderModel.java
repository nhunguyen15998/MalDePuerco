package models;

import java.sql.ResultSet;
import java.util.ArrayList;

import utils.CompareOperator;
import utils.DataMapping;
import utils.JoinCondition;

public class OrderModel extends BaseModel {
	private static String table = "orders";
	private static String[] columns = {"id, code, table_id, reservation_id,	tip, total_amount, payment_method_id, "
										+ "created_at, status, user_id"};
	
	private int id;
	private int sequence;
	private String orderCode;
	private String tableCode;
	private String reservationCode;
	private double tip;
	private double total;
	private String paymentMethodCode;
	private String createdAt;
	private int status;
	private String userCode;
	
	//status
	private static final int PENDING = 0;
	private static final int PROCESSING = 1;
	private static final int SERVED = 2;
	private static final int COMPLETED = 3;

	public static DataMapping isPending = DataMapping.getInstance(PENDING, "Pending"); 
	public static DataMapping isProcessing = DataMapping.getInstance(PROCESSING, "Processing"); 
	public static DataMapping isServed = DataMapping.getInstance(SERVED, "Served"); 
	public static DataMapping isCompleted = DataMapping.getInstance(COMPLETED, "Completed"); 

	public OrderModel() {
		super(table, columns);	
	}
	
	public OrderModel(int id, int sequence, String orderCode, String tableCode, String reservationCode, double tip,
					  double total, String paymentMethodCode, String createdAt, int status, String userCode) {
		super(table, columns);	
		this.setId(id);
		this.setSequence(sequence);
		this.setOrderCode(orderCode);
		this.setTableCode(tableCode);
		this.setReservationCode(reservationCode);
		this.setTip(tip);
		this.setTotal(total);
		this.setPaymentMethodCode(paymentMethodCode);
		this.setCreatedAt(createdAt);
		this.setStatus(status);
		this.setUserCode(userCode);
	}
	
	//get data - orders - tables - reservations - payment_method
	public ResultSet getOrderList(ArrayList<CompareOperator> conditions) {
		try {
			String[] selects = {"orders.*, tables.code, reservations.code, payment_method.code, tables.code"};
			//table
			ArrayList<CompareOperator> tableCondition = new ArrayList<CompareOperator>();
			tableCondition.add(CompareOperator.getInstance("tables.id", "=", "orders.table_id"));
			//reservation
			ArrayList<CompareOperator> reservationCondition = new ArrayList<CompareOperator>();
			reservationCondition.add(CompareOperator.getInstance("reservations.id", "=", "orders.reservation_id"));
			//payment_method
			ArrayList<CompareOperator> paymentMethodCondition = new ArrayList<CompareOperator>();
			paymentMethodCondition.add(CompareOperator.getInstance("payment_method.id", "=", "orders.payment_method_id"));
			//user
			ArrayList<CompareOperator> userCondition = new ArrayList<CompareOperator>();
			userCondition.add(CompareOperator.getInstance("users.id", "=", "tables.user_id"));
			//join
			ArrayList<JoinCondition> joins = new ArrayList<JoinCondition>();
			joins.add(JoinCondition.getInstance("left join", "tables", tableCondition));
			joins.add(JoinCondition.getInstance("left join", "reservations", reservationCondition));
			joins.add(JoinCondition.getInstance("left join", "payment_method", paymentMethodCondition));
			joins.add(JoinCondition.getInstance("left join", "users", userCondition));
			return this.getData(selects, conditions, joins);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//create
	public int createOrder(ArrayList<DataMapping> data) {
		try {
			return this.create(data);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	//update
	public boolean updateOrder(int id, ArrayList<DataMapping> data) {
		try {
			ArrayList<CompareOperator> condition = new ArrayList<CompareOperator>();
			condition.add(CompareOperator.getInstance("orders.id", "=", String.valueOf(id)));
			return this.update(data, condition);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
		
	//delete
	public boolean deleteOrder(int id) {
		try {
			ArrayList<CompareOperator> condition = new ArrayList<CompareOperator>();
			condition.add(CompareOperator.getInstance("orders.id", "=", String.valueOf(id)));
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

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getTableCode() {
		return tableCode;
	}

	public void setTableCode(String tableCode) {
		this.tableCode = tableCode;
	}

	public String getReservationCode() {
		return reservationCode;
	}

	public void setReservationCode(String reservationCode) {
		this.reservationCode = reservationCode;
	}

	public double getTip() {
		return tip;
	}

	public void setTip(double tip) {
		this.tip = tip;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public String getPaymentMethodCode() {
		return paymentMethodCode;
	}

	public void setPaymentMethodCode(String paymentMethodCode) {
		this.paymentMethodCode = paymentMethodCode;
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

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	
}
