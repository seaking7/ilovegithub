package com.poc.ilovegithub.job.rank;

import com.poc.ilovegithub.core.domain.UserDetail;
import com.poc.ilovegithub.core.domain.UserStatus;
import com.poc.ilovegithub.core.domain.rank.*;
import com.poc.ilovegithub.core.repository.rank.member.MemberRankResultRepository;
import com.poc.ilovegithub.core.repository.rank.member.MemberRankTmpRepository;
import com.poc.ilovegithub.core.repository.rank.MemberRepository;
import com.poc.ilovegithub.core.repository.rank.RankTemplateRepository;
import com.poc.ilovegithub.core.repository.rank.search.SearchRankResultRepository;
import com.poc.ilovegithub.core.repository.rank.search.SearchRankTmpRepository;
import com.poc.ilovegithub.core.service.MainLanguageService;
import com.poc.ilovegithub.core.service.MemberRankService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
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

import java.util.Arrays;
import java.util.Collections;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MemberRankJobConfig {

    private final Environment env;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final MemberRepository memberRepository;
    private final RankTemplateRepository rankTemplateRepository;
    private final MemberRankTmpRepository memberRankTmpRepository;
    private final MemberRankResultRepository memberRankResultRepository;
    private final SearchRankTmpRepository searchRankTmpRepository;
    private final SearchRankResultRepository searchRankResultRepository;
    private final MemberRankService memberRankService;
    private final MainLanguageService mainLanguageService;

    @Bean("MemberRankJob")
    public Job memberRankJob(Step memberRepoInsertStep,
                             Step memberRankUpdateStep,
                             Step searchRankUpdateStep) {
        return jobBuilderFactory.get("MemberRankJob")
                .incrementer(new RunIdIncrementer())
                .start(memberRepoInsertStep)
                .next(memberRankUpdateStep)
                .next(searchRankUpdateStep)
                .build();
    }

    @JobScope
    @Bean("memberRepoInsertStep")
    public Step memberRepoInsertStep(ItemReader memberRepoInsertReader,
                                     ItemProcessor memberRepoInsertProcessor,
                                     ItemWriter memberRepoInsertWriter) {
        return stepBuilderFactory.get("memberRepoInsertStep")
                .listener(memberRepoInsertStepListener())
                .<UserDetail, UserDetail>chunk(Integer.parseInt(env.getProperty("my.fetch-count")))
                .reader(memberRepoInsertReader)
                .processor(memberRepoInsertProcessor)
                .writer(memberRepoInsertWriter)
                .build();
    }


    public StepExecutionListener memberRepoInsertStepListener(){
        return new StepExecutionListener() {
            @Override
            public void beforeStep(StepExecution stepExecution) {
                log.info("===memberRankStep START");

            }

            @Override
            public ExitStatus afterStep(StepExecution stepExecution) {
                log.info("==== memberRankStep END");
                return null;
            }
        };
    }

    @StepScope
    @Bean
    public RepositoryItemReader<Member> memberRepoInsertReader() {

        return new RepositoryItemReaderBuilder<Member>()
                .name("memberRepoInsertReader")
                .repository(memberRepository)
                .methodName("findByStatusEquals")
                .pageSize(Integer.parseInt(env.getProperty("my.fetch-count")))
                .arguments(UserStatus.INIT)
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<Member, Integer> memberRepoInsertProcessor() {

        return member -> memberRankService.memberRepoInsert(member);

    }

    @StepScope
    @Bean
    public ItemWriter<Integer> memberRepoInsertWriter() {
        return items -> log.info("chunk complete memberRepoInsert");
    }





    @JobScope
    @Bean("memberRankUpdateStep")
    public Step memberRankMainLanguageStep(ItemReader memberRankUpdateReader,
                                           ItemProcessor memberRankUpdateProcessor,
                                           ItemWriter memberRankUpdateWriter) {
        return stepBuilderFactory.get("memberRankUpdateStep")
                .listener(memberRankUpdateStepListener())
                .<UserDetail, UserDetail>chunk(Integer.parseInt(env.getProperty("my.fetch-count")))
                .reader(memberRankUpdateReader)
                .processor(memberRankUpdateProcessor)
                .writer(memberRankUpdateWriter)
                .build();
    }

    public StepExecutionListener memberRankUpdateStepListener(){
        return new StepExecutionListener() {
            @Override
            public void beforeStep(StepExecution stepExecution) {
                log.info("===memberRankUpdateStep START, memberRankStep truncate start");
                rankTemplateRepository.truncateMemberLankTmp();
                log.info("===memberRankUpdateStep ING, insert member_rank_tmp start");
                rankTemplateRepository.insertMemberRankTmp();
            }

            @Override
            public ExitStatus afterStep(StepExecution stepExecution) {
                log.info("---after step memberRankUpdateStep");
                rankTemplateRepository.updateMemberRank();
                return null;
            }
        };
    }

    @StepScope
    @Bean
    public RepositoryItemReader<MemberRankTmp> memberRankUpdateReader() {
        return new RepositoryItemReaderBuilder<MemberRankTmp>()
                .name("memberRankUpdateReader")
                .repository(memberRankTmpRepository)
                .methodName("findBy")
                .pageSize(Integer.parseInt(env.getProperty("my.fetch-count")))
                .arguments(Arrays.asList())
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<MemberRankTmp, MemberRankResult> memberRankUpdateProcessor() {
        return item -> mainLanguageService.memberRankResultMaker(item);
    }

    @StepScope
    @Bean
    public ItemWriter<MemberRankResult> memberRankUpdateWriter() {
        return items -> items.forEach(item -> memberRankResultRepository.save(item));
    }



    @JobScope
    @Bean("searchRankUpdateStep")
    public Step searchRankUpdateStep(ItemReader searchRankUpdateReader,
                                           ItemProcessor searchRankUpdateProcessor,
                                           ItemWriter searchRankUpdateWriter) {
        return stepBuilderFactory.get("searchRankUpdateStep")
                .listener(searchRankUpdateStepListener())
                .<UserDetail, UserDetail>chunk(Integer.parseInt(env.getProperty("my.fetch-count")))
                .reader(searchRankUpdateReader)
                .processor(searchRankUpdateProcessor)
                .writer(searchRankUpdateWriter)
                .build();
    }

    public StepExecutionListener searchRankUpdateStepListener(){
        return new StepExecutionListener() {
            @Override
            public void beforeStep(StepExecution stepExecution) {
                log.info("===searchRankUpdateStep START, insertSearchRankTmp start");
                rankTemplateRepository.insertSearchRankTmp();

            }

            @Override
            public ExitStatus afterStep(StepExecution stepExecution) {
                rankTemplateRepository.updateSearchRank();
                log.info("---after step searchRankUpdateStep END");
                return null;
            }
        };
    }

    @StepScope
    @Bean
    public RepositoryItemReader<SearchRankTmp> searchRankUpdateReader() {
        return new RepositoryItemReaderBuilder<SearchRankTmp>()
                .name("searchRankUpdateReader")
                .repository(searchRankTmpRepository)
                .methodName("findBy")
                .pageSize(Integer.parseInt(env.getProperty("my.fetch-count")))
                .arguments(Arrays.asList())
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<SearchRankTmp, SearchRankResult> searchRankUpdateProcessor() {
        return item -> mainLanguageService.searchRankResultMaker(item);
    }

    @StepScope
    @Bean
    public ItemWriter<SearchRankResult> searchRankUpdateWriter() {
        return items -> items.forEach(item -> searchRankResultRepository.save(item));
    }

}
