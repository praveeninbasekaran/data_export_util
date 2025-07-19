package com.example.export.test;

import com.example.export.dto.ExportRequest;
import com.example.export.enums.ExportFormat;
import com.example.export.enums.SortOrder;
import com.example.export.util.ExportUtil;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class TestExport {

    public static class SampleDTO {
        private int id;
        private String name;
        private String description;

        public SampleDTO(int id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
        }

        // Getters & setters (optional, not strictly required for reflection)
    }

    public static void main(String[] args) throws Exception {
        List<SampleDTO> data = new ArrayList<>();

        // Generate data, including one record with >32k characters
        for (int i = 1; i <= 10; i++) {
            String desc;
            if (i == 5) {
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < 40000; j++) sb.append("x"); // 40k characters
                desc = sb.toString();
            } else {
                desc = "Normal description " + i;
            }

            data.add(new SampleDTO(i, "Name-" + i, desc));
        }

        ExportRequest<SampleDTO> request = new ExportRequest<>();
        request.setData(data);
        request.setFormat(ExportFormat.EXCEL);
        request.setSortByField("id");
        request.setSortOrder(SortOrder.ASC);

        try (FileOutputStream fos = new FileOutputStream("sample_output.xlsx")) {
            ExportUtil.export(request, fos);
        }

        System.out.println("Excel export completed: sample_output.xlsx");
    }
}