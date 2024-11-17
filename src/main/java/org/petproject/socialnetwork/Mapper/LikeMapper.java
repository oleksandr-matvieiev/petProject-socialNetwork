package org.petproject.socialnetwork.Mapper;

import org.mapstruct.Mapper;
import org.petproject.socialnetwork.DTO.LikeDTO;
import org.petproject.socialnetwork.Model.Likes;

@Mapper(componentModel = "spring")
public interface LikeMapper {
   LikeDTO toDTO(Likes like);
   Likes toEntity(LikeDTO likeDTO);
}
