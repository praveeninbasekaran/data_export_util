package com.data.export.services;

import java.io.OutputStream;

import com.data.export.dto.ExportRequest;

public interface DataExporter<T> {
	
	void export(ExportRequest<T> request, OutputStream outputStream) throws Exception;

}
