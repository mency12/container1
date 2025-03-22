package com.mency.kubernetes.container1.controller;

import com.mency.kubernetes.container1.model.FileRequest;
import com.mency.kubernetes.container1.model.FileResponse;
import com.mency.kubernetes.container1.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class FileController {

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/store-file")
    public ResponseEntity<FileResponse> storeFile(@RequestBody FileRequest request) {
        FileResponse response = fileService.storeFile(request.getFile(), request.getData());

        if (response.getError() != null) {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/calculate")
    public ResponseEntity<FileResponse> calculate(@RequestBody FileRequest request) {
        FileResponse response = fileService.calculateProduct(request.getFile(), request.getProduct());

        if (response.getError() != null) {
            if (response.getError().equals("File not found.")) {
                return ResponseEntity.status(404).body(response);
            }
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/start")
    public ResponseEntity<Map<String, String>> start(@RequestBody Map<String, String> request) {
        String banner = request.get("banner");
        String ip = request.get("ip");

        System.out.println("Received start request from banner: " + banner + " with IP: " + ip);

        return ResponseEntity.ok(Map.of("message", "Received start request"));
    }
}