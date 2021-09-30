package utils;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class ValidationDataMapping {
	// fieldname, field, error, value, pattern
	private String fieldName;
	private String field;
	private String error;
	private String pattern;
	
	public ValidationDataMapping(String fieldName, String field, String error, String pattern) {
		this.setFieldName(fieldName);
		this.setField(field);
		this.setError(error);
		this.setPattern(pattern);
	}

	//gets & sets
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

}