package org.petproject.socialnetwork.Mapper;

import org.mapstruct.Mapper;
import org.petproject.socialnetwork.DTO.UserDTO;
import org.petproject.socialnetwork.Model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(User user);

    User toEntity(UserDTO userDTO);

}
