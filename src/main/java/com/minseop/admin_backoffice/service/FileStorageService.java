package com.minseop.admin_backoffice.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private Path uploadPath;

    @PostConstruct
    public void init() {
        this.uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                log.info("파일 저장 디렉토리 생성됨: {}", uploadPath);
            }
        } catch (IOException e) {
            throw new RuntimeException("업로드 디렉토리 생성 실패", e);
        }
    }

    public String store(MultipartFile file) {
        try {
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path targetLocation = uploadPath.resolve(filename);
            file.transferTo(targetLocation.toFile());
            log.info("파일 저장 완료: {}", targetLocation);
            return filename;
        } catch (IOException e) {
            log.error("파일 저장 중 오류 발생", e);
            throw new RuntimeException("파일 저장 실패", e);
        }
    }
}

