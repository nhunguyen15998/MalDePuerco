package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import utils.CompareOperator;
import utils.DataMapping;
import utils.Helpers;
import utils.JoinCondition;

import org.springframework.security.crypto.bcrypt.BCrypt;

import db.MySQLJDBC;
public class UserModel extends BaseModel {
	public static String table = "users";
	public static String[] columns = {"id", "code", "name", "email", "phone", "password", "role_id", "created_at", "status"};
	private static UserModel userModel;
	private int id;
	private int sequence;
	private String code;
	private String name;
	private String email;
	private String phone;
	private String password;
	private String role;
	private String createdAt;
	private int status;
	
	//gender
	public static final int MALE_USER = 0;
	public static final int FEMALE_USER = 1;
	public static final int OTHER_USER = 2;
	public static DataMapping isMale = DataMapping.getInstance(MALE_USER, "Male");
	public static DataMapping isFemale = DataMapping.getInstance(FEMALE_USER, "Female");
	public static DataMapping isOther = DataMapping.getInstance(OTHER_USER, "Other");
	
	//status
	public static final int USER_ACTIVATED = 1;
	public static final int USER_DEACTIVATED = 0;
	public static DataMapping isActivated = DataMapping.getInstance(USER_ACTIVATED, "Activated");
	public static DataMapping isDeactivated = DataMapping.getInstance(USER_DEACTIVATED, "Deactivated");
	
	//shown
	public static boolean isShown = false;
	
	//constructors
	public UserModel() {
		super(table, columns);
		if(userModel != null) {
			userModel = new UserModel();
		}
	}
	
	public UserModel(int id, int sequence, String code, String name, String email,
			String phone, String role, String createdAt, int status) {
		super(table, columns);
		this.id = id;
		this.sequence = sequence;
		this.code = code;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.role = role;
		this.createdAt = createdAt;
		this.status = status;
	}

	
	//login
	public boolean doLogin(String phone, String password) {
		boolean check= false;
		try {
			String[] selects = {"phone", "password"};
			ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
			conditions.add(CompareOperator.getInstance("phone", "=", phone));
			ResultSet rs = this.getData(selects, conditions, null);
			if(rs.next()) { 
				boolean checkpass = BCrypt.checkpw(password,rs.getString("password"));
				System.out.println("check pass:"+checkpass);
				if(checkpass) {
					check=true;
					loginSupport(rs.getString("phone"));
				}
			}
			
		} catch (Exception eLogin) {
			eLogin.printStackTrace();
		}
		return check;
	}
	public ResultSet loginSupport(String phone) {
		try {
			
			String[] selects = {"id", "name"};
			ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
			conditions.add(CompareOperator.getInstance("phone", "=", phone));
			conditions.add(CompareOperator.getInstance("status", "=", String.valueOf(USER_ACTIVATED)));
			ResultSet results = this.getData(selects, conditions, null);
			return results;
		} catch (Exception eLogin) {
			eLogin.printStackTrace();
			return null;
		}
	}
	
	//get list
	public ResultSet getUserList(ArrayList<CompareOperator> conditions) {
		try {
			String[] selects = {"users.id", "users.name as user_name", "users.code", 
								"users.email", "users.phone", "users.password", 
								"users.created_at", "users.status",
								"roles.name as role_name", "roles.code as role_code"};
			
			ArrayList<CompareOperator> joinRole = new ArrayList<CompareOperator>();
			joinRole.add(CompareOperator.getInstance("users.role_id", " = ", "roles.id"));
			
			ArrayList<JoinCondition> joins = new ArrayList<JoinCondition>();
			joins.add(JoinCondition.getInstance(" join ", " roles ", joinRole));
			return this.getData(selects, conditions, joins);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//get user by id
	public ResultSet getUserById(int id) {
		try {
			String[] selects = {"users.*", "users.name as user_name", "roles.name as role_name"};
			ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
			conditions.add(CompareOperator.getInstance("users.id", "=", String.valueOf(id)));
			
			
			ArrayList<CompareOperator> joinConditions = new ArrayList<CompareOperator>();
			joinConditions.add(CompareOperator.getInstance("roles.id", "=", "users.role_id"));
			
			ArrayList<JoinCondition> joins = new ArrayList<JoinCondition>();
			joins.add(JoinCondition.getInstance("join", "roles", joinConditions));
			
			
			
			return this.getData(selects, conditions, joins);
		} catch (Exception eGetUserById) {
			eGetUserById.printStackTrace();
			return null;
		}
	}
	
	//create
	public int createUser(ArrayList<DataMapping> data) {
		try {
			return this.create(data);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	//update
	public boolean updateUserById(int id, ArrayList<DataMapping> data) {
		try {
			
			ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
			conditions.add(CompareOperator.getInstance("users.id", "=", String.valueOf(id)));
			return this.update(data, conditions);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//delete
	public boolean deleteUserById(int id) {
		try {
			ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
			conditions.add(CompareOperator.getInstance("users.id", "=", String.valueOf(id)));
			
			return this.delete(conditions);
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
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
