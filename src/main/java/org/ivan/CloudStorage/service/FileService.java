package org.ivan.CloudStorage.service;

import org.ivan.CloudStorage.exception.FileNotFoundException;
import org.ivan.CloudStorage.model.File;
import org.ivan.CloudStorage.model.User;
import org.ivan.CloudStorage.repository.FileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileService.class);

    @Autowired
    private FileRepository fileRepository;

    public List<File> getFiles(User user) {
        logger.info("Fetching files for user: {}", user.getUsername());
        return fileRepository.findByUser(user);
    }

    @Transactional
    public File uploadFile(User user, String filename, String filePath, Long size) {
        logger.info("Uploading file: {} for user: {}", filename, user.getUsername());
        File file = new File();
        file.setUser(user);
        file.setFilename(filename);
        file.setFilePath(filePath);
        file.setSize(size);
        return fileRepository.save(file);
    }

    @Transactional
    public void deleteFile(User user, String filename) {
        logger.info("Deleting file: {} for user: {}", filename, user.getUsername());
        File file = fileRepository.findByFilenameAndUser(filename, user).orElseThrow(() -> new FileNotFoundException("File not found"));
        fileRepository.delete(file);
    }

    public String saveFileToDisk(MultipartFile file) {
        try {
            Path path = Paths.get("uploads/" + file.getOriginalFilename());
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());
            logger.info("File saved to disk: {}", path);
            return path.toString();
        } catch (IOException e) {
            logger.error("Failed to save file: {}", file.getOriginalFilename(), e);
            throw new RuntimeException("Failed to save file", e);
        }
    }
}