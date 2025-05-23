package com.yedu.api.global.config.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class S3UploadService {

  private final AmazonS3 amazonS3;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  @Value("${cloud.aws.s3.prefix-profile}")
  private String profile;

  public String saveProfileFile(MultipartFile multipartFile) {
    return getString(multipartFile, profile);
  }

  private String getString(MultipartFile multipartFile, String dir) {
    try {
      String originalFilename = multipartFile.getOriginalFilename();
      String fileName = dir + "/" + originalFilename + UUID.randomUUID();

      ObjectMetadata metadata = new ObjectMetadata();
      metadata.setContentLength(multipartFile.getSize());
      metadata.setContentType(multipartFile.getContentType());

      amazonS3.putObject(bucket, fileName, multipartFile.getInputStream(), metadata);
      return amazonS3.getUrl(bucket, fileName).toString();
    } catch (IOException ex) {
      throw new IllegalArgumentException(); // todo : 적절한 예외 처리
    }
  }

  public void deleteProfileImage(String url) {
    try {
      String decodedUrl = URLDecoder.decode(url, "UTF-8");
      String fileName = decodedUrl.substring(decodedUrl.lastIndexOf('/') + 1);
      amazonS3.deleteObject(bucket + "/" + profile, fileName);
    } catch (UnsupportedEncodingException e) {
      throw new IllegalArgumentException(); // todo : 적절한 예외 처리
    }
  }
}
