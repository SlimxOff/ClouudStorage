package org.ivan.CloudStorage.service;

import org.ivan.CloudStorage.exception.FileNotFoundException;
import org.ivan.CloudStorage.model.File;
import org.ivan.CloudStorage.model.User;
import org.ivan.CloudStorage.repository.FileRepository;
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

    @Autowired
    private FileRepository fileRepository;

    public List<File> getFiles(User user) {
        return fileRepository.findByUser(user);
    }

    @Transactional
    public File uploadFile(User user, String filename, String filePath, Long size) {
        File file = new File();
        file.setUser(user);
        file.setFilename(filename);
        file.setFilePath(filePath);
        file.setSize(size);
        return fileRepository.save(file);
    }

    @Transactional
    public void deleteFile(User user, String filename) {
        File file = fileRepository.findByFilenameAndUser(filename, user).orElseThrow(() -> new FileNotFoundException("File not found"));
        fileRepository.delete(file);
    }

    public String saveFileToDisk(MultipartFile file) {
        try {
            Path path = Paths.get("uploads/" + file.getOriginalFilename());
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());
            return path.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file", e);
        }
    }
}