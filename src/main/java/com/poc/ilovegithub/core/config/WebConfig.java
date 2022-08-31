package com.poc.ilovegithub.core.config;

import com.poc.ilovegithub.core.repository.JdbcTemplateRepository;
import com.poc.ilovegithub.core.repository.MailTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@RequiredArgsConstructor
@Configuration
public class WebConfig {

    private final DataSource dataSource;

    @Bean
    public JdbcTemplateRepository getJdbcTemplateRepository(){
        return new JdbcTemplateRepository(dataSource);
    }

    @Bean
    public MailTemplateRepository getMailTemplateRepository(){
        return new MailTemplateRepository(dataSource);
    }

}
