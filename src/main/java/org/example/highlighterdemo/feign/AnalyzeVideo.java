package org.example.highlighterdemo.feign;

import org.example.highlighterdemo.config.exception.CustomException;
import org.example.highlighterdemo.config.exception.ErrorCode;
import org.example.highlighterdemo.feign.config.FeignConfig;
import org.example.highlighterdemo.feign.dto.VideoAnalyzeDTO;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "analyze-video", url = "${fastapi.url}",
        configuration = FeignConfig.class, fallbackFactory = AnalyzeVideo.VideoFallbackFactory.class)
public interface AnalyzeVideo {
    @PostMapping(value = "/analyze", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    VideoAnalyzeDTO analyzeVideo(@RequestPart("file") MultipartFile file);

    @Component
    class VideoFallbackFactory implements FallbackFactory<FallbackWithFactory> {

        @Override
        public FallbackWithFactory create(Throwable cause) {
            return new FallbackWithFactory();
        }
    }
    class FallbackWithFactory implements AnalyzeVideo {

        @Override
        public VideoAnalyzeDTO analyzeVideo(MultipartFile file) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "FAST API ERROR : failed analyze video");
        }
    }

}
