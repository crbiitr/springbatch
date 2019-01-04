package com.springbatch.practice.listener;

import org.aspectj.lang.annotation.After;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.context.annotation.Configuration;

/**
 * @author Chetan Raj
 * @implNote
 * @since : 04/01/19
 */
public class ChunkListener {
    private static final Logger logger = LoggerFactory.getLogger(ChunkListener.class);

    @BeforeChunk
    public void beforeChunk(ChunkContext context) {
        logger.info(">> Before the chunk <<");
    }

    @AfterChunk
    public void afterChunk(ChunkContext context) {
        logger.info(">> After the chunk <<");
    }
}
