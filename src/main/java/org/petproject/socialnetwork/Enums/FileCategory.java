package org.petproject.socialnetwork.Enums;

import lombok.Getter;

@Getter
public enum FileCategory {
    PROFILE_IMAGE("profile/Photos/"),
    POST_IMAGE("posts/Photos/");
    private final String folder;

    FileCategory(String folder) {

        this.folder = folder;
    }

}
