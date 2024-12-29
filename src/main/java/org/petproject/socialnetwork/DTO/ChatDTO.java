    package org.petproject.socialnetwork.DTO;

    import lombok.Data;
    import org.petproject.socialnetwork.Model.Chat;

    @Data
    public class ChatDTO {
        private Long id;
        private String userOne;
        private String userTwo;

        public ChatDTO(Chat chat) {
            this.id = chat.getId();
            this.userOne = chat.getUserOne().getUsername();
            this.userTwo = chat.getUserTwo().getUsername();
        }
    }
