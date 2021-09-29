package utils;

public class DataMapping {
	public String key;
	public String value;
	private static DataMapping dataMapping;
	
	public static DataMapping getInstance(String key, String value) {
		if(dataMapping == null) {
			dataMapping = new DataMapping();
			dataMapping.key = key;
			dataMapping.value = value;
			return dataMapping;
		}
		return dataMapping;
	}
	
	public static DataMapping getInstance(int key, String value) {
		if(dataMapping == null) {
			dataMapping = new DataMapping();
			dataMapping.key = String.valueOf(key);
			dataMapping.value = value;
			return dataMapping;
		}
		return dataMapping;
	}
	
	public String toString() {
		return this.value;
	}
	
	
}
