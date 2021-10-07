package models;

import java.sql.ResultSet;
import java.util.ArrayList;

import utils.CompareOperator;
import utils.DataMapping;
import utils.JoinCondition;

public class ReservationModel extends BaseModel{
	private static String table = "reservations";
	private static String[] columns = {"id, code, customer_name, phone,email, start_time,end_time, deposit,discount_id, date_pick, seats_pick, created_at, status"};
	private static ReservationModel reserModel;
	
	private int id, deposit, status, seats, no;
	private String code, name, phone, email, start_time, end_time, date_pick,decrease, createdAt;
	
	//status
		public static final int RESER_NEW = 1;
		public static final int RESER_CANCELLED = 0;
		public static final int RESER_CONFIRMED = 2;
		public static final int RESER_DEPOSITED = 3;
		public static final int RESER_PRESENT = 4;
		public static final int RESER_EXPIRED = 5;
		
		public static DataMapping isNew = DataMapping.getInstance(RESER_NEW, "New");
		public static DataMapping isCancelled = DataMapping.getInstance(RESER_CANCELLED, "Cancelled");
		public static DataMapping isConfirmed = DataMapping.getInstance(RESER_CONFIRMED, "Confirmed");
		public static DataMapping isDeposited = DataMapping.getInstance(RESER_DEPOSITED, "Deposited");
		public static DataMapping isPresent = DataMapping.getInstance(RESER_PRESENT, "Present");
		public static DataMapping isExpried = DataMapping.getInstance(RESER_EXPIRED, "Expired");
		
		
		public ReservationModel() {
			super(table, columns);
			if(reserModel != null) {
				reserModel = new ReservationModel();
			}
		}
		
		
		
	public static ReservationModel getInstance( int id,int no, int deposit, String decrease, int status,
				int seats, String code, String name, String phone, String email, String start_time, String end_time,
				String date_pick, String createdAt) {
			if(reserModel ==  null) {
				ReservationModel item = new ReservationModel();
				item.id = id;
				item.no = no;
				item.deposit = deposit;
				item.decrease = decrease;
				item.status = status;
				item.seats = seats;
				item.code = code;
				item.name = name;
				item.phone = phone;
				item.email = email;
				item.start_time = start_time;
				item.end_time = end_time;
				item.date_pick = date_pick;
				item.createdAt = createdAt;
				return item;
			}
			return reserModel;
		}


	//get list
		public ResultSet getReserList(ArrayList<CompareOperator> conditions) {
			try {
				String[] selects = {"reservations.*","decrease"};
				
				ArrayList<CompareOperator> joinDiscount = new ArrayList<CompareOperator>();
				joinDiscount.add(CompareOperator.getInstance("reservations.discount_id", " = ", "discounts.id"));
				
				ArrayList<JoinCondition> joins = new ArrayList<JoinCondition>();
				joins.add(JoinCondition.getInstance("join", "discounts", joinDiscount));
				return this.getData(selects, conditions, joins);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
		//get user by id
		public ResultSet getReserById(int id) {
			try {
				String[] selects = {"reservations.*", "discounts.decrease"};
				ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
				conditions.add(CompareOperator.getInstance("reservations.id", "=", String.valueOf(id)));
				
				
				ArrayList<CompareOperator> joinConditions = new ArrayList<CompareOperator>();
				joinConditions.add(CompareOperator.getInstance("reservations.discount_id", "=", "discounts.id"));
				
				ArrayList<JoinCondition> joins = new ArrayList<JoinCondition>();
				joins.add(JoinCondition.getInstance("join", "discounts", joinConditions));
				
				
				 
				return this.getData(selects, conditions, joins);
			} catch (Exception eGetUserById) {
				eGetUserById.printStackTrace();
				return null;
			}
		}
		
		
		//create
		public int createReser(ArrayList<DataMapping> data) {
			try {
				return this.create(data);
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
		}
		
		//update
		public boolean updateReserById(int id, ArrayList<DataMapping> data) {
			try {
				
				ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
				conditions.add(CompareOperator.getInstance("reservations.id", "=", String.valueOf(id)));
				return this.update(data, conditions);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		
		//delete
		public boolean deleteReserById(int id) {
			try {
				ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
				conditions.add(CompareOperator.getInstance("reservations.id", "=", String.valueOf(id)));
				
				return this.delete(conditions);
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
	public int getDeposit() {
		return deposit;
	}
	public void setDeposit(int deposit) {
		this.deposit = deposit;
	}
	
	public String getDecrease() {
		return decrease;
	}

	public void setDecrease(String decrease) {
		this.decrease = decrease;
	}



	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getSeats() {
		return seats;
	}
	public void setSeats(int seats) {
		this.seats = seats;
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
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public String getDate_pick() {
		return date_pick;
	}
	public void setDate_pick(String date_pick) {
		this.date_pick = date_pick;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}






	public int getNo() {
		return no;
	}



	public void setNo(int no) {
		this.no = no;
	}
	
	
}
