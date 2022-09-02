package com.poc.ilovegithub.core.repository.rank;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@Slf4j
public class RankTemplateRepository {

    private final JdbcTemplate jdbcTemplate;

    public RankTemplateRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    // member_rank 를 재집계하기 위해 임시성 테이블 truncate
    public void truncateMemberLankTmp(){
        jdbcTemplate.execute("truncate table g_member_rank_tmp");
        jdbcTemplate.execute("truncate table g_member_rank_result");
    }

    public void insertMemberRankTmp() {
        int insertCount =  jdbcTemplate.update("insert into g_member_rank_tmp(id, login, company, followers, following, size, stargazers_count,\n" +
                "                        created_at, updated_at)\n" +
                "select a.id, a.login, a.company, a.followers, a.following,\n" +
                "       sum(gr.size) size, sum(gr.stargazers_count) stargazers_count,\n" +
                "a.created_at, a.updated_at\n" +
                "from g_member a, g_repository gr\n" +
                "where a.login = gr.login\n" +
                "group by a.id, a.login, a.company, a.followers, a.following, a.created_at, a.updated_at");
        log.info("insert memberRank result : insert:{}", insertCount);
    }

    // g_member_rank_result 에 최종 결과 취합되면, 기존 데이터 지우고 다시 넣어줌
    @Transactional
    public void updateMemberRank(){
        int delete_from_g_member_rank = jdbcTemplate.update("delete from g_member_rank");
        int update = jdbcTemplate.update("insert into g_member_rank (id, login, company, followers, following, size, stargazers_count, first_language, second_language, third_language, created_at, updated_at)\n" +
                "select id, login, company, followers, following, size, stargazers_count, first_language, second_language, third_language, created_at, updated_at from g_member_rank_result ");
        log.info("update memberRank result : delete:{}, insert:{}", delete_from_g_member_rank, update);
    }


}
