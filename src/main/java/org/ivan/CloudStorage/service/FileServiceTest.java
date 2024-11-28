package org.ivan.CloudStorage.service;

import org.ivan.CloudStorage.model.File;
import org.ivan.CloudStorage.model.User;
import org.ivan.CloudStorage.repository.FileRepository;
import org.junit.jupiter.api.Assertions;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.testng.annotations.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class FileServiceTest {

    @Mock
    private FileRepository fileRepository;

    @InjectMocks
    private FileService fileService;

    @Test
    public void testUploadFile() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        File file = new File();
        file.setId(1L);
        file.setFilename("testFile.txt");
        file.setFilePath("/uploads/testFile.txt");
        file.setSize(1024L);
        file.setUser(user);

        Mockito.when(fileRepository.save(ArgumentMatchers.any(File.class))).thenReturn(file);

        File uploadedFile = fileService.uploadFile(user, "testFile.txt", "/uploads/testFile.txt", 1024L);
        Assertions.assertNotNull(uploadedFile);
        assertEquals("testFile.txt", uploadedFile.getFilename());
    }

    @Test
    public void testSaveFileToDisk() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "testFile.txt", "text/plain", "Hello, World!".getBytes());

        String filePath = fileService.saveFileToDisk(multipartFile);
        Path path = Paths.get(filePath);
        Assertions.assertTrue(Files.exists(path));
        Assertions.assertEquals("Hello, World!", new String(Files.readAllBytes(path)));

        Files.deleteIfExists(path);
    }
}