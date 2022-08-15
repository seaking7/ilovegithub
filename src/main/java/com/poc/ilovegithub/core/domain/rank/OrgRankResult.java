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
@Table(name = "g_org_rank_result")
public class OrgRankResult {

    @Id
    private int id;

    @Column(nullable = false)
    private String login;

    private Integer peopleCount;
    private Integer size;
    private Integer stargazersCount;
    private String firstLanguage;
    private String secondLanguage;
    private String thirdLanguage;
    private Boolean isKorean;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static OrgRankResult from(OrgRankTmp orgRankTmp){
        return OrgRankResult.builder()
                .id(orgRankTmp.getId())
                .login(orgRankTmp.getLogin())
                .peopleCount(orgRankTmp.getPeopleCount())
                .size(orgRankTmp.getSize())
                .stargazersCount(orgRankTmp.getStargazersCount())
                .firstLanguage(orgRankTmp.getFirstLanguage())
                .secondLanguage(orgRankTmp.getSecondLanguage())
                .thirdLanguage(orgRankTmp.getThirdLanguage())
                .isKorean(orgRankTmp.getIsKorean())
                .createdAt(orgRankTmp.getCreatedAt())
                .updatedAt(orgRankTmp.getUpdatedAt())
                .build();
    }

}
