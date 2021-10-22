package models;

import java.sql.ResultSet;
import java.util.ArrayList;

import utils.CompareOperator;
import utils.DataMapping;
import utils.JoinCondition;

public class OrderWaiterModel extends BaseModel{
	private static String table = "orders";
	private static String[] columns = {"id, code, table_id, reservation_id, tip, total_amount, "
									  + "payment_method_id, created_at, status, user_id"};
	
	private static OrderWaiterModel orderWModel;
	
	private int id;
	private int sequence;
	private String code;
	private String tableID;
	private String reservationID;
	private double tip;
	private double total_amount;
	private String paymentID;
	private String createdAt;
	private int status;
	private String userCode;
	private int quantity;
	
	//status
	public static final int	PENDING = 0;
	public static final int PROCESSING = 1;
	public static final int SERVED = 2;
	public static final int COMPLETED = 3;
	
	public static DataMapping isPending = DataMapping.getInstance(PENDING, "Pending");
	public static DataMapping isProcessing = DataMapping.getInstance(PROCESSING, "Processing");
	public static DataMapping isServed = DataMapping.getInstance(SERVED, "Served");
	public static DataMapping isCompleted = DataMapping.getInstance(COMPLETED, "Complete");
	
	//shown
	public static boolean isShown = false;
	
	//constructors
	public OrderWaiterModel() {
		super(table, columns);
		if(orderWModel != null) {
			orderWModel = new OrderWaiterModel();
			
		}
	}

	public OrderWaiterModel (int id, int sequence, String code, String tableID,
											   String reservationID, double tip, double total_amount, 
											   String paymentID, int quantity, int status, String userCode) {
		super(table, columns);
		this.id = id;
		this.sequence = sequence;
		this.code = code;
		this.tableID = tableID;
		this.reservationID = reservationID;
		this.tip = tip;
		this.total_amount = total_amount;
		this.paymentID = paymentID;
		this.quantity = quantity;
		this.status = status;
		this.userCode = userCode;
	}
	
	//getList
	public ResultSet getOrderList(ArrayList<CompareOperator> conditions, String[] groupBys) {
		try {
			
			String[] selects = {"orders.id", "orders.code", "tables.name as tblNameID",
								"reservations.code as reservationCode", "orders.tip", 
								"orders.total_amount", "payment_method.name as payment_id", 
								"sum(order_details.quantity) as orderQuantity", "orders.status", "users.name as userID"};
			ArrayList<CompareOperator> tableJoin = new ArrayList<CompareOperator>();
			tableJoin.add(CompareOperator.getInstance("tables.id", "=", "orders.table_id"));
			
			ArrayList<CompareOperator> reserJoin = new ArrayList<CompareOperator>();
			reserJoin.add(CompareOperator.getInstance("reservations.id", "=", "orders.reservation_id"));
			
			ArrayList<CompareOperator> payJoin = new ArrayList<CompareOperator>();
			payJoin.add(CompareOperator.getInstance("payment_method.id", "=", "orders.payment_method_id"));
			
			ArrayList<CompareOperator> userJoin = new ArrayList<CompareOperator>();
			userJoin.add(CompareOperator.getInstance("users.id", "=", "orders.user_id"));
			
			ArrayList<CompareOperator> orderdetailJoin = new ArrayList<CompareOperator>();
			orderdetailJoin.add(CompareOperator.getInstance("order_details.order_id", "=", "orders.id group by orders.id"));
			
			
			ArrayList<JoinCondition> joins = new ArrayList<JoinCondition>();
			joins.add(JoinCondition.getInstance("left join", "tables", tableJoin));
			joins.add(JoinCondition.getInstance("left join", "reservations", reserJoin));
			joins.add(JoinCondition.getInstance("left join", "payment_method", payJoin));
			joins.add(JoinCondition.getInstance("left join", "users", userJoin));
			joins.add(JoinCondition.getInstance("left join", "order_details", orderdetailJoin));
			
			return this.getData(selects, conditions, joins, groupBys, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
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

	public String getTableID() {
		return tableID;
	}

	public void setTableID(String tableID) {
		this.tableID = tableID;
	}

	public String getReservationID() {
		return reservationID;
	}

	public void setReservationID(String reservationID) {
		this.reservationID = reservationID;
	}

	public double getTip() {
		return tip;
	}

	public void setTip(double tip) {
		this.tip = tip;
	}

	public double getTotal_amount() {
		return total_amount;
	}

	public void setTotal_amount(double total_amount) {
		this.total_amount = total_amount;
	}

	public String getPaymentID() {
		return paymentID;
	}

	public void setPaymentID(String paymentID) {
		this.paymentID = paymentID;
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

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	
	
}
