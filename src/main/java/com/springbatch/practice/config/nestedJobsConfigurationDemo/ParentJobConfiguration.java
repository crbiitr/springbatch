package com.springbatch.practice.config.nestedJobsConfigurationDemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.step.builder.JobStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.batch.core.repository.JobRepository;

/**
 * @author Chetan Raj
 * @implNote This 'spring.batch.job.names=parentJob' property is very important. Use only in case of nested job case.
 * @since : 03/01/19
 */
@Configuration
public class ParentJobConfiguration {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private Job childJob;
    @Autowired
    private JobLauncher jobLauncher;

    public final static Logger logger = LoggerFactory.getLogger(ChildJobConfiguration.class);

    @Bean
    public Step childParentStep1() {
        return stepBuilderFactory.get("childParentStep1")
                .tasklet(((stepContribution, chunkContext) -> {
                    logger.info("================================>> Inside ParentJobConfiguration::childParentStep1 method, This is the childParentStep1 tasklet");
                    return RepeatStatus.FINISHED;
                })).build();
    }

    /**
     * Note here i am getting error because of Spring 2.0 with batch 4.0. Here error is in '.repository(jobRegistry)' line.
     *
     * Link: https://stackoverflow.com/questions/52010399/caused-by-java-lang-illegalargumentexception-unable-to-deserialize-the-executi
     */
/*    @Bean
    public Job parentJob(JobRepository jobRegistry, PlatformTransactionManager transactionManager) {
        logger.info("***********************************->> Inside ParentJobConfiguration::childJob method, Executing parentJob");
        Step childJobStep = new JobStepBuilder(new StepBuilder("childJobStep"))
                .job(childJob)
                .launcher(jobLauncher)
                .repository(jobRegistry)
                .transactionManager(transactionManager)
                .build();

        return jobBuilderFactory.get("parentJob")
                .start(childParentStep1())
                .next(childJobStep)
                .build();
    }*/

}
