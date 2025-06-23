/**
 * 
 */
package com.data.export.services;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.data.export.dto.ExportRequest;

/**
 * 
 */
public class ExcelExporter<T> extends BaseExporter<T> {
	
	private static final Logger logger = LoggerFactory.getLogger(ExcelExporter.class);
	
	@Override
	public void export(ExportRequest<T> request, OutputStream out) throws Exception {
		long start = System.currentTimeMillis();
		validateRequest(request);

		List<T> data = sortData(request.getData(), request.getSortByField(), request.getSortOrder());
		if (data.isEmpty())
			return;

		Class<?> clazz = data.get(0).getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields)
			field.setAccessible(true);

		try (SXSSFWorkbook workbook = new SXSSFWorkbook(100)) {
			Sheet sheet = workbook.createSheet("Export");

			Row header = sheet.createRow(0);
			for (int i = 0; i < fields.length; i++) {
				header.createCell(i).setCellValue(fields[i].getName());
			}

			final int BATCH_SIZE = 1000;
			for (int batchStart = 0; batchStart < data.size(); batchStart += BATCH_SIZE) {
				int batchEnd = Math.min(batchStart + BATCH_SIZE, data.size());
				for (int i = batchStart; i < batchEnd; i++) {
					Row row = sheet.createRow(i + 1);
					T item = data.get(i);
					for (int j = 0; j < fields.length; j++) {
						Object val = fields[j].get(item);
						row.createCell(j).setCellValue(val != null ? val.toString() : "");
					}
				}
			}

			workbook.write(out);
		}
		long end = System.currentTimeMillis();
		logger.info("Exported {} records in {} ms", data.size(), (end - start));
	}
}
