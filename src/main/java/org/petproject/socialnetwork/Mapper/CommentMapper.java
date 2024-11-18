package org.petproject.socialnetwork.Mapper;

import org.mapstruct.Mapper;
import org.petproject.socialnetwork.DTO.CommentDTO;
import org.petproject.socialnetwork.Model.Comment;

@Mapper(componentModel = "spring")

public interface CommentMapper {
    CommentDTO toDTO(Comment comment);

    Comment toEntity(CommentDTO commentDTO);

}
