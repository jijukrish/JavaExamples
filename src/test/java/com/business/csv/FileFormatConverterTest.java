package com.business.csv;

import org.junit.Test;

import junit.framework.TestCase;

public class FileFormatConverterTest extends TestCase{
	@Test
	public void testIfflatfileHasBlankData(){
		
	}
	@Test
	public void testIfOutPutFileCreated() throws CSVException{
		assertEquals(FileFormatConverter.formatFileWriteAsCSV(),true);
	}

}
