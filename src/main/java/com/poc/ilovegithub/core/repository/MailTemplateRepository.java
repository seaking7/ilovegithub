package com.poc.ilovegithub.core.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Slf4j
public class MailTemplateRepository {

    private final JdbcTemplate jdbcTemplate;

    public MailTemplateRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void insertKoreanUserTarget() {

        int update = jdbcTemplate.update("insert into g_mail_sender(title, content, email, status, mail_type, created_at)\n" +
                "select  b.title, b.content, a.email, 'PREPARE', b.mail_type, now()\n" +
                "from g_user a, g_mail_template b\n" +
                "where a.is_korean is true\n" +
                "and a.email not in (select email from g_mail_result c)\n" +
                "and a.email not in (select email from g_mail_sender d)\n" +
                "and a.email not in (select email from g_mail_reject_user e)\n" +
                "group by a.email, b.title, b.content, b.mail_type");

        log.info("insert korean user target insert:{}", update);

        int update_sender = jdbcTemplate.update("update g_mail_sender  set status = 'SEND' order by id limit 100");
        log.info("change status to SEND update:{}", update_sender);
    }

    // 발송이 완료되면 해당 유저들은 지운다.
    public void deleteMailSenderAfterSend() {

        int update = jdbcTemplate.update("delete from g_mail_sender where email in (select email from g_mail_result)");
        log.info("delete sender target insert:{}", update);
    }

    // 메일 발송시 Exception 이 발생하면 거부리스트에 넣는다.
    public void insertFailUser(String email) {
        int update = jdbcTemplate.update("insert into g_mail_reject_user(email, reject_type) values(?, 'ERROR')", email);
        log.info("insert Fail User insert:{}", update);
    }

}
