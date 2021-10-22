package models;

import java.sql.ResultSet;
import java.util.ArrayList;

import utils.CompareOperator;
import utils.DataMapping;
import utils.JoinCondition;

public class OrderModel extends BaseModel {
	private static String table = "orders";
	private static String[] columns = {"id, code, table_id, reservation_id,	tip, total_amount, payment_method, "
										+ "created_at, status, user_id"};
	
	private int id;
	private int sequence;
	private String orderCode;
	private String tableCode;
	private String reservationCode;
	private double tip;
	private double total;
	private String paymentMethod;
	private String createdAt;
	private int status;
	private String userCode;
	private int orderQuantity;
	
	public static int currentOrderId = 0;
	
	//status
	public static final int PENDING = 0;
	public static final int PROCESSING = 1;
	public static final int SERVED = 2;
	public static final int COMPLETED = 3;

	public static DataMapping isPending = DataMapping.getInstance(PENDING, "Pending"); 
	public static DataMapping isProcessing = DataMapping.getInstance(PROCESSING, "Processing"); 
	public static DataMapping isServed = DataMapping.getInstance(SERVED, "Served"); 
	public static DataMapping isCompleted = DataMapping.getInstance(COMPLETED, "Completed"); 

	//shown 
	public static boolean isShown = false;
	
	public OrderModel() {
		super(table, columns);	
	}
	
	public OrderModel(int id, int sequence, String orderCode, String tableCode, String reservationCode, double tip,
					  int orderQuantity, double total, String paymentMethod, String createdAt, int status, String userCode) {
		super(table, columns);	
		this.setId(id);
		this.setSequence(sequence);
		this.setOrderCode(orderCode);
		this.setTableCode(tableCode);
		this.setReservationCode(reservationCode);
		this.setTip(tip);
		this.setTotal(total);
		this.setPaymentMethod(paymentMethod);
		this.setCreatedAt(createdAt);
		this.setStatus(status);
		this.setUserCode(userCode);
	}
	
	public OrderModel(int id, String orderCode, String tableCode, String reservationCode, double tip, int orderQuantity, 
					  double total, String paymentMethod, int status, String userCode) {
		super(table, columns);
		this.setId(id);
		this.setOrderCode(orderCode);
		this.setTableCode(tableCode);
		this.setReservationCode(reservationCode);
		this.setTip(tip);
		this.setOrderQuantity(orderQuantity);
		this.setTotal(total);
		this.setPaymentMethod(paymentMethod);
		this.setStatus(status);
		this.setUserCode(userCode);
	}
	
	//get data - orders - tables - reservations - payment_method
	public ResultSet getOrderList(ArrayList<CompareOperator> conditions) {
		try {
			String[] selects = {"orders.*, tables.code as table_code, reservations.code as reservations_code,"
								+ "payment_method.code as payment_method_code, tables.name,"
								+ "sum(order_details.quantity) as order_quantity"};
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
			userCondition.add(CompareOperator.getInstance("users.id", "=", "orders.user_id"));
			//orderdetail
			ArrayList<CompareOperator> orderDetailCondition = new ArrayList<CompareOperator>();
			orderDetailCondition.add(CompareOperator.getInstance("order_details.order_id", "=", "orders.id"));
			//join
			ArrayList<JoinCondition> joins = new ArrayList<JoinCondition>();
			joins.add(JoinCondition.getInstance("left join", "tables", tableCondition));
			joins.add(JoinCondition.getInstance("left join", "reservations", reservationCondition));
			joins.add(JoinCondition.getInstance("left join", "payment_method", paymentMethodCondition));
			joins.add(JoinCondition.getInstance("left join", "users", userCondition));
			joins.add(JoinCondition.getInstance("left join", "order_details", orderDetailCondition));

			return this.getData(selects, conditions, joins, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//get by id
	public ResultSet getOrderById(int id) {
		try {		
			ArrayList<CompareOperator> condition = new ArrayList<CompareOperator>();
			condition.add(CompareOperator.getInstance("id", " = ", String.valueOf(id)));
			return this.getData(columns, condition, null, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//load combobox
	public ResultSet getStatus(int status) {
		try {
			String[] selects = {"orders.status"};
			ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
			conditions.add(CompareOperator.getInstance("status", "=", "0"));
			conditions.add(CompareOperator.getInstance("status", "=", "1"));
			conditions.add(CompareOperator.getInstance("status", "=", "2"));
			conditions.add(CompareOperator.getInstance("status", "=", "3"));
			
			return this.getData(selects, conditions, null, null, null, null);
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

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
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

	public int getOrderQuantity() {
		return orderQuantity;
	}

	public void setOrderQuantity(int orderQuantity) {
		this.orderQuantity = orderQuantity;
	}

	
}
