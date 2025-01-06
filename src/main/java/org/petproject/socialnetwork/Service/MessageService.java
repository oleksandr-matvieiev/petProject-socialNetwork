package org.petproject.socialnetwork.Service;

import org.petproject.socialnetwork.DTO.MessageDTO;
import org.petproject.socialnetwork.Enums.NotificationType;
import org.petproject.socialnetwork.Exceptions.MessageNotFound;
import org.petproject.socialnetwork.Exceptions.UserNotFound;
import org.petproject.socialnetwork.Mapper.MessageMapper;
import org.petproject.socialnetwork.Model.Chat;
import org.petproject.socialnetwork.Model.Message;
import org.petproject.socialnetwork.Model.User;
import org.petproject.socialnetwork.Repository.ChatRepository;
import org.petproject.socialnetwork.Repository.MessageRepository;
import org.petproject.socialnetwork.Repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {
    private final NotificationService notificationService;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final MessageMapper messageMapper;
    private final ChatRepository chatRepository;

    public MessageService(NotificationService notificationService, MessageRepository messageRepository, UserRepository userRepository, MessageMapper messageMapper, ChatRepository chatRepository) {
        this.notificationService = notificationService;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.messageMapper = messageMapper;
        this.chatRepository = chatRepository;
    }

    public MessageDTO sendMessage(String senderUsername, String receiverUsername, String content) {
        User sender = userRepository.findByUsername(senderUsername).orElseThrow(UserNotFound::new);
        User receiver = userRepository.findByUsername(receiverUsername).orElseThrow(UserNotFound::new);

        Chat chat = chatRepository.findByUserOneAndUserTwo(sender, receiver)
                .or(() -> chatRepository.findByUserOneAndUserTwo(receiver, sender))
                .orElse(null);

        if (chat == null) {
            chat = new Chat();
            chat.setUserOne(sender);
            chat.setUserTwo(receiver);
            chat = chatRepository.save(chat);
        }

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        message.setCreatedAt(LocalDateTime.now());
        message.setRead(false);
        message.setChat(chat);

        notificationService.createNotification(receiver, sender, NotificationType.MESSAGE, "You have a new message from: " + senderUsername + "!");
        return messageMapper.toDTO(messageRepository.save(message));
    }


    public List<MessageDTO> getConversation(String senderUsername, String receiverUsername) {
        User sender = userRepository.findByUsername(senderUsername).orElseThrow(UserNotFound::new);
        User receiver = userRepository.findByUsername(receiverUsername).orElseThrow(UserNotFound::new);

        List<Message> messages = messageRepository.findByParticipants(sender, receiver);

        return messages.stream()
                .sorted(Comparator.comparing(Message::getCreatedAt))
                .map(messageMapper::toDTO)
                .collect(Collectors.toList());
    }


    public List<MessageDTO> getInbox(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFound::new);
        return messageRepository.findByReceiverAndIsReadFalse(user).stream()
                .map(messageMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void markAsRead(Long messageId) {
        Message message = messageRepository.findById(messageId).orElseThrow(MessageNotFound::new);
        message.setRead(true);
        messageRepository.save(message);
    }

}
