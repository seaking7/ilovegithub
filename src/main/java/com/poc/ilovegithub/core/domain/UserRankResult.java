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
@Table(name = "G_userRankResult")
public class UserRankResult {

    @Id
    private int id;

    @Column(nullable = false)
    private String login;

    private Integer followers;
    private Integer following;
    private Integer size;
    private Integer stargazersCount;
    private String mainLanguage;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserRankResult from(UserRank userRank){
        return UserRankResult.builder()
                .id(userRank.getId())
                .login(userRank.getLogin())
                .followers(userRank.getFollowers())
                .following(userRank.getFollowing())
                .size(userRank.getSize())
                .stargazersCount(userRank.getStargazersCount())
                .mainLanguage(userRank.getMainLanguage())
                .createdAt(userRank.getCreatedAt())
                .updatedAt(userRank.getUpdatedAt())
                .build();
    }

}
