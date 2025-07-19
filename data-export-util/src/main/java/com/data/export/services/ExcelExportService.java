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

    final int EXCEL_CELL_LIMIT = 32000;
    final int BATCH_SIZE = 1000;

    try (SXSSFWorkbook workbook = new SXSSFWorkbook(100)) {
        Sheet sheet = workbook.createSheet("Export");
        int rowIndex = 0;

        // Write header
        Row header = sheet.createRow(rowIndex++);
        for (int i = 0; i < fields.length; i++) {
            header.createCell(i).setCellValue(fields[i].getName());
        }

        for (int batchStart = 0; batchStart < data.size(); batchStart += BATCH_SIZE) {
            int batchEnd = Math.min(batchStart + BATCH_SIZE, data.size());

            for (int i = batchStart; i < batchEnd; i++) {
                T item = data.get(i);
                Row row = sheet.createRow(rowIndex++);
                int currentRowIndex = rowIndex - 1;

                for (int j = 0; j < fields.length; j++) {
                    Object val = fields[j].get(item);
                    String strVal = val != null ? val.toString() : "";

                    if (strVal.length() <= EXCEL_CELL_LIMIT) {
                        row.createCell(j).setCellValue(strVal);
                    } else {
                        // write first chunk in this row
                        String firstChunk = strVal.substring(0, EXCEL_CELL_LIMIT);
                        row.createCell(j).setCellValue(firstChunk);

                        int startIdx = EXCEL_CELL_LIMIT;
                        // write remaining chunks in new rows below
                        while (startIdx < strVal.length()) {
                            Row extraRow = sheet.createRow(rowIndex++);
                            int endIdx = Math.min(startIdx + EXCEL_CELL_LIMIT, strVal.length());
                            String nextChunk = strVal.substring(startIdx, endIdx);
                            extraRow.createCell(j).setCellValue(nextChunk);
                            startIdx = endIdx;
                        }
                    }
                }
            }
        }

        workbook.write(out);
    }

    long end = System.currentTimeMillis();
    System.out.println("Exported " + data.size() + " records in " + (end - start) + " ms");
}