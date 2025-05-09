package org.example.highlighterdemo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.highlighterdemo.config.exception.CustomException;
import org.example.highlighterdemo.config.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystems;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {
    private final S3Client s3Client;
    @Value("${spring.cloud.aws.region.static}")
    private String regionName;
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
        String filePath = UPLOAD_DIRECTORY + file.getOriginalFilename();
        File savedFile = new File(filePath);
        file.transferTo(savedFile);
        return savedFile;
    }

    public void deleteFile(File file) {
        if(file.exists()) {
            boolean deleted = file.delete();
        }
    }

    public String uploadFile(MultipartFile file) throws IOException, S3Exception{
        if(file == null || file.isEmpty()) {
            return null;
        }
        File savedFile = saveFile(file);
        String s3Key = VIDEO_PATH + savedFile.getName();

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(s3Key)
                .build();

        s3Client.putObject(request, RequestBody.fromFile(savedFile));

        deleteFile(savedFile);
        return s3Key;
    }

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
        }catch (IOException e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "S3 ERROR : failed to get file");
        }
    }
}
