package models;

import java.sql.ResultSet;
import java.util.ArrayList;

import utils.CompareOperator;
import utils.DataMapping;
import utils.JoinCondition;

public class OrderDetailModel extends BaseModel {
	private static String table = "order_details";
	private static String[] columns = {"id, order_id, serving_id, serving_note, serving_status, quantity, "
										+ "price, total, user_id, created_at, ice, sugar, size"};
	
	private int id;
	private int sequence;
	private String orderCode;
	private int servingId;
	private String servingCode;
	private String servingName;
	private String thumbnail;
	private String servingNote;
	private int servingStatus;
	private int quantity;
	private double price;
	private double total;
	private String userCode;
	private String createdAt;
	private String ice;
	private String sugar;
	private String size;
	
	//status
	public static final int PENDING = 0;
	public static final int COOKING = 1;
	public static final int READY = 2;
	public static final int SERVING = 3;
	public static final int SERVED = 4;
	public static final int CANCELED = 5;

	public static DataMapping isPending = DataMapping.getInstance(PENDING, "Pending"); 
	public static DataMapping isCooking = DataMapping.getInstance(COOKING, "Cooking"); 
	public static DataMapping isReady = DataMapping.getInstance(READY, "Ready"); 
	public static DataMapping isServing = DataMapping.getInstance(SERVING, "Serving"); 
	public static DataMapping isServed = DataMapping.getInstance(SERVED, "Served"); 
	public static DataMapping isCanceled = DataMapping.getInstance(CANCELED, "Canceled"); 

	public static boolean isShown = false;
	
	public OrderDetailModel() {
		super(table, columns);	
	}
	
	public OrderDetailModel(int id, int sequence, String orderCode, String servingCode, String thumbnail, String servingNote,
							int servingStatus, int quantity, double price, double total, String userCode) {
		super(table, columns);	
		this.setId(id);
		this.setSequence(sequence);
		this.setOrderCode(orderCode);
		this.setServingCode(servingCode);
		this.setThumbnail(thumbnail);
		this.setServingNote(servingNote);
		this.setServingStatus(servingStatus);
		this.setQuantity(quantity);
		this.setPrice(price);
		this.setTotal(total);
		this.setUserCode(userCode);
	}
	
	//overloading of updated order list
	public OrderDetailModel(int servingId, String thumbnail, String servingName, double price, double total,
			String servingNote, int quantity, int servingStatus, String createdAt, String ice, String sugar, String size) {
		super(table, columns);	
		this.setServingId(servingId);
		this.setThumbnail(thumbnail);
		this.setServingName(servingName);
		this.setPrice(price);
		this.setTotal(total);
		this.setServingNote(servingNote);
		this.setQuantity(quantity);
		this.setServingStatus(servingStatus);
		this.setCreatedAt(createdAt);
		this.setIce(ice);
		this.setSugar(sugar);
		this.setSize(size);
	}
	
	public OrderDetailModel(int id, int sequence, String ordercode, String servingName, String size,
			int quantity, int price, int total, String userCode, String createdAt, int servingStatus) {
		super(table, columns);
		this.id = id;
		this.sequence = sequence;
		this.orderCode = ordercode;
		this.servingName = servingName;
		this.size = size;
		this.quantity = quantity;
		this.price = price;
		this.total = total;
		this.userCode = userCode;
		this.createdAt = createdAt;
		this.servingStatus = servingStatus;
	}
	
