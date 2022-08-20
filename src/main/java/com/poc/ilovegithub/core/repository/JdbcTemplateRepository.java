package com.poc.ilovegithub.core.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

@Slf4j
public class JdbcTemplateRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //유저 login 로 size 와 star 를 고려하여 상위 3개 Language 를 찾는다
    public List<String> findMainLanguageByLogin(String login) {
        String query = "select language from g_repository gr  " +
                "where login = ? and language is not null " +
                "group by language order by sum(gr.stargazers_count) desc limit 3 ";
        return jdbcTemplate.query(query, userRepoRowMapper(), login);
    }

    // user_rank 를 재집계하기 위해 임시성 테이블 truncate
    public void truncateUserLankTmp(){
        jdbcTemplate.execute("truncate table g_user_rank_tmp");
        jdbcTemplate.execute("truncate table g_user_rank_result");
    }

    // org_rank 를 재집계하기 위해 임시성 테이블 truncate
    public void truncateOrgLankTmp(){
        jdbcTemplate.execute("truncate table g_org_rank_tmp");
        jdbcTemplate.execute("truncate table g_org_rank_result");
    }

    // 성능을 고려하여 50만 개 정도씩 분할해서 tmp 테이블에 insert
    public int insertUserRankTmp(Integer fromId, Integer toId){
        int update = insertGlobalUserRankTmp(fromId, toId);
        int update_korean = insertKoreanUserRankTmp(fromId, toId);
        log.info("insertUserRankTmp {} ~ {}, global:{} korean:{}", fromId, toId, update, update_korean);
        return update + update_korean;
    }

    public int insertOrgRankTmp(Integer fromId, Integer toId) {
        int update = insertGlobalOrgRankTmp(fromId, toId);
        int update_korean = insertKoreanOrgRankTmp(fromId, toId);
        log.info("insertOrgRankTmp {} ~ {}, global:{} korean:{}", fromId, toId, update, update_korean);
        return update + update_korean;
    }

    // g_user_rank_result 에 최종 결과 취합되면, 기존 데이터 지우고 다시 넣어줌
    @Transactional
    public void updateUserRankResult(){
        int delete_from_g_user_rank = jdbcTemplate.update("delete from g_user_rank");
        int update = jdbcTemplate.update("insert into g_user_rank (id, login, followers, following, size, stargazers_count, first_language, second_language, third_language, is_korean, created_at, updated_at)\n" +
                "select id, login, followers, following, size, stargazers_count, first_language, second_language, third_language, is_korean, created_at, updated_at from g_user_rank_result ");
        log.info("update userRank result : delete:{}, insert:{}", delete_from_g_user_rank, update);
    }

    @Transactional
    public void updateOrgRankResult() {
        int delete_from_g_org_rank = jdbcTemplate.update("delete from g_org_rank");
        int update = jdbcTemplate.update("insert into g_org_rank (id, login, people_count, size, stargazers_count, first_language, second_language, third_language, is_korean, created_at, updated_at)\n" +
                "select id, login, people_count, size, stargazers_count, first_language, second_language, third_language, is_korean, created_at, updated_at from g_org_rank_result ");
        log.info("update orgRank result : delete:{}, insert:{}", delete_from_g_org_rank, update);
    }

    @Transactional
    public void updateKoreanInfo() {
        int update = jdbcTemplate.update("update g_user set is_korean = true where is_korean = false\n" +
                    "     and (lower(location) like '%korea%' or lower(location) like '%seoul%'\n" +
                    "or lower(blog) like '%tistory.com%' or lower(blog) like '%naver.com%' \n" +
                    "or lower(email) like '%hanmail.net%' or lower(email) like '%naver.com%') ");
        log.info("updateKoreanInfo : update:{}", update);
    }


    private int insertKoreanUserRankTmp(Integer fromId, Integer toId) {
        String korean_query = "insert into g_user_rank_tmp(id, login, followers, following, size, stargazers_count,\n" +
                "                        created_at, updated_at, is_korean)\n" +
                "select a.id, a.login, a.followers, a.following,\n" +
                "(select sum(gr.size) from g_repository gr where gr.login = a.login ) size,\n" +
                "(select sum(gr2.stargazers_count) star from g_repository gr2 where gr2.login = a.login) stargazers_count,\n" +
                "a.created_at, a.updated_at, a.is_korean\n" +
                "from g_user a\n" +
                "where a.type = 'User'\n" +
                "and a.id >= ?  and a.id < ?\n" +
                "and a.is_korean is true and a.followers > 0\n" +
                "group by a.id, a.login,  a.followers, a.following, a.created_at, a.updated_at, a.is_korean\n" +
                "having stargazers_count > 0";

        return jdbcTemplate.update(korean_query, fromId, toId);
    }

    private int insertGlobalUserRankTmp(Integer fromId, Integer toId) {
        String global_query = "insert into g_user_rank_tmp(id, login, followers, following, size, stargazers_count,\n" +
                "                        created_at, updated_at, is_korean)\n" +
                "select a.id, a.login, a.followers, a.following,\n" +
                "(select sum(gr.size) from g_repository gr where gr.login = a.login ) size,\n" +
                "(select sum(gr2.stargazers_count) star from g_repository gr2 where gr2.login = a.login) stargazers_count,\n" +
                "a.created_at, a.updated_at, a.is_korean\n" +
                "from g_user a\n" +
                "where a.type = 'User'\n" +
                "and a.id >= ?  and a.id < ?\n" +
                "and a.is_korean is false and a.followers > 100\n" +
                "group by a.id, a.login,  a.followers, a.following, a.created_at, a.updated_at, a.is_korean\n" +
                "having stargazers_count > 100";

        return jdbcTemplate.update(global_query, fromId, toId);
    }

    private RowMapper<String> userRepoRowMapper() {
        return (rs, rowNum) -> rs.getString("language");
    }



    private int insertKoreanOrgRankTmp(Integer fromId, Integer toId) {
        String korean_query =  "insert into g_org_rank_tmp(id, login, people_count, size, stargazers_count,\n" +
                "                        created_at, updated_at, is_korean)\n" +
                "select a.id, a.login, \n" +
                " (select count(*) count from g_org_members gom2 where gom2.org_id = a.id) people_count,\n" +
                " (select sum(gr.size) from g_repository gr where gr.login = a.login ) size,\n" +
                " (select sum(gr2.stargazers_count) star from g_repository gr2 where gr2.login = a.login) stargazers_count,\n" +
                "a.created_at, a.updated_at, a.is_korean\n" +
                "from g_user a\n" +
                "where a.type = 'Organization'\n" +
                " and a.id >= ?  and a.id < ? \n" +
                " and a.is_korean is true \n" +
                "group by a.id, a.login, a.type, a.created_at, a.updated_at, a.is_korean\n" +
                "having stargazers_count > 0";

        return jdbcTemplate.update(korean_query, fromId, toId);
    }

    private int insertGlobalOrgRankTmp(Integer fromId, Integer toId) {
        String global_query = "insert into g_org_rank_tmp(id, login, people_count, size, stargazers_count,\n" +
                "                        created_at, updated_at, is_korean)\n" +
                "select a.id, a.login, \n" +
                " (select count(*) count from g_org_members gom2 where gom2.org_id = a.id) people_count,\n" +
                " (select sum(gr.size) from g_repository gr where gr.login = a.login ) size,\n" +
                " (select sum(gr2.stargazers_count) star from g_repository gr2 where gr2.login = a.login) stargazers_count,\n" +
                "a.created_at, a.updated_at, a.is_korean\n" +
                "from g_user a\n" +
                "where a.type = 'Organization'\n" +
                " and a.id >= ?  and a.id < ? \n" +
                " and a.is_korean is false\n" +
                "group by a.id, a.login, a.type, a.created_at, a.updated_at, a.is_korean\n" +
                "having stargazers_count > 10";

        return jdbcTemplate.update(global_query, fromId, toId);
    }


}
