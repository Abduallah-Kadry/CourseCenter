package com.app.coursecenter.service.FileStorageService;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {


    // this should copy the uploaded file through other services and put them into the specified folder
    @Override
    public String saveFile(MultipartFile file, String folder) throws IOException {

        Path uploadPath = Paths.get("uploads/" + folder);

        if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();

        Path filePath = uploadPath.resolve(filename);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return "/uploads/" + folder + "/" + filename;

    }
}
