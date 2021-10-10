package utils;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
					String regex = "^(?:[01]?\\d|2[0-3])(?::[0-5]\\d){1,2}$"; 
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
	
}

