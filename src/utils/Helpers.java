package utils;

import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Screen;

public class Helpers {
	
	//format money
	public static DecimalFormat formatNumber(String pattern) {
		if(pattern == null) {
			pattern = "###,###.###";
		}
		DecimalFormat decimalFormat = new DecimalFormat(pattern);
		return decimalFormat;	
	}
	
	//format date
	public static DateTimeFormatter formatDate(String pattern) {
		if(pattern == null) {
			pattern = "yyyy-MM-dd";
		}
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(pattern);
		return dateFormat;	
	}
	
	//format time
	public static String formatTime(String time) {
		 
		    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		    Date date = null;
		    try {
		        date = sdf.parse(time);
		    } catch (ParseException e) {
		        e.printStackTrace();
		    }
		    String formattedTime = sdf.format(date);
			return formattedTime;

	}
	//numeric
	public static boolean isNumeric(String str) { 
		try {  
			Double.parseDouble(str);  
			return true;
		} catch(NumberFormatException e) {  
			return false;  
		}  
	}
	
	public static String randomString(int len){
		String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		SecureRandom rnd = new SecureRandom();
	    StringBuilder sb = new StringBuilder(len);
	    for(int i = 0; i < len; i++)
	       sb.append(AB.charAt(rnd.nextInt(AB.length())));
	    return sb.toString();
	}
	
	  public static String randomCode(String ob){
	        int max = 10000000;
	        int min = 0;
	        int random = 0;
	        int range = (max-min)+1;
	        random =(int)(Math.random()*range)+min;
	        String c = String.valueOf(random);
	        String code = ob+c;
	        return code;
	  }
	
	  public static void  status(String type) {
			if(type.equals("success")) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Done");
			alert.setHeaderText("Save success");
			alert.showAndWait();
			}
			if(type.equals("error")) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Please select a row");
				alert.showAndWait();
			}
		}
	
	
}
