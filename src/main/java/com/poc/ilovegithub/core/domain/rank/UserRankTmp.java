package com.poc.ilovegithub.core.domain.rank;

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
@Table(name = "g_user_rank_tmp")
public class UserRankTmp {

    @Id
    private int id;

    @Column(nullable = false)
    private String login;

    private Integer followers;
    private Integer following;
    private Integer size;
    private Integer stargazersCount;
    private String mainLanguage;
    private Boolean isKorean;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
