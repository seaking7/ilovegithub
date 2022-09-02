package com.poc.ilovegithub.core.domain.rank;

import com.poc.ilovegithub.core.domain.UserStatus;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "g_member")
public class Member {

    @Id
    private Integer id;

    @Column(nullable = false)
    private String login;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    private String name;

    private String role;

    @Column(nullable = false)
    private String type;
    private String blog;
    private String company;

    private String location;
    private String email;
    private String bio;
    private Integer publicRepos;
    private Integer followers;
    private Integer following;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime joinedAt;
    private LocalDateTime lastLoginAt;

}
