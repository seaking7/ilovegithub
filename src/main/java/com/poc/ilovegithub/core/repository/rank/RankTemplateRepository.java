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


    public void insertSearchRankTmp(){
        jdbcTemplate.execute("truncate table g_search_count");
        jdbcTemplate.execute("truncate table g_search_rank_tmp");
        jdbcTemplate.execute("truncate table g_search_rank_result");

        int insert_to_search_count = jdbcTemplate.update("insert into g_search_count(login, search_count, created_at)\n" +
                "select c.login, sum(c.cnt) sum, now() from (\n" +
                "select b.login, count(*) cnt from g_search_result a, g_user b\n" +
                "where a.search_id != 0 and a.search_id = b.id group by b.login\n" +
                "union all\n" +
                "select a.search_login, count(*) cnt from g_search_result a\n" +
                "where a.search_login is not null group by a.search_login) c\n" +
                "group by c.login order by sum desc");


        int insert_to_rank_tmp = jdbcTemplate.update("insert into g_search_rank_tmp(id, login, type, followers, following, search_count, size, stargazers_count, created_at, updated_at)\n" +
                "select a.id, a.login, a.type, a.followers, a.following, b.search_count,\n" +
                "       sum(gr.size) size, sum(gr.stargazers_count) stargazers_count, a.created_at, a.updated_at\n" +
                "from g_user a, g_search_count b,  g_repository gr\n" +
                "where b.login = a.login and b.login = gr.login \n" +
                "group by a.id, a.login, a.type, a.followers, a.following,  b.search_count, a.created_at, a.updated_at");


        log.info("insert searchRank tmp result : search_count:{} rank_tmp:{}", insert_to_search_count,  insert_to_rank_tmp);
    }

    // g_search_rank_result 에 최종 결과 취합되면, 기존 데이터 지우고 다시 넣어줌
    @Transactional
    public void updateSearchRank(){
        int delete_from_g_search_rank = jdbcTemplate.update("delete from g_search_rank");
        int insert = jdbcTemplate.update("insert into g_search_rank (id, login, type, followers, following, search_count, size, stargazers_count, first_language, second_language, third_language, created_at, updated_at)\n" +
                "select id, login, type, followers, following, search_count, size, stargazers_count, first_language, second_language, third_language, created_at, updated_at from g_search_rank_result ");
        log.info("update searchRank result : delete:{}, insert:{}", delete_from_g_search_rank, insert);
    }

}
