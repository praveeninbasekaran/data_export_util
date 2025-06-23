/**
 * 
 */
package com.data.export.dto;

import java.util.List;

import org.apache.poi.ss.formula.functions.T;

import com.data.export.enums.ExportFormat;
import com.data.export.enums.SortOrder;

/**
 * 
 */
public class ExportRequest<T> {
	
	private List<T> data;
    private ExportFormat format;
    private String fileName;
    private String sortByField;
    private SortOrder sortOrder;
	public List<T> getData() {
		return data;
	}
	public void setData(List<T> data) {
		this.data = data;
	}
	public ExportFormat getFormat() {
		return format;
	}
	public void setFormat(ExportFormat format) {
		this.format = format;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getSortByField() {
		return sortByField;
	}
	public void setSortByField(String sortByField) {
		this.sortByField = sortByField;
	}
	public SortOrder getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(SortOrder sortOrder) {
		this.sortOrder = sortOrder;
	}
	@Override
	public String toString() {
		return "ExportRequest [data=" + data + ", format=" + format + ", fileName=" + fileName + ", sortByField="
				+ sortByField + ", sortOrder=" + sortOrder + "]";
	}	

    
}
