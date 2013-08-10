package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config{
	
	private static Properties properties;
	
	public static void init() throws IOException{
		File file = new File("../logic.properties");
		if(file.exists()){
			properties = new Properties();
			properties.load(new FileInputStream(file));
		}
	}
	
	public static String getString(String key){
		return properties.getProperty(key).toString();
	}
	
	public static float getFloat(String key){
		return Float.parseFloat(getString(key));
	}
	
	public static double getDouble(String key){
		return Double.parseDouble(getString(key));
	}
	
	public static int getInt(String key){
		return Integer.parseInt(getString(key));
	}
	
	public static boolean getBoolean(String key){
		return getString(key).equalsIgnoreCase("true");
	}
}
