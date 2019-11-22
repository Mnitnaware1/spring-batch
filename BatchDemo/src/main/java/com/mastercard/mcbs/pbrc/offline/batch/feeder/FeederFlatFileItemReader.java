package com.mastercard.mcbs.pbrc.offline.batch.feeder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.core.io.FileSystemResource;
import org.springframework.validation.BindException;

import com.mastercard.mcbs.pbrc.offline.batch.model.FeederBatchFileObject;
import com.mastercard.mcbs.pbrc.offline.batch.model.FeederQueryBuilder;
import com.mastercard.mcbs.pbrc.offline.batch.model.PriceGuideBatchFileObject;
import com.mastercard.mcbs.pbrc.offline.batch.service.SequenceService;

public class FeederFlatFileItemReader<T> extends FlatFileItemReader<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeederFlatFileItemReader.class);


    /**
     * buildFlatFileItemReader is a method to generate flatFileItemReader for FEEDER.
     *
     * @param tableMetaDataModel .
     * @param filePath           .
     * @return FlatFIleItemReader .
     */
    public FlatFileItemReader<FeederBatchFileObject> buildFeederFlatFileItemReader(
            FeederQueryBuilder tableMetaDataModel, String filePath) {
        LOGGER.info("Setup FlatFileItemReader");
        FlatFileItemReader<FeederBatchFileObject> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource(filePath));
        reader.setLineMapper(getFeederLineMapper(tableMetaDataModel));
        reader.setStrict(false);
        return reader;
    }

    private DefaultLineMapper<FeederBatchFileObject> getFeederLineMapper(FeederQueryBuilder tableMetaDataModel) {
        return new DefaultLineMapper<FeederBatchFileObject>() {
            {
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setNames(tableMetaDataModel.getFileHeaders());
                        setDelimiter(",");
                    }
                });
                setFieldSetMapper(FeederBatchFileObject::new);
            }
        };
    }

    /**
     * buildFlatFileItemReader is a method to generate flatFileItemReader for PRICE GUIDE.
     *
     * @param tableMetaDataModel .
     * @param filePath           .
     * @return FlatFIleItewmReader  .
     */
    public FlatFileItemReader<PriceGuideBatchFileObject> buildPriceGuideFlatFileItemReader(
            FeederQueryBuilder tableMetaDataModel, String filePath, SequenceService sequenceService) {
        LOGGER.info("Setup FlatFileItemReader");
        FlatFileItemReader<PriceGuideBatchFileObject> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource(filePath));
        reader.setLineMapper(
                new DefaultLineMapper<PriceGuideBatchFileObject>() {
                    {
                        setLineTokenizer(
                                new DelimitedLineTokenizer() {
                                    {
                                        setNames(tableMetaDataModel.getFileHeaders());
                                        setDelimiter(" | ");
                                    }
                                });
                        setFieldSetMapper(new FieldSetMapper<PriceGuideBatchFileObject>() {
                            @Override
                            public PriceGuideBatchFileObject mapFieldSet(FieldSet fieldSet) throws BindException {
                                return new PriceGuideBatchFileObject(fieldSet, tableMetaDataModel.getTableName(), sequenceService);
                            }
                        });
                    }
                });
        reader.setStrict(false);
        reader.setLinesToSkip(1);
        return reader;
    }
}
