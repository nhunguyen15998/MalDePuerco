package models;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;

import utils.CompareOperator;
import utils.DataMapping;
import utils.JoinCondition;

public class UserModel extends BaseModel {
	public static String table = "users";
	public static String[] columns = {"id", "code", "name", "gender", "birth", "salary", "email",
									  "phone", "password", "role_id", "created_at", "status"};
	private UserModel userModel;
	private int id;
	private int sequence;
	private String code;
	private String name;
	private int gender;
	private String birth;
	private double salary;
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
	
	public UserModel(int id, int sequence, String code, String name, int gender, String birth, double salary, String email, String phone, String role, String createdAt, int status) {
		super(table, columns);
		this.setId(id);
		this.setSequence(sequence);
		this.setCode(code);
		this.setName(name);
		this.setGender(gender);
		this.setBirth(birth);
		this.setSalary(salary);
		this.setEmail(email);
		this.setPhone(phone);
		this.setBranchName(branchName);
		this.setUserType(userType);
		this.setCreatedAt(createdAt);
		this.setStatus(status);
	}
	
	//login
	public ResultSet doLogin(String phone, String password) {
		try {
			//String hashPassword = Helpers.sha256(password);
			
			String[] selects = {"id", "name"};
			ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
			conditions.add(CompareOperator.getInstance("phone", "=", phone));
			//conditions.add(CompareOperator.getInstance("password", "=", hashPassword));
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
			String[] selects = {"users.id", "users.name as user_name", "users.user_code", "users.gender", "users.birth", 
								"users.identity_card", "users.salary", "users.email", "users.phone", "users.password", 
								"users.created_at", "users.status", "branches.name as branch", "user_types.user_type_code as role_code",
								"user_types.name as role_name"};
			
			ArrayList<CompareOperator> joinBranch = new ArrayList<CompareOperator>();
			joinBranch.add(CompareOperator.getInstance("users.branch_id", " = ", "branches.id"));
			ArrayList<CompareOperator> joinUserType = new ArrayList<CompareOperator>();
			joinUserType.add(CompareOperator.getInstance("users.user_type_id", " = ", "user_types.id"));
			
			ArrayList<JoinCondition> joins = new ArrayList<JoinCondition>();
			joins.add(JoinCondition.getInstance(" join ", " branches ", joinBranch));
			joins.add(JoinCondition.getInstance(" join ", " user_types ", joinUserType));
			return this.getData(selects, conditions, joins);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//get user by id
	public ResultSet getUserById(int id) {
		try {
			String[] selects = {"users.*", "users.name as user_name", "user_types.name as role_name"};
			ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
			conditions.add(CompareOperator.getInstance("users.id", "=", String.valueOf(id)));
			
			
			ArrayList<CompareOperator> joinConditions = new ArrayList<CompareOperator>();
			joinConditions.add(CompareOperator.getInstance("user_types.id", "=", "users.user_type_id"));
			
			ArrayList<JoinCondition> joins = new ArrayList<JoinCondition>();
			joins.add(JoinCondition.getInstance("join", "user_types", joinConditions));
			
			
			
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
	
	//get list user types
	public ResultSet getUserTypeList() {
		try {
			String[] select = {"distinct user_types.name, users.user_type_id, user_types.id"};
			ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
			conditions.add(CompareOperator.getInstance("users.user_type_id", " = ", "user_types.id"));
			ArrayList<JoinCondition> joins = new ArrayList<JoinCondition>();
			joins.add(JoinCondition.getInstance("right join", "user_types", conditions));
			
			return this.getData(select, null, joins);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	//get user salary by id
	public ResultSet getUserSalaryById() {
		try {
			String[] selects = {"user_salary.*, users.id"};
			ArrayList<CompareOperator> conditions = new ArrayList<CompareOperator>();
			conditions.add(CompareOperator.getInstance("user_salary.user_id", " = ", "users.id"));
			ArrayList<JoinCondition> joins = new ArrayList<JoinCondition>();
			joins.add(JoinCondition.getInstance("join", "user_salary", conditions));
			return this.getData(selects, null, joins);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
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

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
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

	public void setUserType(String userType) {
		this.role = userType;
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
