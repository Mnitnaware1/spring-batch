package com.mastercard.mcbs.pbrc.offline.batch.feeder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.database.JdbcBatchItemWriter;

import com.mastercard.mcbs.pbrc.offline.batch.model.FeederBatchFileObject;
import com.mastercard.mcbs.pbrc.offline.batch.model.PriceGuideBatchFileObject;

import javax.sql.DataSource;

public class FeederBatchItemWriter<T> extends JdbcBatchItemWriter<T> {

    private static final Logger BATCH_WRITER_LOGGER =
            LoggerFactory.getLogger(FeederBatchItemWriter.class);

    /**
     * Build price guide writer, keep this as separate method to handle FEEDER load .
     *
     * @param buildQuery .
     * @param dataSource .
     * @return JdbcBatchItemWriter .
     */
    public JdbcBatchItemWriter<T> buildFeederWriter(String buildQuery, DataSource dataSource) {
        JdbcBatchItemWriter<T> writer = new JdbcBatchItemWriter<>();
        writer.setDataSource(dataSource);
        writer.setSql(buildQuery);
        BATCH_WRITER_LOGGER.info("JdbcBatchItemWriter Feeder SQL: {}", buildQuery);
        writer.setItemSqlParameterSourceProvider(o -> ((FeederBatchFileObject) o));
        writer.afterPropertiesSet();
        return writer;
    }

    /**
     * Build price guide writer, keep this as separate method to handle price guide load .
     *
     * @param buildQuery .
     * @param dataSource .
     * @return buildPriceWriter .
     */
    public JdbcBatchItemWriter<T> buildPriceWriter(String buildQuery, DataSource dataSource) {
        JdbcBatchItemWriter<T> writer = new JdbcBatchItemWriter<>();
        writer.setDataSource(dataSource);
        writer.setSql(buildQuery);
        BATCH_WRITER_LOGGER.info("JdbcBatchItemWriter PRICE_GUIDE SQL: {}", buildQuery);
        writer.setItemSqlParameterSourceProvider(o -> ((PriceGuideBatchFileObject) o));
        writer.afterPropertiesSet();
        return writer;
    }
}
