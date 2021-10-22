package utils;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import controllers.SettingController;

import java.text.ParseException;

import db.MySQLJDBC;
import javafx.scene.control.Label;

public class Validations {
	
	
	//required|min:4|max:5
	public static ArrayList<DataMapping> validated(ArrayList<ValidationDataMapping> datas) {
		ArrayList<DataMapping> messages = new ArrayList<DataMapping>();
		try {
			
			if(datas == null || datas.size() == 0 ) {
				return messages;
			}
			
			for (ValidationDataMapping item : datas) {
				DataMapping message = new DataMapping();
				message.key = item.getError();
				
				String pattern = item.getPattern();
				//System.out.println(pattern);
				
				String value = item.getField();
				System.out.println(item.getFieldName() + " " + item.getField());
				
				if(pattern.isEmpty()) {
					continue;
				}
				
				String[] patternDatas = pattern.split("\\|");

				//optional
				if(Arrays.asList(patternDatas).contains("option") && value == null || Arrays.asList(patternDatas).contains("option") && value.isEmpty()) {
					continue;
				}
				
				//required
				if(Arrays.asList(patternDatas).contains("required") && value == null || Arrays.asList(patternDatas).contains("required") && value.isEmpty()) {
					message.value = "Field "+item.getFieldName()+" is required";
					messages.add(message);
					continue;
				}
				
				//numeric
				if(Arrays.asList(patternDatas).contains("numeric")&&!value.isEmpty()) {
					if(!Helpers.isNumeric(value)) {
						message.value = "Field "+item.getFieldName()+" is numeric";
						messages.add(message);
						continue;
					}
					
					double currentValue = Double.parseDouble(value);
					
					for (String patternData : patternDatas) {
						if(patternData.contains("min")) {
							double min = Double.parseDouble(patternData.replace("min:", ""));
							if(currentValue < min) {
								message.value = "Field "+item.getFieldName()+" must exceed " + min;
								messages.add(message);
								continue;
							}
						}
						
						if(patternData.contains("max")) {
							double max = Double.parseDouble(patternData.replace("max:", ""));
							if(currentValue > max) {
								message.value = "Field "+item.getFieldName()+" must not exceed " + max;
								messages.add(message);
								continue;
							}
						}
					}
				}
				
				//phone
				if(Arrays.asList(patternDatas).contains("phone")) {
					String regex = "^(0[3|5|7|8|9])+([0-9]{8})$"; 
					if(!value.matches(regex)) {
						message.value = "Invalid phone number";
						messages.add(message);
						continue;
					}
				}
				
				
				//time
				if(Arrays.asList(patternDatas).contains("time")) {
					String regex = "^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$"; 
					if(!value.matches(regex)) {
						message.value = "Invalid time";
						messages.add(message);
						continue;
					}
				}
				//numeric
				if(Arrays.asList(patternDatas).contains("string")&&!value.isEmpty()) {
					for (String patternData : patternDatas) {
						if(patternData.contains("min")) {
							double min = Double.parseDouble(patternData.replace("min:", ""));
							if(value.length() < min) {
								message.value = "Field "+item.getFieldName()+" must exceed " + min + " character(s)";
								messages.add(message);
								continue;
							}
						}
						
						if(patternData.contains("max")) {
							double max = Double.parseDouble(patternData.replace("max:", ""));
							if(value.length() > max) {
								message.value = "Field "+item.getFieldName()+" must not exceed " + max + " character(s)";
								messages.add(message);
								continue;
							}
						}
					}
					
				}
				
				//email
				if(Arrays.asList(patternDatas).contains("email")&& !value.isEmpty()) {
					String regex = "[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9-]+([.][a-zA-Z]+)+";
					if(!value.matches(regex)) {
						message.value = "Invalid email address";
						messages.add(message);
						continue;
					}
				}
				
				//regex
				if(pattern.contains("regex")) {
					for (String patternData : patternDatas) {
						if(patternData.contains("regex")) {
							String regex = patternData.replace("regex:", "");
							System.out.println(regex);
							System.out.println(value.matches(regex));
							if(!value.matches(regex)) {
								message.value = "Invalid format";
								messages.add(message);
								continue;
							}
						}
					}
					
				}
				
				
				
			}
			
			return messages;
		} catch (Exception e) {
			e.printStackTrace();
			return messages;
		}
	}
	  public static boolean checkDup(String ob,String tbl,String cond, String obCompare,Label lb,String er ) {
          boolean check=true;
          System.out.println("in checkDup");
       try{
           
           String sql = "select "+ob+" from "+tbl+" where "+cond+ob+"='"+obCompare+"'";
           Statement pstmt = MySQLJDBC.connection.createStatement();
           ResultSet rs = pstmt.executeQuery(sql);
           if(rs.next()){
               check=false;
              lb.setText(er);
           }
           pstmt.close(); 
           rs.close();
       }catch(SQLException ex){
           
       }
       return check;
    }
	  
	  public static boolean checkDate(Date date1, Date date2, Label lb, String er) throws ParseException {
	      Calendar now = Calendar.getInstance();
           int year= now.get(Calendar.YEAR);
           int month= (now.get(Calendar.MONTH) + 1);
           int day= + now.get(Calendar.DATE);
           SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
           java.util.Date nowS =  sdf.parse(year+"-"+month+"-"+day); 
	      boolean check=true;
	          if(date1==null||date1.equals("")||date2==null&&date2.equals("")){
	              return false;
	          }else{
	            if(date1.compareTo(date2)>0){
	             check=false;
	             lb.setText(er);
	            }
	          }
	           if(nowS.compareTo(date1)>0&&nowS.compareTo(date2)>0){
	             check=false;
	             lb.setText(er);
	            }
	       return check;
	    }
	  
	  public static boolean checkTime(LocalTime time, LocalTime time2, Label lb, String er){
		     boolean check= true;
		     if(time.compareTo(time2)>=0){
		         check=false;
		         lb.setText(er);
		     }
		     
		     return check;
		 }
	  public static boolean checkTimeUpdate(LocalTime time, Label lb, String er){
		  boolean check= true;
		  int timeBook;
		  	Preferences preference;
			preference = Preferences.userNodeForPackage(SettingController.class);
			timeBook =preference.getInt("timeBook",2);
		     LocalTime now = LocalTime.now();
		     LocalTime timeCheck=LocalTime.of(now.getHour()+timeBook, now.getMinute());
		     
		     if(time.compareTo(timeCheck)<=0){
		         check=false;
		         lb.setText(er);
		     }
		     
		     return check;
	  }
	
}

