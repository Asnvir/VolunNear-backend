package com.volunnear.mappers;

import com.volunnear.dtos.response.ActivityDTO;
import com.volunnear.entitiy.GalleryImage;
import com.volunnear.entitiy.activities.Activity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ActivityMapper {

    ActivityMapper INSTANCE = Mappers.getMapper(ActivityMapper.class);

    @Mapping(source = "latitude", target = "locationDTO.latitude")
    @Mapping(source = "longitude", target = "locationDTO.longitude")
    @Mapping(source = "galleryImages", target = "galleryImages")
    ActivityDTO activityToActivityDTO(Activity activity);

    default String map(GalleryImage galleryImage) {
        return galleryImage.getImageUrl();
    }
}
