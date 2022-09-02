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
@Table(name = "g_member_rank_result")
public class MemberRankResult {

    @Id
    private int id;

    @Column(nullable = false)
    private String login;

    private String company;
    private Integer followers;
    private Integer following;
    private Integer size;
    private Integer stargazersCount;
    private String firstLanguage;
    private String secondLanguage;
    private String thirdLanguage;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static MemberRankResult from(MemberRankTmp memberRankTmp){
        return MemberRankResult.builder()
                .id(memberRankTmp.getId())
                .login(memberRankTmp.getLogin())
                .company(memberRankTmp.getCompany())
                .followers(memberRankTmp.getFollowers())
                .following(memberRankTmp.getFollowing())
                .size(memberRankTmp.getSize())
                .stargazersCount(memberRankTmp.getStargazersCount())
                .firstLanguage(memberRankTmp.getFirstLanguage())
                .secondLanguage(memberRankTmp.getSecondLanguage())
                .thirdLanguage(memberRankTmp.getThirdLanguage())
                .createdAt(memberRankTmp.getCreatedAt())
                .updatedAt(memberRankTmp.getUpdatedAt())
                .build();
    }

}
