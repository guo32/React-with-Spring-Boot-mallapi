package org.dorastudy.mallapi.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Log4j2
@RequiredArgsConstructor
public class CustomFileUtil {
    @Value("${org.dorastudy.upload.path}")
    private String uploadPath;

    @PostConstruct
    public void init() {
        File tempFolder = new File(uploadPath);
        if (!tempFolder.exists()) {
            tempFolder.mkdir();
        }

        uploadPath = tempFolder.getAbsolutePath();

        log.info("Upload path: {}", uploadPath);
    }

    public List<String> saveFiles(List<MultipartFile> files) throws RuntimeException {
        if (files == null || files.isEmpty()) {
            return List.of(); // 비어있는 리스트 반환
        }
        List<String> uploadNames = new ArrayList<>();
        for (MultipartFile file : files) {
            String saveName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path savePath = Paths.get(uploadPath, saveName);
            try {
                Files.copy(file.getInputStream(), savePath); // 원본 파일 업로드

                String contentType = file.getContentType(); // Mime type
                // 이미지 파일 일 때 섬네일 생성
                if (contentType != null && contentType.startsWith("image")) {
                    Path thumbnailPath = Paths.get(uploadPath, "s_" + saveName);
                    Thumbnails.of(savePath.toFile()).size(200, 200).toFile(thumbnailPath.toFile());
                }
                uploadNames.add(saveName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return uploadNames;
    }
}
