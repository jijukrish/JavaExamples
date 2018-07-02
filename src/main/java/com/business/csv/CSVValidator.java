package com.business.csv;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class CSVValidator {
	public static boolean isDateFormat(String inDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    dateFormat.setLenient(false);
	    try {
	      dateFormat.parse(inDate.trim());
	    } catch (ParseException pe) {
	      
	      return false;
	    }
	    return true; 
	}
	
	public static boolean isContainsSperator(String input){
		if(input.contains(Constants.COMMA_CHAR)){
			return true;
		}else {
			return false;
		}
	}
}


