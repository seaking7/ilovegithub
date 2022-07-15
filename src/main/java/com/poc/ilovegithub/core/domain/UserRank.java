package com.poc.ilovegithub.core.domain;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "G_userRank")
public class UserRank {

    @Id
    private Long id;

    @Column(nullable = false)
    private String login;

    private Integer followers;
    private Integer size;
    private Integer stargazers_count;
    private String main_language;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;

}
