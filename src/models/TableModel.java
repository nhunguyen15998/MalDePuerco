package models;

import java.sql.ResultSet;
import java.util.ArrayList;

import utils.CompareOperator;
import utils.DataMapping;
import utils.JoinCondition;

public class TableModel extends BaseModel{
	private static String table = "tables";
	private static String[] columns = {"id, code, name, seats, created_at, user_id, status, is_set"};
	private static TableModel tableModel;
	private int id, user_id;
	private int sequence;
	private String code, username;
	private String name;
	private int seats;
	private String createdAt;
	private int status;
	private int is_set;
	
	//status

	public static final int TABLE_EMPTY = 0;
	public static final int TABLE_SERVING = 1;
	public static final int TABLE_WAITING = 2; 
	public static final int TABLE_PLACED = 3; 
	public static DataMapping isEmpty = DataMapping.getInstance(TABLE_EMPTY, "Empty");
	public static DataMapping isServing = DataMapping.getInstance(TABLE_SERVING, "Serving");
	public static DataMapping isWaiting = DataMapping.getInstance(TABLE_WAITING, "Waiting");
	public static DataMapping isPlaced = DataMapping.getInstance(TABLE_PLACED, "Placed");
	// is set on tablet
	public static final int YES = 1; 
	public static final int NO = 0;  
	public static DataMapping isSet = DataMapping.getInstance(TABLE_EMPTY, "Yes");
	public static DataMapping isNotSet = DataMapping.getInstance(TABLE_SERVING, "No");
	//constructor
	public TableModel() {
		super(table, columns);
		if(tableModel != null) {
			tableModel = new TableModel();
		}
	}
 
	//get
		public ResultSet getTableList(ArrayList<CompareOperator> conditions) {
			try {	
				
				return this.getData(columns, conditions, null);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

	public TableModel( int id,int sequence,  String code,
				String name, int seats, String createdAt, int status, int is_set,String username,  int user_id) {
			super(table, columns);
			this.id = id;
			this.user_id = user_id;
			this.sequence = sequence;
			this.code = code;
			this.username = username;
			this.name = name;
			this.seats = seats;
			this.createdAt = createdAt;
			this.status = status;
			this.is_set = is_set;
		}

	//get data by id
		public ResultSet getTableById(int id) {
			try {		
				
				ArrayList<CompareOperator> cond = new ArrayList<CompareOperator>();
				cond.add(CompareOperator.getInstance("id", " = ", String.valueOf(id)));
			
				return this.getData(columns, cond, null);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
		public int createTable(ArrayList<DataMapping> data) {
			try {
				return this.create(data);
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
		}
		
		//update
		public boolean updateTableById(int id, ArrayList<DataMapping> data) {
			try {
				
				ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
				conditions.add(CompareOperator.getInstance("id", "=", String.valueOf(id)));
				return this.update(data, conditions);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		
		//delete
		public boolean deleteTableById(int id) {
			try {
				ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
				conditions.add(CompareOperator.getInstance("id", "=", String.valueOf(id)));
				
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

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public int getSequence() {
			return sequence;
		}

		public void setSequence(int sequence) {
			this.sequence = sequence;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getSeats() {
			return seats;
		}

		public void setSeats(int seats) {
			this.seats = seats;
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

		public int getIs_set() {
			return is_set;
		}

		public void setIs_set(int is_set) {
			this.is_set = is_set;
		}

		public int getUser_id() {
			return user_id;
		}

		public void setUser_id(int user_id) {
			this.user_id = user_id;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}
		
		
		
		
		
}
