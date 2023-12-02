package com.techx7.techstore.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryService {

    String uploadFile(MultipartFile multipartFile, String entityType, String entityName) throws IOException;

    String seedFile(String entityType, String entityName) throws IOException;

}
