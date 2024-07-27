package com.volunnear.controllers;

import com.volunnear.Routes;
import com.volunnear.services.interfaces.FileUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
public class FileUploadController {
    public final FileUploadService fileUploadService;

    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping(value =Routes.UPLOAD_VOLUNTEER_AVATAR,  consumes = { "multipart/form-data" })
    public ResponseEntity<String> uploadVolunteerAvatar(@PathVariable UUID volunteerId,
                                                        @Parameter(description = "File to upload", required = true) MultipartFile file) {
        return ResponseEntity.ok(fileUploadService.uploadVolunteerAvatar(volunteerId, file));
    }
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping(value =Routes.UPLOAD_ORGANISATION_AVATAR, consumes = { "multipart/form-data" })
    public ResponseEntity<String> uploadOrganisationAvatar(@PathVariable(name = "orgId") UUID organisationId,
                                                           @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(fileUploadService.uploadOrganisationAvatar(organisationId, file));
    }
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping(value =Routes.UPLOAD_ACTIVITY_COVER_IMAGE, consumes = { "multipart/form-data" })
    public ResponseEntity<String> uploadActivityCoverImage(@PathVariable UUID activityId,
                                                           @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(fileUploadService.uploadActivityCoverImage(activityId, file));
    }
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping(value =Routes.UPLOAD_ACTIVITY_GALLERY_IMAGE, consumes = { "multipart/form-data" })
    public ResponseEntity<String> uploadActivityGalleryImage(@PathVariable UUID activityId,
                                                             @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(fileUploadService.uploadActivityGalleryImage(activityId, file));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping(value = Routes.UPLOAD_ACTIVITY_GALLERY_IMAGES, consumes = { "multipart/form-data" })
    @Operation(summary = "Upload multiple gallery images for an activity")
    public ResponseEntity<List<String>> uploadActivityGalleryImages(@PathVariable UUID activityId,
                                                              @RequestParam("files") List<MultipartFile> files) {
        return fileUploadService.uploadActivityGalleryImages(activityId, files)
                .thenApply(ResponseEntity::ok)
                .join();
    }
}
