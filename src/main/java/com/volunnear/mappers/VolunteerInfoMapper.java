package com.volunnear.mappers;

import com.volunnear.dtos.response.VolunteerInfoDTO;
import com.volunnear.entitiy.infos.VolunteerInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface VolunteerInfoMapper {
    VolunteerInfoMapper INSTANCE = Mappers.getMapper(VolunteerInfoMapper.class);

    @Mapping(source = "realNameOfUser", target = "realName")
    @Mapping(source = "appUser.email", target = "email")
    VolunteerInfoDTO volunteerInfoToVolunteerInfoDTO(VolunteerInfo volunteerInfo);
}
