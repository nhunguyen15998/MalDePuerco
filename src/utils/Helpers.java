package utils;

import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Helpers {
	
	//format money
	public static DecimalFormat formatNumber(String pattern) {
		if(pattern == null) {
			pattern = "###,###.##";
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
	
	//numeric
	public static boolean isNumeric(String str) { 
		try {  
			Double.parseDouble(str);  
			return true;
		} catch(NumberFormatException e) {  
			return false;  
		}  
	}
	
	//random
	public static String randomString(int len){
		String AB = "0123456789";
		SecureRandom rnd = new SecureRandom();
	    StringBuilder sb = new StringBuilder(len);
	    StringBuilder prefix = new StringBuilder(len);
	    for(int i = 0; i < len; i++) {
		   sb.append(AB.charAt(rnd.nextInt(AB.length())));
	    }
	    return prefix.append(sb).toString();
	}
	
	//local datetime
	public static void convertSystemDateTime(String itemDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	    try {
			Date date = sdf.parse(itemDate);
			sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
			System.out.println(sdf.format(date));    
			sdf.setTimeZone(TimeZone.getDefault());
			System.out.println("test: "+sdf.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}  
	}
	
	
}
