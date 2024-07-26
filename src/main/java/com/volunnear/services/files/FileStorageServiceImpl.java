package com.volunnear.services.files;

import com.volunnear.exceptions.files.FileUploadException;
import com.volunnear.services.interfaces.FileStorageService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {
    private final MinioClient minioClient;
    private final String minoBaseUrl;

    public FileStorageServiceImpl(MinioClient minioClient, @Value("${MINIO_API_URL}")String minoBaseUrl) {

        this.minioClient = minioClient;
        this.minoBaseUrl = minoBaseUrl;
    }

    @Override
    public String uploadFile(MultipartFile file, String bucketName) throws FileUploadException {
        try {
            String objectName = System.currentTimeMillis() + "_" + UUID.randomUUID();
            InputStream inputStream = file.getInputStream();

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            return getPublicUrl(bucketName, objectName);
        } catch (Exception e) {
            throw new FileUploadException("Failed to upload file", e);
        }
    }

    private String getPublicUrl(String bucketName, String objectName) {
        return minoBaseUrl + "/" + bucketName + "/" + objectName;
    }
}
