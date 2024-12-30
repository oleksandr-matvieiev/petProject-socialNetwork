package org.petproject.socialnetwork.Model;

import jakarta.persistence.*;
import lombok.*;
import org.petproject.socialnetwork.Enums.RoleName;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private RoleName name;
}
