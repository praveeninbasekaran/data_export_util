package com.data.export.util;

import java.io.OutputStream;

import com.data.export.dto.ExportRequest;
import com.data.export.services.DataExporter;

public class ExportUtil {

	  public static <T> void export(ExportRequest<T> request, OutputStream outputStream) throws Exception {
	        DataExporter<T> exporter = ExporterFactory.getExporter(request.getFormat());
	        exporter.export(request, outputStream);
	    }
}
