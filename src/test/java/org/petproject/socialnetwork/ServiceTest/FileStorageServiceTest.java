package org.petproject.socialnetwork.ServiceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.petproject.socialnetwork.Enums.FileCategory;
import org.petproject.socialnetwork.Service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(properties = {
        "app.file.upload-base-dir=uploads/"
})
class FileStorageServiceTest {

    @InjectMocks
    private FileStorageService fileStorageService;

    @Mock
    private MultipartFile multipartFile;

    @Value("${app.file.upload-base-dir}")
    private String baseUploadDir;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ReflectionTestUtils.setField(fileStorageService, "baseUploadDir", "uploads/");
    }


    @Test
    void saveImage_Success() throws IOException {
        String originalFileName = "test-image.png";
        FileCategory fileCategory = FileCategory.TEST_IMAGES; // Ensure this is a real FileCategory value

        // Mock the behavior of the multipartFile
        when(multipartFile.getOriginalFilename()).thenReturn(originalFileName);
        when(multipartFile.getBytes()).thenReturn("test-image-content".getBytes());
        when(multipartFile.getContentType()).thenReturn("image/png");

        // Call the method to test
        String result = fileStorageService.saveImage(multipartFile, fileCategory);

        // Define the expected path and check
        Path expectedPath = Paths.get(baseUploadDir, FileCategory.TEST_IMAGES.getFolder());
        assertTrue(result.contains("/uploads/" + fileCategory.getFolder())); // Verify the URL path
        assertTrue(Files.exists(expectedPath)); // Verify the file was actually saved

        deleteDirectory(Paths.get(baseUploadDir, FileCategory.TEST_IMAGES.getFolder()));

    }

    @Test
    void saveImage_InvalidFileType_ThrowsException() {
        when(multipartFile.getContentType()).thenReturn("text/plain");

        FileCategory fileCategory = mock(FileCategory.class); // Mock fileCategory for this test

        // Verify exception is thrown when file type is invalid
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                fileStorageService.saveImage(multipartFile, fileCategory)
        );

        assertEquals("Only image files are allowed", exception.getMessage());
        verifyNoInteractions(fileCategory); // Ensure no further interactions with the file category mock
    }

    @Test
    void saveImage_CreatesDirectoryIfNotExists() throws IOException {
        String originalFileName = "test-image.png";

        // Mock the multipartFile behavior
        when(multipartFile.getOriginalFilename()).thenReturn(originalFileName);
        when(multipartFile.getBytes()).thenReturn("test-image-content".getBytes());
        when(multipartFile.getContentType()).thenReturn("image/png");

        // Ensure directory is created before saving the file
        Path expectedDirectoryPath = Paths.get(baseUploadDir, FileCategory.TEST_IMAGES.getFolder());
        if (!Files.exists(expectedDirectoryPath)) {
            Files.createDirectories(expectedDirectoryPath); // Create directory if it doesn't exist
        }

        String result = fileStorageService.saveImage(multipartFile, FileCategory.TEST_IMAGES);
        assertTrue(Files.exists(expectedDirectoryPath));

        // Clean up after the test
        deleteDirectory(Paths.get(baseUploadDir, FileCategory.TEST_IMAGES.getFolder()));
    }

    private static void deleteDirectory(Path path) throws IOException {
        if (Files.isDirectory(path)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
                for (Path entry : stream) {
                    deleteDirectory(entry);
                }
            }
        }
        Files.delete(path);
    }
}
