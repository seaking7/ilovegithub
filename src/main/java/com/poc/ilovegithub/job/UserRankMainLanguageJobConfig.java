package com.poc.ilovegithub.job;

import com.poc.ilovegithub.core.domain.UserDetail;
import com.poc.ilovegithub.core.domain.UserRank;
import com.poc.ilovegithub.core.domain.UserRankResult;
import com.poc.ilovegithub.core.repository.UserDetailRepository;
import com.poc.ilovegithub.core.repository.UserRankRepository;
import com.poc.ilovegithub.core.repository.UserRankResultRepository;
import com.poc.ilovegithub.core.service.UserRankMainLanguageService;
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

import java.util.Arrays;
import java.util.Collections;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class UserRankMainLanguageJobConfig {
    private final Environment env;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final UserRankMainLanguageService userRankMainLanguageService;
    private final UserRankRepository userRankRepository;
    private final UserRankResultRepository userRankResultRepository;


    @Bean("UserRankMainLanguageJob")
    public Job userRankMainLanguageJob(Step userRankMainLanguageStep) {
        return jobBuilderFactory.get("UserRankMainLanguageJob")
                .incrementer(new RunIdIncrementer())
                .start(userRankMainLanguageStep)
                .build();
    }

    @JobScope
    @Bean("userRankMainLanguageStep")
    public Step userRankMainLanguageStep(ItemReader userRankMainLanguageReader,
                                     ItemProcessor userRankMainLanguageProcessor,
                                     ItemWriter userRankMainLanguageWriter) {
        return stepBuilderFactory.get("userRankMainLanguageStep")
                .<UserDetail, UserDetail>chunk(10)
                .reader(userRankMainLanguageReader)
                .processor(userRankMainLanguageProcessor)
                .writer(userRankMainLanguageWriter)
                .build();
    }

    @StepScope
    @Bean
    public RepositoryItemReader<UserRank> userRankMainLanguageReader() {
        return new RepositoryItemReaderBuilder<UserRank>()
                .name("userRankMainLanguageReader")
                .repository(userRankRepository)
                .methodName("findAllByMainLanguageIsNull")
                .pageSize(10)
                .arguments(Arrays.asList())
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<UserRank, UserRankResult> userRankMainLanguageProcessor() {
        return new ItemProcessor<UserRank, UserRankResult>() {
            @Override
            public UserRankResult process(UserRank item) throws Exception {
                UserRankResult userDetail = userRankMainLanguageService.userRankInsert(item);
                return userDetail;
            }
        };
    }

    @StepScope
    @Bean
    public ItemWriter<UserRankResult> userRankMainLanguageWriter() {
        return items -> {
           items.forEach(item -> userRankResultRepository.save(item));
        };
    }
}
