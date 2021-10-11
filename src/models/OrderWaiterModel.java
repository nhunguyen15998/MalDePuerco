package models;

import java.sql.ResultSet;
import java.util.ArrayList;

import utils.CompareOperator;
import utils.DataMapping;
import utils.JoinCondition;

public class OrderWaiterModel extends BaseModel{
	private static String table = "orders";
	private static String[] columns = {"id", "code", "table_id", "reservation_id", "tip", 
							"total_amount", "payment_method_id", "created_at", "status"};
	
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
	
	//status
	public static final int	PENDING_ORDER = 0;
	public static final int PROCESSING_ORDER = 1;
	public static final int SERVED_ORDER = 3;
	public static final int COMPLETED_ORDER = 4;
	
	public static DataMapping isPending = DataMapping.getInstance(PENDING_ORDER, "Pending");
	public static DataMapping isProcessing = DataMapping.getInstance(PROCESSING_ORDER, "Processing");
	public static DataMapping isServed = DataMapping.getInstance(SERVED_ORDER, "Served");
	public static DataMapping isCompleted = DataMapping.getInstance(COMPLETED_ORDER, "Complete");
	
	//shown
	public static boolean isShown = false;
	
	//constructors
	public OrderWaiterModel() {
		super(table, columns);
		if(orderWModel != null) {
			orderWModel = new OrderWaiterModel();
			
		}
	}

	public static OrderWaiterModel getInstance(int id, int sequence, String code, String tableID,
											   String reservationID, double tip, double total_amount, 
											   String paymentID, String createdAt, int status) {
		if(orderWModel == null) {
			OrderWaiterModel item = new OrderWaiterModel();
			item.setId(id);
			item.setSequence(sequence);
			item.setCode(code);
			item.setTableID(tableID);
			item.setReservationID(reservationID);
			item.setTip(tip);
			item.setTotal_amount(total_amount);
			item.setPaymentID(paymentID);
			item.setCreatedAt(createdAt);
			item.setStatus(status);
		}
		return orderWModel;
	}
	
	//getList
	public ResultSet getOrderList(ArrayList<CompareOperator> conditions) {
		try {
			
			String[] selects = {"orders.id", "orders.code", "tables.name as tblNameID",
								"reservations.code as reservationCode", "orders.tip", 
								"order.total_amount", "payment_method.name as payment_id", 
								"orders.created_at", "orders.status"};
			ArrayList<CompareOperator> tableJoin = new ArrayList<CompareOperator>();
			tableJoin.add(CompareOperator.getInstance("tables.id", "=", "orders.table_id"));
			
			ArrayList<CompareOperator> reserJoin = new ArrayList<CompareOperator>();
			reserJoin.add(CompareOperator.getInstance("reservations.id", "=", "tables.reservation_id"));
			
			ArrayList<CompareOperator> payJoin = new ArrayList<CompareOperator>();
			payJoin.add(CompareOperator.getInstance("payment_method.id", "=", "tables.payment_method_id"));
			
			ArrayList<JoinCondition> joins = new ArrayList<JoinCondition>();
			joins.add(JoinCondition.getInstance("join", "tables", tableJoin));
			joins.add(JoinCondition.getInstance("join", "reservations", reserJoin));
			joins.add(JoinCondition.getInstance("join", "payment_method", payJoin));
			
			return this.getData(selects, conditions, joins);
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
	
	
}
