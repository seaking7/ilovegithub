package com.poc.ilovegithub.core.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JdbcTemplateRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<String> findMainLanguageById(int id) {
        String query = "select language from g_repository gr  " +
                "where user_id = ? and language is not null " +
                "group by language order by sum(gr.size * gr.stargazers_count) desc limit 3 ";
        List<String> result = jdbcTemplate.query(query, userRepoRowMapper(), id);
        return result;
    }
//
//    public List<UserRepoInfo> findUserRepoById(int id) {
//        String query = "select id, login, name, size, stargazers_count , language, created_at , updated_at , pushed_at " +
//                "from g_repository gr \n" +
//                "where login in (select login from g_user where id = ?) \n" +
//                "order by stargazers_count desc";
//
//        List<UserRepoInfo> result = jdbcTemplate.query(query, userRepoRowMapper(), id);
//        return result;
//    }
//
//    public void deleteByContentId(String contentId){
//        jdbcTemplate.update("delete from executeLog where content_id = ?", contentId);
//    }
//
//
    private RowMapper<String> userRepoRowMapper() {
        return (rs, rowNum) -> {
            String returnValue = rs.getString("language");
            return returnValue;
        };
    }
//
//    private RowMapper<UserDetailInfo> userDetailRowMapper() {
//        return (rs, rowNum) -> {
//            UserDetailInfo userDetailInfo = new UserDetailInfo();
//            userDetailInfo.setId(rs.getInt("id"));
//            userDetailInfo.setLogin(rs.getString("login"));
//            userDetailInfo.setStatus(rs.getString("status"));
//            userDetailInfo.setName(rs.getString("name"));
//            userDetailInfo.setType(rs.getString("type"));
//            userDetailInfo.setBlog(rs.getString("blog"));
//            userDetailInfo.setCompany(rs.getString("company"));
//            userDetailInfo.setLocation(rs.getString("location"));
//            userDetailInfo.setEmail(rs.getString("email"));
//            userDetailInfo.setBio(rs.getString("bio"));
//            userDetailInfo.setPublic_repos(rs.getInt("public_repos"));
//            userDetailInfo.setFollowers(rs.getInt("followers"));
//            userDetailInfo.setFollowing(rs.getInt("following"));
//            userDetailInfo.setStar(rs.getInt("star"));
//            userDetailInfo.setUpdatedAt(rs.getTimestamp("updated_at") == null ? null : rs.getTimestamp("updated_at").toLocalDateTime());
//            return userDetailInfo;
//        };
//    }

}
