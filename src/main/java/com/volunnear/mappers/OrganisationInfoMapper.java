package com.volunnear.mappers;

import com.volunnear.dtos.response.OrganisationInfoDTO;
import com.volunnear.entitiy.infos.OrganisationInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrganisationInfoMapper {

    OrganisationInfoMapper INSTANCE = Mappers.getMapper(OrganisationInfoMapper.class);

    @Mapping(source = "appUser.email", target = "email")
    @Mapping(source = "appUser.username", target = "userName")
    OrganisationInfoDTO organisationInfoToOrganisationInfoDTO(OrganisationInfo organisationInfo);
}
