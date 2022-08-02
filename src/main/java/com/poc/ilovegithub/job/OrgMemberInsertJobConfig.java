package com.poc.ilovegithub.job;

import com.poc.ilovegithub.core.domain.UserDetail;
import com.poc.ilovegithub.core.domain.UserStatus;
import com.poc.ilovegithub.core.repository.UserDetailRepository;
import com.poc.ilovegithub.core.service.OrgMemberInsertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Sort;

import java.util.Collections;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class OrgMemberInsertJobConfig {
    private final Environment env;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final OrgMemberInsertService orgMemberInsertService;
    private final UserDetailRepository userDetailRepository;


    @Bean("OrgMemberInsertJob")
    public Job orgMemberInsertJob(Step orgMemberInsertStep) {
        return jobBuilderFactory.get("OrgMemberInsertJob")
                .incrementer(new RunIdIncrementer())
                .start(orgMemberInsertStep)
                .build();
    }

    @JobScope
    @Bean("orgMemberInsertStep")
    public Step orgMemberInsertStep(ItemReader orgMemberInsertReader,
                                     ItemProcessor orgMemberInsertProcessor,
                                     ItemWriter orgMemberInsertWriter) {
        return stepBuilderFactory.get("orgMemberInsertStep")
                .<UserDetail, UserDetail>chunk(Integer.parseInt(env.getProperty("my.fetch-count")))
                .reader(orgMemberInsertReader)
                .processor(orgMemberInsertProcessor)
                .writer(orgMemberInsertWriter)
                .build();
    }

    @StepScope
    @Bean
    public RepositoryItemReader<UserDetail> orgMemberInsertReader() {
        return new RepositoryItemReaderBuilder<UserDetail>()
                .name("orgMemberInsertReader")
                .repository(userDetailRepository)
                .methodName("findByTypeEqualsAndStatusEqualsAndIdGreaterThanAndIdLessThan")
                .pageSize(Integer.parseInt(env.getProperty("my.fetch-count")))
                .arguments("Organization", UserStatus.REPO_INSERTED, Integer.parseInt(env.getProperty("my.start-from-id")), Integer.parseInt(env.getProperty("my.end_to_id")))
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<UserDetail, UserDetail> orgMemberInsertProcessor() {
        return new ItemProcessor<UserDetail, UserDetail>() {
            @Override
            public UserDetail process(UserDetail item) throws Exception {
                UserDetail userDetail = orgMemberInsertService.orgMemberInsert(item);
                return userDetail;
            }
        };
    }

    @StepScope
    @Bean
    public ItemWriter<UserDetail> orgMemberInsertWriter() {
        return items -> {
           items.forEach(item -> userDetailRepository.save(item));
        };
    }
}
