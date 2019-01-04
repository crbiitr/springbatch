package com.springbatch.practice.config.ListenerJobConfiguration;

import com.springbatch.practice.listener.ChunkListener;
import com.springbatch.practice.listener.JobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * @author Chetan Raj
 * @implNote
 * @since : 04/01/19
 */
@Configuration
public class ListenerJobConfiguration {
    public final Logger logger = LoggerFactory.getLogger(ListenerJobConfiguration.class);

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    ItemReader<String> reader() {
        return new ListItemReader<>(Arrays.asList("one", "two", "three"));
    }

    @Bean
    public ItemWriter<String> writer() {
        return new ItemWriter<String>() {
            @Override
            public void write(List<? extends String> items) throws Exception {
                for (String item : items) {
                    logger.info("Writing Item :: {}", item);
                }
            }
        };
    }

    @Bean
    public Step listenerStep1() {
        logger.info("================================>> Inside ListenerJobConfiguration::listenerStep1 method, This is the listenerStep1 tasklet");

        return stepBuilderFactory.get("listenerStep1")
                .<String, String>chunk(2)
                .faultTolerant()
                .listener(new ChunkListener())
                .reader(reader())
                .writer(writer())
                .build();
    }

    @Bean
    public Job listenerDemoJobs() {
        logger.info("***********************************->> Inside ListenerJobConfiguration::listenerDemoJobs method, Executing listenerDemoJobs");
        return jobBuilderFactory.get("listenerDemoJobs")
                .start(listenerStep1())
                .listener(new JobListener())
                .build();
    }
}
