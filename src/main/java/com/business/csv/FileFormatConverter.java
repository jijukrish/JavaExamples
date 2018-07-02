package com.business.csv;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;



/**
 * Class used to convert a flat file into CSV
 * @author jijukrishnan
 *
 */
public class FileFormatConverter {
	
	//private static CsvListWriter _writer;
		private static ArrayList _csvColumns = new ArrayList();
		
		private static CellProcessor[] getProcessors() {
	        
	        int size= _csvColumns.size();
	        final CellProcessor[] processors = new CellProcessor[size];
	        ColumnBean bean = new ColumnBean();
	        for(int i=0;i<size;i++){
	        	bean = (ColumnBean)_csvColumns.get(i);
	        	if(bean.getColType().equalsIgnoreCase("string")){
	        		processors[i] = new NotNull();
	        	}
	        	if(bean.getColType().equalsIgnoreCase("numeric")){
	        		processors[i] = new NotNull(new ParseDouble());
	        	}
	        	if(bean.getColType().equalsIgnoreCase("date")){
	        		
	        		processors[i] = new NotNull();
	        	}
	        }
	        
	        
	        return processors;
	    }
		public static void main(String[] args){
			try{
			formatFileWriteAsCSV();
			}catch(CSVException csv){
			
				System.out.println(csv.getMessage());
			}
		}
		public static boolean formatFileWriteAsCSV()throws CSVException{
			FileInputStream inputStream = null;
			CSVException csvExceptions = null;
			
			Scanner sc = null;
			String path = "datafile2.txt";
			try {
			    inputStream = new FileInputStream(path);
			    sc = new Scanner(inputStream, "UTF-8");
			    
			    String metadatafile = "metadata.csv";
			    final String[] header = getHeaderFromMetadata(metadatafile);
			    if(header == null){
			    	System.out.println("Metadata not read from file .Program can not continue");
			    	System.exit(0);
			    }

			    ICsvMapWriter mapWriter = null;
				try {
					mapWriter = new CsvMapWriter(new FileWriter("outData.csv"), CsvPreference.STANDARD_PREFERENCE);
					
					final CellProcessor[] processors = getProcessors();
					int attribCount = _csvColumns.size();
					String[] values = null;
					
					String[] colHeader = new String[attribCount];
					for(int i=0;i<attribCount;i++){
						colHeader[i] = ((ColumnBean)_csvColumns.get(i)).getColName();
					}
					 
					final Map<String, Object> targetObj = new HashMap<String, Object>();
				 
					mapWriter.writeHeader(header);
					while (sc.hasNextLine()) {
					    String line = sc.nextLine();
					    values = line.split(" ");
					    try{ 
					   for(int i=0;i<values.length;i++){
					   
						   if(((ColumnBean)_csvColumns.get(i)).getColType().equalsIgnoreCase("string")){
							   
							   if(CSVValidator.isContainsSperator(values[i])){
								   values[i]= '"' + values[i]+ '"'; 
							   }
						   }
						   if(((ColumnBean)_csvColumns.get(i)).getColType().equalsIgnoreCase("numeric")){
							   try{
								   
								   float temp = Float.parseFloat(values[i]);
								   
							   }catch(NumberFormatException nf){
								   System.out.println(nf.getMessage());
							   }
						   }
						   if(((ColumnBean)_csvColumns.get(i)).getColType().equalsIgnoreCase("date")){
							  
							   // validate input date value
							   if(CSVValidator.isDateFormat(values[i])){
								   DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
								   DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
								  
								   String inputDateStr=values[i];
								   Date date = inputFormat.parse(inputDateStr);
								   values[i] = outputFormat.format(date);
								   targetObj.put(colHeader[i], values[i]);
							   } else {
								   csvExceptions= new CSVException("date format in file is wrong");
								   throw csvExceptions;
							   }
						   }else {
							   targetObj.put(colHeader[i], values[i]);
						   }
					   }
				
					   mapWriter.write(targetObj, colHeader, processors);
					    }catch(IndexOutOfBoundsException iof){
					    	System.out.println("Looks like value for a field is  not present in file");
					    	System.out.println("Data missing in line " +line);
					    	continue;
					    }

				}		
					
				} catch(IOException io){
					System.out.println("IO exception in the program. Exception is " + io.getMessage());
				}
				catch(ParseException de){
					System.out.println("Exception in parsing date. Exception is " + de.getMessage());
				}
				catch(Exception e){
			    	System.out.println(e.getMessage());
			    	System.exit(0);
			    }
				finally {
					//gracefully closing handles
		            try {
		            	mapWriter.close();
		            } catch (IOException e) {
		                e.printStackTrace();
		            }
				}
				
				// note that Scanner suppresses exceptions
			    if (sc.ioException() != null) {
			    	System.out.println("Exception in reading the flat file");
			        throw sc.ioException();
			    }
			}catch(FileNotFoundException fe){
				System.out.println("File not found exception occured..... " + fe.getMessage() );
				
			}
			catch(IOException fe){
				System.out.println("IO exception occcured ....... " + fe.getMessage());
			}
			
			finally {
				try{
			    if (inputStream != null) {
			        inputStream.close();
			    }
			    if (sc != null) {
			        sc.close();
			    }
				}catch(IOException io){}
			}
			return true;
		}
		
		private static  String[] getHeaderFromMetadata(String filename){
			String[] header =null;
			try {
				FileInputStream inputStream = null;
				
				Scanner sc = null;
			    inputStream = new FileInputStream(filename);
			    sc = new Scanner(inputStream, "UTF-8");
			    		    
			    String[] words;
			    int colCount=0;
			    //Creating beans and storing in arraylist for fields
			    while (sc.hasNextLine()) {
			        String line = sc.nextLine();
			        words = line.split(",");
			        ColumnBean colBean = new ColumnBean(words[0], Integer.parseInt(words[1]), words[2]);
			        _csvColumns.add(colCount++, colBean);
			    }
			    
			    System.out.println("total number of columns defined in metadata file is...... " + colCount );
			    //Generate header string
			    
			    header = new String[colCount];
			    for(int i=0;i<_csvColumns.size();i++){
			    
			    	header[i]=((ColumnBean)_csvColumns.get(i)).getColName();
			    	
			    }
			} catch(IOException io){
				
			}
			
			
			return header;
		}
		
}
