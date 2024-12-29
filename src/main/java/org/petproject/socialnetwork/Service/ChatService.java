package org.petproject.socialnetwork.Service;

import org.petproject.socialnetwork.DTO.ChatDTO;
import org.petproject.socialnetwork.Exceptions.UserNotFound;
import org.petproject.socialnetwork.Model.Chat;
import org.petproject.socialnetwork.Model.User;
import org.petproject.socialnetwork.Repository.ChatRepository;
import org.petproject.socialnetwork.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    public ChatService(ChatRepository chatRepository, UserRepository userRepository) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
    }

    public ChatDTO createChat(String currentUserUsername, String receiverUsername) {
        User userOne = userRepository.findByUsername(currentUserUsername).orElseThrow(UserNotFound::new);
        User userTwo = userRepository.findByUsername(receiverUsername).orElseThrow(UserNotFound::new);

        Chat existingChat = chatRepository.findByUserOneAndUserTwo(userOne, userTwo)
                .or(() -> chatRepository.findByUserOneAndUserTwo(userTwo, userOne))
                .orElse(null);

        if (existingChat != null) {
            return new ChatDTO(existingChat);
        }

        Chat chat = new Chat();
        chat.setUserOne(userOne);
        chat.setUserTwo(userTwo);
        return new ChatDTO(chatRepository.save(chat));
    }

    public List<ChatDTO> getUserChats(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFound::new);
        return chatRepository.findByUserOneOrUserTwo(user, user).stream()
                .map(ChatDTO::new)
                .collect(Collectors.toList());
    }
}