	//get data order_details - orders - servings - users - serving_image -tables
	public ResultSet getOrderDetailList(ArrayList<CompareOperator> conditions, String orderBys) {
		try {
			String[] selects = {"order_details.*, orders.id, orders.table_id, orders.code, orders.status, orders.total_amount,"
								+ "servings.thumbnail, servings.name, users.code"};
			//orders
			ArrayList<CompareOperator> orderCondition = new ArrayList<CompareOperator>();
			orderCondition.add(CompareOperator.getInstance("orders.id", "=", "order_details.order_id"));
			//servings
			ArrayList<CompareOperator> servingCondition = new ArrayList<CompareOperator>();
			servingCondition.add(CompareOperator.getInstance("servings.id", "=", "order_details.serving_id"));
			//user
			ArrayList<CompareOperator> userCondition = new ArrayList<CompareOperator>();
			userCondition.add(CompareOperator.getInstance("users.id", "=", "order_details.user_id"));
			//tables
			ArrayList<CompareOperator> tableCondition = new ArrayList<CompareOperator>();
			tableCondition.add(CompareOperator.getInstance("tables.id", "=", "orders.table_id"));
			//join
			ArrayList<JoinCondition> joins = new ArrayList<JoinCondition>();
			joins.add(JoinCondition.getInstance("left join", "orders", orderCondition));
			joins.add(JoinCondition.getInstance("left join", "servings", servingCondition));
			joins.add(JoinCondition.getInstance("left join", "users", userCondition));
			joins.add(JoinCondition.getInstance("left join", "tables", tableCondition));
			return this.getData(selects, conditions, joins, null, orderBys, null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ResultSet getOrderDetailListD(ArrayList<CompareOperator> conditions) {
		try {
			String[] selects = {"order_details.id", "orders.code as code", "servings.name as serName",
								"order_details.size", "order_details.quantity", "order_details.price",
								"order_details.total", "users.code as uCode", "time(order_details.created_at) as time", "order_details.serving_status"};
			//orders
			ArrayList<CompareOperator> orderCondition = new ArrayList<CompareOperator>();
			orderCondition.add(CompareOperator.getInstance("orders.id", "=", "order_details.order_id"));
			//servings
			ArrayList<CompareOperator> servingCondition = new ArrayList<CompareOperator>();
			servingCondition.add(CompareOperator.getInstance("servings.id", "=", "order_details.serving_id"));
			//user
			ArrayList<CompareOperator> userCondition = new ArrayList<CompareOperator>();
			userCondition.add(CompareOperator.getInstance("users.id", "=", "order_details.user_id"));
			//tables
			ArrayList<CompareOperator> tableCondition = new ArrayList<CompareOperator>();
			tableCondition.add(CompareOperator.getInstance("tables.id", "=", "orders.table_id"));
			//join
			ArrayList<JoinCondition> joins = new ArrayList<JoinCondition>();
			joins.add(JoinCondition.getInstance("left join", "orders", orderCondition));
			joins.add(JoinCondition.getInstance("left join", "servings", servingCondition));
			joins.add(JoinCondition.getInstance("left join", "users", userCondition));
			joins.add(JoinCondition.getInstance("left join", "tables", tableCondition));
			return this.getData(selects, conditions, joins, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ResultSet getById(int id) {
		try {
			String[] selects = {"order_details.id", "orders.code as code", "servings.name as serName",
					"order_details.size", "order_details.quantity", "order_details.price",
					"order_details.total", "users.code as uCode", "order_details.user_id","time(order_details.created_at) as time", "order_details.serving_status"};
			
			ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
			conditions.add(CompareOperator.getInstance("order_details.id", "=", String.valueOf(id)));
			//orders
			ArrayList<CompareOperator> orderCondition = new ArrayList<CompareOperator>();
			orderCondition.add(CompareOperator.getInstance("orders.id", "=", "order_details.order_id"));
			//servings
			ArrayList<CompareOperator> servingCondition = new ArrayList<CompareOperator>();
			servingCondition.add(CompareOperator.getInstance("servings.id", "=", "order_details.serving_id"));
			//user
			ArrayList<CompareOperator> userCondition = new ArrayList<CompareOperator>();
			userCondition.add(CompareOperator.getInstance("users.id", "=", "order_details.user_id"));
			//tables
			ArrayList<CompareOperator> tableCondition = new ArrayList<CompareOperator>();
			tableCondition.add(CompareOperator.getInstance("tables.id", "=", "orders.table_id"));
			//join
			ArrayList<JoinCondition> joins = new ArrayList<JoinCondition>();
			joins.add(JoinCondition.getInstance("left join", "orders", orderCondition));
			joins.add(JoinCondition.getInstance("left join", "servings", servingCondition));
			joins.add(JoinCondition.getInstance("left join", "users", userCondition));
			joins.add(JoinCondition.getInstance("left join", "tables", tableCondition));
			return this.getData(selects, conditions, joins, null, null, null);
		} catch (Exception e) {
			e.printStackTrace(); 
			return null;
		}
	}
	
	//create
	public int createOrderDetail(ArrayList<DataMapping> data) {
		try {
			return this.create(data);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	//update
	public boolean updateOrderDetail(int id, ArrayList<DataMapping> data) {
		try {
			ArrayList<CompareOperator> condition = new ArrayList<CompareOperator>();
			condition.add(CompareOperator.getInstance("order_details.id", "=", String.valueOf(id)));
			return this.update(data, condition);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
		
	//delete
	public boolean deleteOrderDetail(int id) {
		try {
			ArrayList<CompareOperator> condition = new ArrayList<CompareOperator>();
			condition.add(CompareOperator.getInstance("order_details.id", "=", String.valueOf(id)));
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

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getServingCode() {
		return servingCode;
	}

	public void setServingCode(String servingCode) {
		this.servingCode = servingCode;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getServingNote() {
		return servingNote;
	}

	public void setServingNote(String servingNote) {
		this.servingNote = servingNote;
	}

	public int getServingStatus() {
		return servingStatus;
	}

	public void setServingStatus(int servingStatus) {
		this.servingStatus = servingStatus;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public String getServingName() {
		return servingName;
	}

	public void setServingName(String servingName) {
		this.servingName = servingName;
	}

	public int getServingId() {
		return servingId;
	}

	public void setServingId(int servingId) {
		this.servingId = servingId;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getIce() {
		return ice;
	}

	public void setIce(String ice) {
		this.ice = ice;
	}

	public String getSugar() {
		return sugar;
	}

	public void setSugar(String sugar) {
		this.sugar = sugar;
	}
	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public static String getTable() {
		return table;
	}

	public static void setTable(String table) {
		OrderDetailModel.table = table;
	}

	
}
 
