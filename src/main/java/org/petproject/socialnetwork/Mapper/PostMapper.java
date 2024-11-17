package org.petproject.socialnetwork.Mapper;

import org.mapstruct.Mapper;
import org.petproject.socialnetwork.DTO.PostDTO;
import org.petproject.socialnetwork.Model.Post;

@Mapper(componentModel = "spring")
public interface PostMapper {
    PostDTO toDTO(Post post);

    Post toEntity(PostDTO postDTO);

}