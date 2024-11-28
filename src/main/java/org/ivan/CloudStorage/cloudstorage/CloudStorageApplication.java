package org.ivan.CloudStorage.cloudstorage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "org.ivan.CloudStorage.repository")
public class CloudStorageApplication {
    public static void main(String[] args) {
        SpringApplication.run(CloudStorageApplication.class, args);
    }
}