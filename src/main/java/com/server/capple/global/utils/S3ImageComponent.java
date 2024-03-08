package com.server.capple.global.utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.server.capple.global.exception.RestApiException;
import com.server.capple.global.exception.errorCode.S3ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3ImageComponent {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * 이미지 업로드
     * @param multipartFile 업로드 할 이미지 파일
     * @return 업로드된 파일의 접근 URL
     */
    public String uploadImage(MultipartFile multipartFile) {
        String fileName = createFileName(multipartFile.getOriginalFilename());

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        try {
            amazonS3.putObject(bucket, fileName, multipartFile.getInputStream(), metadata);
        } catch (IOException e) {
            throw new RestApiException(S3ErrorCode.FAILED_UPLOAD_IMAGE);
        }

        return amazonS3.getUrl(bucket, fileName).toString();
    }

    /**
     * 이미지 파일명 생성
     * @param originalFileName 파일의 이름
     * @return 작명된 파일 이름
     */
    public String createFileName(String originalFileName) {
        return getOriginalFileName(originalFileName) + UUID.randomUUID().toString().replaceAll("-","") + getFileExtension(originalFileName);
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    private String getOriginalFileName(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    /**
     * 버킷에서 이미지 삭제
     * @param fileUrl
     */
    public void deleteImage(String fileUrl) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileUrl.split("/", 4)[3]));
    }
}
