package com.business.csv;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTest {
		public static void main(String args[]){
			try{
			DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
			DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
			String inputDateStr="2013-06-24";
			Date date = inputFormat.parse(inputDateStr);
			String outputDateStr = outputFormat.format(date);
			System.out.println(outputDateStr);
			}catch(ParseException pe){
				
			}
			
		}
}
