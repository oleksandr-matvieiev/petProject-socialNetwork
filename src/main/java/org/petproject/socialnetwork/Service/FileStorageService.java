package org.petproject.socialnetwork.Service;

import org.petproject.socialnetwork.Enums.FileCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileStorageService {
    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);
    @Value("${app.file.upload-base-dir}")
    private String baseUploadDir;

    public String saveImage(MultipartFile file, FileCategory fileCategory) throws IOException {
        if (!Objects.requireNonNull(file.getContentType()).startsWith("image/")) {
            logger.error("Invalid file type: {}. Only images allowed.", file.getContentType());
            throw new IllegalArgumentException("Only image files are allowed");
        }
        String folder = fileCategory.getFolder();
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        System.out.println("Base Upload Dir: " + baseUploadDir);
        System.out.println("Folder: " + folder);

        Path uploadPath = Paths.get(baseUploadDir, folder);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        Path filePath = uploadPath.resolve(fileName);
        Files.write(filePath, file.getBytes());
        logger.info("Image {} uploaded successfully to {}", fileName, filePath.toAbsolutePath());
        return "/uploads/"+folder + fileName;
    }
}
