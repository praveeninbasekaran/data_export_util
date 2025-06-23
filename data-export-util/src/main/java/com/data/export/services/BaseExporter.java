/**
 * 
 */
package com.data.export.services;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.data.export.dto.ExportRequest;
import com.data.export.enums.SortOrder;

/**
 * 
 */
public class BaseExporter<T> implements DataExporter<T> {

	 @Override
	    public void export(ExportRequest<T> request, OutputStream outputStream) throws Exception {
	        throw new UnsupportedOperationException("Export method must be implemented by subclasses");
	    }

	    protected void validateRequest(ExportRequest<T> request) {
	        if (request.getData() == null || request.getData().isEmpty()) {
	            throw new IllegalArgumentException("No data provided");
	        }
	    }
	
	    protected List<T> sortData(List<T> data, String sortBy, SortOrder order) {
	        if (sortBy == null || sortBy.isBlank()) return data;
	        try {
	            Field field = data.get(0).getClass().getDeclaredField(sortBy);
	            field.setAccessible(true);
	            Comparator<T> comparator = Comparator.comparing(o -> {
	                try {
	                    return (Comparable) field.get(o);
	                } catch (Exception e) {
	                    return null;
	                }
	            });
	            if (order == SortOrder.DESC) {
	                comparator = comparator.reversed();
	            }
	            return data.parallelStream().sorted(comparator).collect(Collectors.toList());
	        } catch (NoSuchFieldException e) {
	            return data;
	        }
	    }

	

}
