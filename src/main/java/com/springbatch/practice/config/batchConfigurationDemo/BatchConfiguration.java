package com.springbatch.practice.config.batchConfigurationDemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

/**
 * @author Chetan Raj
 * @implNote
 * @since : 03/01/19
 */
@Configuration
public class BatchConfiguration {
    public final Logger logger = LoggerFactory.getLogger(BatchConfiguration.class);

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Tasklet tasklet() {
        return new CountingTasklet();
    }

    @Bean
    public Flow flow1() {
        logger.info("#################################->> Inside BatchConfiguration::flow1 method, Starting flow1");
        return new FlowBuilder<Flow>("flow1")
                .start(stepBuilderFactory.get("step1")
                        .tasklet(tasklet()).build())
                .build();
    }

    @Bean
    public Flow flow2() {
        logger.info("#################################->> Inside BatchConfiguration::flow2 method, Starting flow2");
        return new FlowBuilder<Flow>("flow2")
                .start(stepBuilderFactory.get("step2").tasklet(tasklet()).build())
                .next(stepBuilderFactory.get("step3").tasklet(tasklet()).build())
                .build();
    }

    @Bean
    public Job parallelJobs() {
        logger.info("***********************************->> Inside BatchConfiguration::parallelJobs method, Executing parallelJobs");
        return jobBuilderFactory.get("parallelJobs")
                .start(flow1())
                .split(new SimpleAsyncTaskExecutor()).add(flow2())
                .end()
                .build();
    }

    public static class CountingTasklet implements Tasklet {
        public final Logger logger = LoggerFactory.getLogger(CountingTasklet.class);

        @Override
        public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
            logger.info("{} has been executed on thread {}", chunkContext.getStepContext().getStepName(), Thread.currentThread().getName());
            return RepeatStatus.FINISHED;
        }
    }
}
