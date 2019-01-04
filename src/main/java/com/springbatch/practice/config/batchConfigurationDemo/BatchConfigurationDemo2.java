package com.springbatch.practice.config.batchConfigurationDemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Chetan Raj
 * @implNote
 * @since : 03/01/19
 */
@Configuration
public class BatchConfigurationDemo2 {
    public final Logger logger = LoggerFactory.getLogger(BatchConfiguration.class);

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step startStep() {
        return stepBuilderFactory.get("startStep")
                .tasklet(((stepContribution, chunkContext) -> {
                    logger.info("================================>> Inside BatchConfigurationDemo2::startStep method, This is the start tasklet");
                    return RepeatStatus.FINISHED;
                })).build();
    }

    @Bean
    public Step evenStep() {
        return stepBuilderFactory.get("evenStep")
                .tasklet(((stepContribution, chunkContext) -> {
                    logger.info("================================>> Inside BatchConfigurationDemo2::evenStep method, This is the evenStep tasklet");
                    return RepeatStatus.FINISHED;
                })).build();
    }


    @Bean
    public Step oddStep() {
        return stepBuilderFactory.get("oddStep")
                .tasklet(((stepContribution, chunkContext) -> {
                    logger.info("================================>> Inside BatchConfigurationDemo2::oddStep method, This is the oddStep tasklet");
                    return RepeatStatus.FINISHED;
                })).build();
    }

    /*
    @Bean
    public Job deciderDemoJobs() {
        logger.info("***********************************->> Inside BatchConfigurationDemo2::deciderDemoJobs method, Executing deciderDemoJobs");
        return jobBuilderFactory.get("deciderDemoJobs")
                .start(startStep())
                .next(decider())
                .from(decider()).on("ODD").to(oddStep())
                .from(decider()).on("EVEN").to(evenStep())
                .from(oddStep()).on("*").to(decider())
                .from(decider()).on("ODD").to(oddStep())
                .from(decider()).on("EVEN").to(evenStep())
                .end()
                .build();
    }
    */

    @Bean
    public Job deciderDemoJobs() {
        logger.info("***********************************->> Inside BatchConfigurationDemo2::deciderDemoJobs method, Executing deciderDemoJobs");
        return jobBuilderFactory.get("deciderDemoJobs")
                .start(startStep())
                .next(decider())
                .from(decider()).on("ODD").to(oddStep())
                .from(decider()).on("EVEN").to(evenStep())
                .from(oddStep()).on("*").to(decider())
                .end()
                .build();
    }

    @Bean
    public JobExecutionDecider decider() {
        return new OddDecider();
    }

    public static class OddDecider implements JobExecutionDecider {
        private int count = 0;
        @Override
        public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
            count++;
            if((count & 1) == 0) {
                return new FlowExecutionStatus("EVEN");
            } else {
                return new FlowExecutionStatus("ODD");
            }
        }
    }
}
