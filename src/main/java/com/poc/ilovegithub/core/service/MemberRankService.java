package com.poc.ilovegithub.core.service;

import com.poc.ilovegithub.core.domain.UserDetail;
import com.poc.ilovegithub.core.domain.rank.Member;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class MemberRankService {

    GitRepoInsertService gitRepoInsertService;

    public Integer memberRepoInsert(Member member) throws InterruptedException {

        UserDetail userDetail = new UserDetail();
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        mapper.map(member, userDetail);

        if(userDetail.getType().equals("Organization")) {
            userDetail = gitRepoInsertService.gitRepoInsert(userDetail, "orgs");
        } else {
            userDetail = gitRepoInsertService.gitRepoInsert(userDetail,"users");
        }

        return userDetail.getId();
    }


}
