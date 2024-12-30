package org.petproject.socialnetwork.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.petproject.socialnetwork.DTO.UserDTO;
import org.petproject.socialnetwork.Model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "profile_picture", target = "profile_picture")
    UserDTO toDTO(User user);

    User toEntity(UserDTO userDTO);

}
