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
@Table(name = "g_org_rank_tmp")
public class OrgRankTmp {

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

}
