package com.poc.ilovegithub.core.domain;

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
@Table(name = "G_user")
public class UserDetail {

    @Id
    private Integer id;

    @Column(nullable = false)
    private String login;

    @Column(nullable = false)
    private String type;
    private String name;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    private String company;
    private String blog;
    private String location;
    private String email;
    private String bio;
    private Integer public_repos;
    private Integer public_gists;
    private Integer followers;
    private Integer following;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;



}
