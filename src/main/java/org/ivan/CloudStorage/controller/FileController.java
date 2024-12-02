package org.ivan.CloudStorage.controller;

import org.ivan.CloudStorage.model.File;
import org.ivan.CloudStorage.model.User;
import org.ivan.CloudStorage.service.FileService;
import org.ivan.CloudStorage.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileService fileService;

    @Autowired
    private UserService userService;

    @GetMapping("/files")
    public ResponseEntity<List<File>> getFiles(@RequestHeader("auth-token") String token) {
        logger.info("GET request to /files");
        User user = userService.getUserFromToken(token);
        return ResponseEntity.ok(fileService.getFiles(user));
    }

    @PostMapping("/files")
    public ResponseEntity<File> uploadFile(@RequestHeader("auth-token") String token, @RequestParam("file") MultipartFile file) {
        logger.info("POST request to /files");
        User user = userService.getUserFromToken(token);
        String filename = file.getOriginalFilename();
        String filePath = fileService.saveFileToDisk(file);
        Long size = file.getSize();
        return ResponseEntity.ok(fileService.uploadFile(user, filename, filePath, size));
    }

    @DeleteMapping("/files/{filename}")
    public ResponseEntity<Void> deleteFile(@RequestHeader("auth-token") String token, @PathVariable String filename) {
        logger.info("DELETE request to /files/{}", filename);
        User user = userService.getUserFromToken(token);
        fileService.deleteFile(user, filename);
        return ResponseEntity.noContent().build();
    }
}