package com.volunnear.services.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface FileUploadService {

    public String uploadVolunteerAvatar(UUID volunteerId, MultipartFile file);
    public String uploadOrganisationAvatar(UUID organisationId, MultipartFile  file);

    public String uploadActivityCoverImage(UUID activityId, MultipartFile  file);

    public String uploadActivityGalleryImage(UUID activityId, MultipartFile  file);

    CompletableFuture<List<String>> uploadActivityGalleryImages(UUID activityId, List<MultipartFile> files);
}
