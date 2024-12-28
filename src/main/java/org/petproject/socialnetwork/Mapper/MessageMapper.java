package org.petproject.socialnetwork.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.petproject.socialnetwork.DTO.MessageDTO;
import org.petproject.socialnetwork.Model.Message;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    @Mapping(source = "sender.username", target = "senderUsername")
    @Mapping(source = "receiver.username", target = "receiverUsername")
    MessageDTO toDTO(Message message);

    @Mapping(target = "sender", ignore = true)
    @Mapping(target = "receiver", ignore = true)
    Message toEntity(MessageDTO messageDTO);

}

