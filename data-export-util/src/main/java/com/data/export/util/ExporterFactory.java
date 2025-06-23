/**
 * 
 */
package com.data.export.util;

import com.data.export.enums.ExportFormat;
import com.data.export.services.DataExporter;
import com.data.export.services.ExcelExporter;

/**
 * 
 */
public class ExporterFactory {
	
	public static <T> DataExporter<T> getExporter(ExportFormat format) {
        return switch (format) {
            case EXCEL -> new ExcelExporter<>();
            case PDF -> throw new UnsupportedOperationException("PDF not implemented");
        };
    }

}
