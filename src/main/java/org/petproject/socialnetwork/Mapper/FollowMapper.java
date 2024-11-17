package org.petproject.socialnetwork.Mapper;

import org.mapstruct.Mapper;
import org.petproject.socialnetwork.DTO.FollowDTO;
import org.petproject.socialnetwork.Model.Follow;

@Mapper(componentModel = "spring")
public interface FollowMapper {
    FollowDTO toDTO(Follow follow);

    Follow toEntity(FollowDTO followDTO);

}
