package com.volunnear.services.files;

import com.volunnear.entitiy.GalleryImage;
import com.volunnear.entitiy.activities.Activity;
import com.volunnear.entitiy.infos.OrganisationInfo;
import com.volunnear.entitiy.infos.VolunteerInfo;
import com.volunnear.exceptions.NotFoundException;
import com.volunnear.repositories.GalleryImagesRepository;
import com.volunnear.repositories.infos.ActivitiesRepository;
import com.volunnear.repositories.infos.OrganisationInfoRepository;
import com.volunnear.repositories.infos.VolunteerInfoRepository;
import com.volunnear.services.interfaces.FileStorageService;
import com.volunnear.services.interfaces.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import static com.volunnear.Constants.*;

@Service
@Slf4j
public class FileUploadServiceImpl implements FileUploadService {

    private final VolunteerInfoRepository volunteerInfoRepository;

    private final OrganisationInfoRepository organisationInfoRepository;

    private final ActivitiesRepository activitiesRepository;

    private final FileStorageService fileStorageService;

    private final GalleryImagesRepository galleryImageRepository;

    @Autowired
    public FileUploadServiceImpl(VolunteerInfoRepository volunteerInfoRepository, OrganisationInfoRepository organisationInfoRepository, ActivitiesRepository activitiesRepository, FileStorageService fileStorageService, GalleryImagesRepository galleryImageRepository) {
        this.volunteerInfoRepository = volunteerInfoRepository;
        this.organisationInfoRepository = organisationInfoRepository;
        this.activitiesRepository = activitiesRepository;
        this.fileStorageService = fileStorageService;
        this.galleryImageRepository = galleryImageRepository;
    }
    @Override
    public String uploadVolunteerAvatar(UUID volunteerId, MultipartFile file) {
        String fileUrl = fileStorageService.uploadFile(file, FILE_STORAGE_VOLUNTEER_AVATAR);
        log.info("File url: {}", fileUrl);
        VolunteerInfo volunteerInfo = volunteerInfoRepository.findVolunteerInfoByAppUserId(volunteerId).orElseThrow(() -> new NotFoundException("Volunteer not found with id: " + volunteerId));
        volunteerInfo.setAvatarUrl(fileUrl);
        volunteerInfoRepository.save(volunteerInfo);
        return fileUrl;
    }

    @Override
    public String uploadOrganisationAvatar(UUID organisationId, MultipartFile  file){
        String fileUrl = fileStorageService.uploadFile(file, FILE_STORAGE_ORGANISATION_AVATAR);
        log.info("File url: {}", fileUrl);
        OrganisationInfo organisationInfo = organisationInfoRepository.findOrganisationInfoByAppUserId(organisationId).orElseThrow(() -> new NotFoundException("Organisation not found with id: " + organisationId));
        organisationInfo.setAvatarUrl(fileUrl);
        organisationInfoRepository.save(organisationInfo);
        return fileUrl;
    }

    @Override
    public String uploadActivityCoverImage(UUID activityId, MultipartFile  file) {
        String fileUrl = fileStorageService.uploadFile(file, FILE_STORAGE_ACTIVITY_IMAGE);
        log.info("File url: {}", fileUrl);
        Activity activity = activitiesRepository.findById(activityId).orElseThrow(() -> new NotFoundException("Activity not found with id: " + activityId));
        activity.setCoverImageUrl(fileUrl);
        activitiesRepository.save(activity);
        return fileUrl;
    }

    @Override
    public String uploadActivityGalleryImage(UUID activityId, MultipartFile  file) {
        String fileUrl = fileStorageService.uploadFile(file, FILE_STORAGE_ACTIVITY_IMAGE);
        log.info("File url: {}", fileUrl);
        addGalleryImageToActivity(activityId, fileUrl);
        return fileUrl;
    }

    @Override
    @Async("fileUploadTaskExecutor")
    public CompletableFuture<List<String>> uploadActivityGalleryImages(UUID activityId, List<MultipartFile> files) {
        return CompletableFuture.supplyAsync(() ->
                        files.stream()
                                .map(file -> fileStorageService.uploadFile(file, FILE_STORAGE_ACTIVITY_IMAGE))
                                .toList()
        ).thenApply(fileUrls -> {
            addGalleryImagesToActivity(activityId, fileUrls);
            return fileUrls;
        });
    }

    protected void addGalleryImageToActivity(UUID activityId, String imageUrl) {
        Activity activity = activitiesRepository.findById(activityId)
                .orElseThrow(() -> new NotFoundException("Activity not found with id: " + activityId));

        GalleryImage galleryImage = new GalleryImage();
        galleryImage.setActivity(activity);
        galleryImage.setImageUrl(imageUrl);

        galleryImageRepository.save(galleryImage);
        activity.getGalleryImages().add(galleryImage);
        activitiesRepository.save(activity);
    }


    protected void addGalleryImagesToActivity(UUID activityId, List<String> imageUrls) {
        Activity activity = activitiesRepository.findById(activityId)
                .orElseThrow(() -> new NotFoundException("Activity not found with id: " + activityId));

        for (String imageUrl : imageUrls) {
            GalleryImage galleryImage = new GalleryImage();
            galleryImage.setActivity(activity);
            galleryImage.setImageUrl(imageUrl);
            galleryImageRepository.save(galleryImage);
            activity.getGalleryImages().add(galleryImage);
        }

        activitiesRepository.save(activity);
    }
}
