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
@Table(name = "G_repository")
public class GitRepo {

    @Id
    private Integer id;

    @Column(nullable = false)
    private String login;

    @Column(nullable = false)
    private String name;

    private Integer userId;

    private Integer size;
    private Integer stargazers_count;

    private String language;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private LocalDateTime pushed_at;
    private LocalDateTime fetched_on;




}
