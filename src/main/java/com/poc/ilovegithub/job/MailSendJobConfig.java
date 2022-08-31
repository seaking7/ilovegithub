package com.poc.ilovegithub.job;

import com.poc.ilovegithub.core.domain.MailResult;
import com.poc.ilovegithub.core.domain.MailSender;
import com.poc.ilovegithub.core.domain.UserDetail;
import com.poc.ilovegithub.core.repository.JdbcTemplateRepository;
import com.poc.ilovegithub.core.repository.MailResultRepository;
import com.poc.ilovegithub.core.repository.MailSenderRepository;
import com.poc.ilovegithub.core.repository.MailTemplateRepository;
import com.poc.ilovegithub.core.service.MailSendService;
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
import org.springframework.data.domain.Sort;

import java.util.Collections;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MailSendJobConfig {

    private final static int MAIL_SEND_BATCH_SIZE = 5;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final MailSendService mailSendService;
    private final MailSenderRepository mailSenderRepository;
    private final MailResultRepository mailResultRepository;

    private final MailTemplateRepository mailTemplateRepository;

    @Bean("MailSendJob")
    public Job mailSendJob(Step mailSendStep) {
        return jobBuilderFactory.get("MailSendJob")
                .incrementer(new RunIdIncrementer())
                .start(mailSendStep)
                .build();
    }

    @JobScope
    @Bean("mailSendStep")
    public Step mailSendStep(ItemReader mailSendReader,
                                     ItemProcessor mailSendProcessor,
                                     ItemWriter mailSendWriter) {
        return stepBuilderFactory.get("mailSendStep")
                .listener(mailSendStepListener())
                .<UserDetail, UserDetail>chunk(MAIL_SEND_BATCH_SIZE)
                .reader(mailSendReader)
                .processor(mailSendProcessor)
                .writer(mailSendWriter)
                .build();
    }


    public StepExecutionListener mailSendStepListener(){
        return new StepExecutionListener() {
            @Override
            public void beforeStep(StepExecution stepExecution) {
                log.info("===mailSendStep START");
                mailTemplateRepository.insertKoreanUserTarget();

            }

            @Override
            public ExitStatus afterStep(StepExecution stepExecution) {
                mailTemplateRepository.deleteMailSenderAfterSend();
                log.info("==== mailSendStep END");
                return null;
            }
        };
    }

    @StepScope
    @Bean
    public RepositoryItemReader<MailSender> mailSendReader() {

        return new RepositoryItemReaderBuilder<MailSender>()
                .name("mailSendReader")
                .repository(mailSenderRepository)
                .methodName("findByStatusEquals")
                .pageSize(MAIL_SEND_BATCH_SIZE)
                .arguments("SEND")
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<MailSender, MailResult> mailSendProcessor() {

        return sender -> mailSendService.emailSend(sender);

    }

    @StepScope
    @Bean
    public ItemWriter<MailResult> mailSendWriter() {
        return items -> items.forEach(mailResultRepository::save);
    }
}
