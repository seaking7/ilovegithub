package com.poc.ilovegithub.job;

import com.poc.ilovegithub.core.domain.GithubUser;
import com.poc.ilovegithub.core.domain.UserDetail;
import com.poc.ilovegithub.core.domain.UserStatus;
import com.poc.ilovegithub.core.repository.UserDetailRepository;
import com.poc.ilovegithub.core.service.UserDetailService;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class UserDetailUpdateJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final UserDetailService userDetailService;
    private final UserDetailRepository userDetailRepository;

    @Bean("UserDetailUpdateJob")
    public Job userDetailUpdateJob(Step userDetailUpdateStep) {
        return jobBuilderFactory.get("UserDetailUpdateJob")
                .incrementer(new RunIdIncrementer())
                .start(userDetailUpdateStep)
                .build();
    }

    @JobScope
    @Bean("userDetailUpdateStep")
    public Step userDetailUpdateStep(ItemReader userDetailReader,
                                     ItemProcessor userDetailProcessor,
                                     ItemWriter userDetailWriter) {
        return stepBuilderFactory.get("githubUserInsertStep")
                .<UserDetail, UserDetail>chunk(5)
                .reader(userDetailReader)
                .processor(userDetailProcessor)
                .writer(userDetailWriter)
                .build();
    }

    @StepScope
    @Bean
    public RepositoryItemReader<GithubUser> userDetailReader() {
        return new RepositoryItemReaderBuilder<GithubUser>()
                .name("userDetailReader")
                .repository(userDetailRepository)
                .methodName("findByStatusEquals")
                .pageSize(5)
                .arguments(UserStatus.INIT)
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<UserDetail, UserDetail> userDetailProcessor(@Value("#{jobParameters['gitToken']}") String gitToken) {
        return new ItemProcessor<UserDetail, UserDetail>() {
            @Override
            public UserDetail process(UserDetail item) throws Exception {
                UserDetail userDetail = userDetailService.getUserDetail(item, gitToken);
                return userDetail;
            }
        };
    }

    @StepScope
    @Bean
    public ItemWriter<UserDetail> userDetailWriter() {
        return items -> {
           items.forEach(item -> userDetailRepository.save(item));
        };
    }
}
