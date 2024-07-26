package com.volunnear.repositories;


import com.volunnear.entitiy.GalleryImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GalleryImagesRepository extends JpaRepository<GalleryImage,UUID> {
}
