package com.poc.ilovegithub.core.service;

import com.poc.ilovegithub.core.domain.rank.*;
import com.poc.ilovegithub.core.repository.GitRepoRepository;
import com.poc.ilovegithub.core.repository.JdbcTemplateRepository;
import com.poc.ilovegithub.core.repository.rank.user.UserRankResultRepository;
import com.poc.ilovegithub.core.repository.rank.user.UserRankTmpRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class MainLanguageService {

    GitRepoRepository gitRepoRepository;
    UserRankTmpRepository userRankTmpRepository;
    UserRankResultRepository userRankResultRepository;
    JdbcTemplateRepository jdbcTemplateRepository;

    // user_rank_tmp 에서 데이터 읽어서 main language 3개 찾아서 user_rank_result 에 insert 수행
    public UserRankResult userRankResultMaker(UserRankTmp userRankTmp) {
        UserRankResult returnUserRank = UserRankResult.from(userRankTmp);
        String login = returnUserRank.getLogin();

        List<String> mainLanguageByLogin = jdbcTemplateRepository.findMainLanguageByLogin(login);
        if(mainLanguageByLogin.size() > 0) returnUserRank.setFirstLanguage(mainLanguageByLogin.get(0));
        if(mainLanguageByLogin.size() > 1) returnUserRank.setSecondLanguage(mainLanguageByLogin.get(1));
        if(mainLanguageByLogin.size() > 2) returnUserRank.setThirdLanguage(mainLanguageByLogin.get(2));

       return returnUserRank;
    }

    public OrgRankResult orgRankResultMaker(OrgRankTmp orgRankTmp) {
        OrgRankResult returnOrgRank = OrgRankResult.from(orgRankTmp);
        String login = returnOrgRank.getLogin();

        List<String> mainLanguageByLogin = jdbcTemplateRepository.findMainLanguageByLogin(login);
        if(mainLanguageByLogin.size() > 0) returnOrgRank.setFirstLanguage(mainLanguageByLogin.get(0));
        if(mainLanguageByLogin.size() > 1) returnOrgRank.setSecondLanguage(mainLanguageByLogin.get(1));
        if(mainLanguageByLogin.size() > 2) returnOrgRank.setThirdLanguage(mainLanguageByLogin.get(2));

        return returnOrgRank;
    }

    public MemberRankResult memberRankResultMaker(MemberRankTmp memberRankTmp) {
        MemberRankResult returnMemberRank = MemberRankResult.from(memberRankTmp);
        String login = returnMemberRank.getLogin();

        List<String> mainLanguageByLogin = jdbcTemplateRepository.findMainLanguageByLogin(login);
        if(mainLanguageByLogin.size() > 0) returnMemberRank.setFirstLanguage(mainLanguageByLogin.get(0));
        if(mainLanguageByLogin.size() > 1) returnMemberRank.setSecondLanguage(mainLanguageByLogin.get(1));
        if(mainLanguageByLogin.size() > 2) returnMemberRank.setThirdLanguage(mainLanguageByLogin.get(2));

        return returnMemberRank;
    }

    public SearchRankResult searchRankResultMaker(SearchRankTmp searchRankTmp) {
        SearchRankResult returnSearchResult = SearchRankResult.from(searchRankTmp);
        String login = returnSearchResult.getLogin();

        List<String> mainLanguageByLogin = jdbcTemplateRepository.findMainLanguageByLogin(login);
        if(mainLanguageByLogin.size() > 0) returnSearchResult.setFirstLanguage(mainLanguageByLogin.get(0));
        if(mainLanguageByLogin.size() > 1) returnSearchResult.setSecondLanguage(mainLanguageByLogin.get(1));
        if(mainLanguageByLogin.size() > 2) returnSearchResult.setThirdLanguage(mainLanguageByLogin.get(2));

        return returnSearchResult;
    }

}
