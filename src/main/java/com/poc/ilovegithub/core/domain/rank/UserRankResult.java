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
@Table(name = "g_user_rank_result")
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
    private Boolean isKorean;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserRankResult from(UserRankTmp userRankTmp){
        return UserRankResult.builder()
                .id(userRankTmp.getId())
                .login(userRankTmp.getLogin())
                .followers(userRankTmp.getFollowers())
                .following(userRankTmp.getFollowing())
                .size(userRankTmp.getSize())
                .stargazersCount(userRankTmp.getStargazersCount())
                .mainLanguage(userRankTmp.getMainLanguage())
                .isKorean(userRankTmp.getIsKorean())
                .createdAt(userRankTmp.getCreatedAt())
                .updatedAt(userRankTmp.getUpdatedAt())
                .build();
    }

}
