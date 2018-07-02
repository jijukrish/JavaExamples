package com.business.csv;
/**
 * All specific exception related to the CSV reading and writing
 * @author jijukrishnan
 *
 */
public class CSVException extends Exception{
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		private String exMessage = Constants.EMPTY_STRING;
		public CSVException() {
			// TODO Auto-generated constructor stub
		}
		public CSVException(String message){
			this.exMessage = message;
		}
		
		@Override
		public String toString() {
		// TODO Auto-generated method stub
		return exMessage;
		}
}
