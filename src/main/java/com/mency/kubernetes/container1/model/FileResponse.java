package com.mency.kubernetes.container1.model;

public class FileResponse {
    private String file;
    private String message;
    private String error;
    private Integer sum;

    // Constructors
    public FileResponse() {}

    public static FileResponse success(String filename, String message) {
        FileResponse response = new FileResponse();
        response.setFile(filename);
        response.setMessage(message);
        return response;
    }

    public static FileResponse error(String filename, String errorMessage) {
        FileResponse response = new FileResponse();
        response.setFile(filename);
        response.setError(errorMessage);
        return response;
    }

    public static FileResponse withSum(String filename, Integer sum) {
        FileResponse response = new FileResponse();
        response.setFile(filename);
        response.setSum(sum);
        return response;
    }

    // Getters and Setters
    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Integer getSum() {
        return sum;
    }

    public void setSum(Integer sum) {
        this.sum = sum;
    }
}