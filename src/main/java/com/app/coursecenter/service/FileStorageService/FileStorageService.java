package com.app.coursecenter.service.FileStorageService;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {

    public String saveFile(MultipartFile file, String folder) throws IOException;

}
