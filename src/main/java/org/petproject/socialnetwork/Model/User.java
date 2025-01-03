package org.petproject.socialnetwork.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@ToString(exclude = {"profilePicture", "bio", "followers", "following"})
@EqualsAndHashCode(of = "id")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Size(max = 50, message = "Username must not exceed 50 characters.")
    @NotBlank(message = "Username us mandatory")
    private String username;

    @Column(nullable = false, unique = true)
    @Email(message = "Email should be valid.")
    @NotBlank(message = "Email is mandatory.")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Password is mandatory.")
    private String password;

    @Column
    private String profilePicture;


    @Column
    private String bio;

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Follow> followers = new HashSet<>();

    @OneToMany(mappedBy = "followee", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Follow> following = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
    @Column(nullable = false)
    private boolean emailVerified = false;

    @Column(nullable = true)
    private String verificationCode;


}
