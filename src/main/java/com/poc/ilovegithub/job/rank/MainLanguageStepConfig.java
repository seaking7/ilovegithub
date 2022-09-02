package com.poc.ilovegithub.job.rank;

import com.poc.ilovegithub.core.domain.UserDetail;
import com.poc.ilovegithub.core.domain.rank.OrgRankResult;
import com.poc.ilovegithub.core.domain.rank.OrgRankTmp;
import com.poc.ilovegithub.core.domain.rank.UserRankResult;
import com.poc.ilovegithub.core.domain.rank.UserRankTmp;
import com.poc.ilovegithub.core.repository.JdbcTemplateRepository;
import com.poc.ilovegithub.core.repository.rank.*;
import com.poc.ilovegithub.core.service.MainLanguageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Collections;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MainLanguageStepConfig {
    private final StepBuilderFactory stepBuilderFactory;

    private final MainLanguageService mainLanguageService;
    private final UserRankTmpRepository userRankTmpRepository;

    private final UserRankResultRepository userRankResultRepository;
    private final OrgRankTmpRepository orgRankTmpRepository;
    private final OrgRankResultRepository orgRankResultRepository;

    private final JdbcTemplateRepository jdbcTemplateRepository;



    @JobScope
    @Bean("userRankMainLanguageStep")
    public Step userRankMainLanguageStep(ItemReader userRankMainLanguageReader,
                                     ItemProcessor userRankMainLanguageProcessor,
                                     ItemWriter userRankMainLanguageWriter) {
        return stepBuilderFactory.get("userRankMainLanguageStep")
                .listener(userRankMainLanguageStepListener())
                .<UserDetail, UserDetail>chunk(100)
                .reader(userRankMainLanguageReader)
                .processor(userRankMainLanguageProcessor)
                .writer(userRankMainLanguageWriter)
                .build();
    }

    public StepExecutionListener userRankMainLanguageStepListener(){
        return new StepExecutionListener() {
            @Override
            public void beforeStep(StepExecution stepExecution) {
                log.info("---before step userRankMainLanguageStep");
            }

            @Override
            public ExitStatus afterStep(StepExecution stepExecution) {
                log.info("---after step userRankMainLanguageStep");
                jdbcTemplateRepository.updateUserRankResult();
                return null;
            }
        };
    }

    @StepScope
    @Bean
    public RepositoryItemReader<UserRankTmp> userRankMainLanguageReader() {
        return new RepositoryItemReaderBuilder<UserRankTmp>()
                .name("userRankMainLanguageReader")
                .repository(userRankTmpRepository)
                .methodName("findAllByFirstLanguageIsNull")
                .pageSize(100)
                .arguments(Arrays.asList())
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<UserRankTmp, UserRankResult> userRankMainLanguageProcessor() {
        return new ItemProcessor<UserRankTmp, UserRankResult>() {
            @Override
            public UserRankResult process(UserRankTmp item) throws Exception {
                return mainLanguageService.userRankResultMaker(item);
            }
        };
    }

    @StepScope
    @Bean
    public ItemWriter<UserRankResult> userRankMainLanguageWriter() {
        return items -> items.forEach(item -> userRankResultRepository.save(item));
    }




    @JobScope
    @Bean("orgRankMainLanguageStep")
    public Step orgRankMainLanguageStep(ItemReader orgRankMainLanguageReader,
                                         ItemProcessor orgRankMainLanguageProcessor,
                                         ItemWriter orgRankMainLanguageWriter) {
        return stepBuilderFactory.get("orgRankMainLanguageStep")
                .listener(orgRankMainLanguageStepListener())
                .<UserDetail, UserDetail>chunk(100)
                .reader(orgRankMainLanguageReader)
                .processor(orgRankMainLanguageProcessor)
                .writer(orgRankMainLanguageWriter)
                .build();
    }

    public StepExecutionListener orgRankMainLanguageStepListener(){
        return new StepExecutionListener() {
            @Override
            public void beforeStep(StepExecution stepExecution) {
                log.info("---before step orgRankMainLanguageStepListener");
            }

            @Override
            public ExitStatus afterStep(StepExecution stepExecution) {
                log.info("---after step orgRankMainLanguageStepListener");
                jdbcTemplateRepository.updateOrgRankResult();
                return null;
            }
        };
    }

    @StepScope
    @Bean
    public RepositoryItemReader<OrgRankTmp> orgRankMainLanguageReader() {
        return new RepositoryItemReaderBuilder<OrgRankTmp>()
                .name("orgRankMainLanguageReader")
                .repository(orgRankTmpRepository)
                .methodName("findBy")
                .pageSize(100)
                .arguments(Arrays.asList())
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<OrgRankTmp, OrgRankResult> orgRankMainLanguageProcessor() {
        return item -> mainLanguageService.orgRankResultMaker(item);
    }

    @StepScope
    @Bean
    public ItemWriter<OrgRankResult> orgRankMainLanguageWriter() {
        return items -> items.forEach(item -> orgRankResultRepository.save(item));
    }




}
