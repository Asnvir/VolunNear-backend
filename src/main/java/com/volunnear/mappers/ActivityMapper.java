package com.volunnear.mappers;

import com.volunnear.dtos.response.ActivityDTO;
import com.volunnear.entitiy.activities.Activity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ActivityMapper {

    ActivityMapper INSTANCE = Mappers.getMapper(ActivityMapper.class);

    @Mapping(source = "latitude", target = "locationDTO.latitude")
    @Mapping(source = "longitude", target = "locationDTO.longitude")
    ActivityDTO activityToActivityDTO(Activity activity);
}
