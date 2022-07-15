package com.poc.ilovegithub.job;

import com.poc.ilovegithub.core.domain.GithubUser;
import com.poc.ilovegithub.core.domain.UserDetail;
import com.poc.ilovegithub.core.domain.UserStatus;
import com.poc.ilovegithub.core.repository.UserDetailRepository;
import com.poc.ilovegithub.core.service.GitRepoInsertService;
import com.poc.ilovegithub.core.service.UserRankInsertService;
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
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Sort;

import java.util.Collections;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class UserRankInsertJobConfig {
    private final Environment env;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final GitRepoInsertService gitRepoInsertService;

    private final UserRankInsertService userRankInsertService;
    private final UserDetailRepository userDetailRepository;


    @Bean("UserRankInsertJob")
    public Job gitRepoInsertJob(Step userRankInsertStep) {
        return jobBuilderFactory.get("UserRankInsertJob")
                .incrementer(new RunIdIncrementer())
                .start(userRankInsertStep)
                .build();
    }

    @JobScope
    @Bean("userRankInsertStep")
    public Step userRankInsertStep(ItemReader userRankInsertReader,
                                     ItemProcessor userRankInsertProcessor,
                                     ItemWriter userRankInsertWriter) {
        return stepBuilderFactory.get("userRankInsertStep")
                .<UserDetail, UserDetail>chunk(Integer.parseInt(env.getProperty("my.fetch-count")))
                .reader(userRankInsertReader)
                .processor(userRankInsertProcessor)
                .writer(userRankInsertWriter)
                .build();
    }

    @StepScope
    @Bean
    public RepositoryItemReader<GithubUser> userRankInsertReader() {
        return new RepositoryItemReaderBuilder<GithubUser>()
                .name("userRankInsertReader")
                .repository(userDetailRepository)
                .methodName("findByStatusEquals")
                .pageSize(Integer.parseInt(env.getProperty("my.fetch-count")))
                .arguments(UserStatus.REPO_INSERTED)
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<UserDetail, UserDetail> userRankInsertProcessor(@Value("#{jobParameters['gitToken']}") String gitToken) {
        return new ItemProcessor<UserDetail, UserDetail>() {
            @Override
            public UserDetail process(UserDetail item) throws Exception {
                UserDetail userDetail = userRankInsertService.userRankInsert(item, gitToken);
                return userDetail;
            }
        };
    }

    @StepScope
    @Bean
    public ItemWriter<UserDetail> userRankInsertWriter() {
        return items -> {
           items.forEach(item -> userDetailRepository.save(item));
        };
    }
}
