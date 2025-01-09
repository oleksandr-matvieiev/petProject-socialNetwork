package org.petproject.socialnetwork.ServiceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.petproject.socialnetwork.Enums.FileCategory;
import org.petproject.socialnetwork.Service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileStorageServiceTest {

    @InjectMocks
    private FileStorageService fileStorageService;

    @Mock
    private MultipartFile multipartFile;

    @Value("${app.file.upload-base-dir}")
    private String baseUploadDir = "test-upload-dir";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveImage_Success() throws IOException {
        String originalFileName = "test-image.png";
        String fileCategoryFolder = "images";

        FileCategory fileCategory = mock(FileCategory.class);
        when(fileCategory.getFolder()).thenReturn(fileCategoryFolder);

        when(multipartFile.getOriginalFilename()).thenReturn(originalFileName);
        when(multipartFile.getBytes()).thenReturn("test-image-content".getBytes());
        when(multipartFile.getContentType()).thenReturn("image/png");

        String result = fileStorageService.saveImage(multipartFile, fileCategory);

        Path expectedPath = Paths.get(baseUploadDir, fileCategoryFolder, originalFileName);
        assertTrue(result.contains("/uploads/" + fileCategoryFolder));
        assertTrue(Files.exists(expectedPath));

        // Clean up after the test
        Files.deleteIfExists(expectedPath);
        Files.deleteIfExists(expectedPath.getParent());
    }

    @Test
    void saveImage_InvalidFileType_ThrowsException() {
        when(multipartFile.getContentType()).thenReturn("text/plain");

        FileCategory fileCategory = mock(FileCategory.class);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                fileStorageService.saveImage(multipartFile, fileCategory)
        );

        assertEquals("Only image files are allowed", exception.getMessage());
        verifyNoInteractions(fileCategory);
    }

    @Test
    void saveImage_CreatesDirectoryIfNotExists() throws IOException {
        String originalFileName = "test-image.png";
        String fileCategoryFolder = "new-folder";

        FileCategory fileCategory = mock(FileCategory.class);
        when(fileCategory.getFolder()).thenReturn(fileCategoryFolder);

        when(multipartFile.getOriginalFilename()).thenReturn(originalFileName);
        when(multipartFile.getBytes()).thenReturn("test-image-content".getBytes());
        when(multipartFile.getContentType()).thenReturn("image/png");

        String result = fileStorageService.saveImage(multipartFile, fileCategory);

        Path expectedDirectoryPath = Paths.get(baseUploadDir, fileCategoryFolder);
        assertTrue(Files.exists(expectedDirectoryPath));

        // Clean up after the test
        Files.deleteIfExists(Paths.get(expectedDirectoryPath.toString(), originalFileName));
        Files.deleteIfExists(expectedDirectoryPath);
    }
}
