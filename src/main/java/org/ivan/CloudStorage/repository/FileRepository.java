package org.ivan.CloudStorage.repository;

import org.ivan.CloudStorage.model.File;
import org.ivan.CloudStorage.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findByUser(User user);
    Optional<File> findByFilenameAndUser(String filename, User user);
}