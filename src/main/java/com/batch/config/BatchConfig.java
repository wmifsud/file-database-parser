package com.batch.config;

import com.batch.pojo.PersonExport;
import com.batch.repository.PersonRepository;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import java.util.Map;

/**
 * @author waylon on 05/05/2017.
 */
@Configuration
public class BatchConfig {

    private static final Logger LOG = LoggerFactory.getLogger(BatchConfig.class);

    @Bean
    @JobScope
    public RepositoryItemReader<PersonExport> personReader(PersonRepository repository, @Value("#{jobParameters[pageSize]}") int pageSize)
    {
        LOG.debug("Instantiating personReader bean");
        RepositoryItemReader<PersonExport> reader = new RepositoryItemReader<>();
        reader.setRepository(repository);
        reader.setMethodName("findAll");
        reader.setPageSize(pageSize);

        Map<String, Sort.Direction> sort = ImmutableMap.of("id", Sort.Direction.ASC);
        reader.setSort(sort);

        return reader;
    }

    @Bean
    @JobScope
    public FileDatabaseItemProcessor personProcessor()
    {
//        @Value("#{jobParameters[exportDateId]}") Long exportDateId
        LOG.debug("Instantiating personProcessor bean");
        return new FileDatabaseItemProcessor();
    }

    @Bean
    @JobScope
    public ItemWriter AccountWriter(@Value("#{jobParameters[fileName]}") String fileName, AccountExportRepository exportRepository, Jaxb2Marshaller recordMarshaller)
    {
        log.debug("Instantiating AccountWriter bean");
        RepositoryItemWriter<Export> accountExportRepositoryItemWriter = exportRepositoryItemWriter(exportRepository);

        StaxEventItemWriter<Record> accountRecordStaxEventItemWriter = recordStaxEventItemWriter(fileName, recordMarshaller);

        return new CddsItemWriter(accountExportRepositoryItemWriter, accountRecordStaxEventItemWriter);
    }

    @Bean
    public Job AccountJob(JobBuilderFactory jobs, Step AccountStep)
    {
        log.debug("Instantiating AccountJob bean");
        return jobs.get(CddsJobType.ACCOUNT.getJobName())
                   .incrementer(new RunIdIncrementer())
                   .flow(AccountStep)
                   .end()
                   .build();
    }

    @Bean
    public Step AccountStep(StepBuilderFactory stepBuilderFactory, ItemReader<AccountToExport> AccountReader,
                                AccountItemProcessor AccountProcessor, CddsItemWriter AccountWriter)
    {
        log.debug("Instantiating AccountStep bean");
        return stepBuilderFactory.get(CddsJobType.ACCOUNT.getStepName())
                .<AccountToExport, ProcessedItem> chunk(ChunkSize)
                .reader(AccountReader)
                .processor(AccountProcessor)
                .writer(AccountWriter)
                .build();
    }
}
