package org.example.highlighterdemo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.highlighterdemo.config.exception.CustomException;
import org.example.highlighterdemo.config.exception.ErrorCode;
import org.example.highlighterdemo.feign.AnalyzeVideo;
import org.example.highlighterdemo.feign.dto.VideoAnalyzeDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {
    private final S3Client s3Client;
    private final AnalyzeVideo analyzeVideo;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    private static final String VIDEO_PATH = "video/";

    private static final String UPLOAD_DIRECTORY = System.getProperty("user.dir")
            + FileSystems.getDefault().getSeparator()
            + "uploads"
            + FileSystems.getDefault().getSeparator();

    private File saveFile(MultipartFile file) throws IOException {
        File dir = new File(UPLOAD_DIRECTORY);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (!created) {
                throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "failed to create directory");
            }
        }
        File savedFile = new File(UPLOAD_DIRECTORY + file.getOriginalFilename());
        file.transferTo(savedFile);
        return savedFile;
    }

    public void deleteFile(File file) {
        if (file.exists()) {
            boolean deleted = file.delete();
        }
    }

    public String uploadFile(MultipartFile file) throws IOException, S3Exception {
        if (file == null || file.isEmpty()) {
            return null;
        }
        File savedFile = saveFile(file);

        if (!analyzeVideo.analyzeVideo(file).is_lol_video()) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "Only League of Legends play videos can be uploaded.");
        }

        String s3Key = VIDEO_PATH + savedFile.getName();

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(s3Key)
                .build();

        s3Client.putObject(request, RequestBody.fromFile(savedFile));

        deleteFile(savedFile);
        return s3Key;
    }

//    public String analyzeAndUploadVideo(MultipartFile file) throws IOException {
//        File savedFile = saveFile(file);
//
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//
//
//        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//        body.add("file", new FileSystemResource(savedFile));
//
//        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
//
//        ResponseEntity<String> response = restTemplate.postForEntity(fastApiUrl+"analyze", request, String.class);
//
//        if ("true".equals(response.getBody())) {
//            return uploadFile(savedFile);
//        } else {
//            deleteFile(savedFile);
//            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "Only League of Legends play videos can be uploaded.");
//        }
//    }

    public void getVideos(OutputStream outputStream, String s3Key) {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucket)
                .key(s3Key)
                .build();

        try (ResponseInputStream<?> s3Object = s3Client.getObject(request)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = s3Object.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
        } catch (IOException e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "S3 ERROR : failed to get file");
        }
    }
}
