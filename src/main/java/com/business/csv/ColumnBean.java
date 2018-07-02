package com.business.csv;

public class ColumnBean {
	String colName;
	int colSize;
	String colType;
	ColumnBean() { }
	ColumnBean(String col,int size,String colTyp){
		this.colName = col;
		this.colSize = size;
		this.colType =colTyp;
	}
	public String getColName() {
		return colName;
	}
	public int getColSize() {
		return colSize;
	}
	public String getColType() {
		return colType;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return colName + " : " + colType + " : " + colSize;
	}
}
