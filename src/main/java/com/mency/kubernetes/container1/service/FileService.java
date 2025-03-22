package com.mency.kubernetes.container1.service;

import com.mency.kubernetes.container1.model.FileResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class FileService {

    @Value("${pv.directory:/yourname_PV_dir}")
    private String pvDirectory;

    private final RestTemplate restTemplate;

    public FileService() {
        this.restTemplate = new RestTemplate();

        // Ensure PV directory exists
        File directory = new File(pvDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public FileResponse storeFile(String filename, String data) {
        if (filename == null) {
            return FileResponse.error(null, "Invalid JSON input.");
        }

        try {
            String filePath = pvDirectory + File.separator + filename;
            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write(data);
            }
            return FileResponse.success(filename, "Success.");
        } catch (IOException e) {
            return FileResponse.error(filename, "Error while storing the file to the storage.");
        }
    }

    public FileResponse calculateProduct(String filename, String product) {
        if (filename == null) {
            return FileResponse.error(null, "Invalid JSON input.");
        }

        String filePath = pvDirectory + File.separator + filename;
        File file = new File(filePath);

        if (!file.exists()) {
            return FileResponse.error(filename, "File not found.");
        }

        try {
            // Call container2 service to calculate sum
            Map<String, String> request = new HashMap<>();
            request.put("filePath", filePath);
            request.put("product", product);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "http://container2-service:8080/calculate-sum",
                    request,
                    Map.class
            );

            Map<String, Object> responseBody = response.getBody();

            if (responseBody.containsKey("error")) {
                return FileResponse.error(filename, (String) responseBody.get("error"));
            } else {
                Integer sum = ((Number) responseBody.get("sum")).intValue();
                return FileResponse.withSum(filename, sum);
            }
        } catch (Exception e) {
            return FileResponse.error(filename, "Error processing request.");
        }
    }
}