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
@Table(name = "g_search_rank_result")
public class SearchRankResult{

    @Id
    private int id;

    @Column(nullable = false)
    private String login;
    private String type;
    private Integer followers;
    private Integer following;
    private Integer searchCount;
    private Integer size;
    private Integer stargazersCount;
    private String firstLanguage;
    private String secondLanguage;
    private String thirdLanguage;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static SearchRankResult from(SearchRankTmp searchRankTmp){
        return SearchRankResult.builder()
                .id(searchRankTmp.getId())
                .login(searchRankTmp.getLogin())
                .type(searchRankTmp.getType())
                .followers(searchRankTmp.getFollowers())
                .following(searchRankTmp.getFollowing())
                .searchCount(searchRankTmp.getSearchCount())
                .size(searchRankTmp.getSize())
                .stargazersCount(searchRankTmp.getStargazersCount())
                .firstLanguage(searchRankTmp.getFirstLanguage())
                .secondLanguage(searchRankTmp.getSecondLanguage())
                .thirdLanguage(searchRankTmp.getThirdLanguage())
                .createdAt(searchRankTmp.getCreatedAt())
                .updatedAt(searchRankTmp.getUpdatedAt())
                .build();
    }

}
