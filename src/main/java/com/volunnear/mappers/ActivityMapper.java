package com.volunnear.mappers;

import com.volunnear.dtos.response.ActivityDTO;
import com.volunnear.entitiy.activities.Activity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ActivityMapper {

    ActivityMapper INSTANCE = Mappers.getMapper(ActivityMapper.class);

    ActivityDTO activityToActivityDTO(Activity activity);
}
