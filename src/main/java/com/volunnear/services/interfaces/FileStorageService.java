package com.volunnear.services.interfaces;


import com.volunnear.exceptions.files.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    public String uploadFile(MultipartFile file, String bucketName) throws FileUploadException;
}
