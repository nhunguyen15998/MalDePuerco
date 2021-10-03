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
			DataMapping item = new DataMapping();
			item.key = String.valueOf(key);
			item.value = value;
			return item;
		}
		return dataMapping;
	}
	
	public String toString() {
		return this.value;
	}
	
	
}
