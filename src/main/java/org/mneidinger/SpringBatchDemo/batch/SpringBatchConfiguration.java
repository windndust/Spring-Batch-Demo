package org.mneidinger.SpringBatchDemo.batch;

import java.util.List;

import org.mneidinger.SpringBatchDemo.jpa.model.ChildUser;
import org.mneidinger.SpringBatchDemo.jpa.model.SuperUser;
import org.mneidinger.SpringBatchDemo.jpa.model.User;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfiguration {

	@Bean
	public Job job(JobBuilderFactory jobBuilderFactory,
					StepBuilderFactory stepBuilderFactory,
					ItemReader<User> itemReader,
					ItemProcessor<User, User> itemProcessor,
					ItemWriter<User> itemWriter) {
		
		Step step = stepBuilderFactory.get("ETL-file-load")
						.<User, User>chunk(100)
						.reader(itemReader)
						.processor(itemProcessor)
						.writer(itemWriter)
						.build();
		
		
		
		{
			StepBuilder sb = stepBuilderFactory.get("hello");
			SimpleStepBuilder<User, User> ssb = sb.<User, User>chunk(10);
			
			ssb.reader(new ItemReader<ChildUser>() {
				@Override
				public ChildUser read()
						throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
					// TODO Auto-generated method stub
					return null;
				}			
			});
			
			ssb.processor(new ItemProcessor<SuperUser, ChildUser>(){
				@Override
				public ChildUser process(SuperUser item) throws Exception {
					// TODO Auto-generated method stub
					return null;
				}				
			});
			ssb.writer(new ItemWriter<SuperUser>(){
				@Override
				public void write(List<? extends SuperUser> items) throws Exception {
					// TODO Auto-generated method stub
					
				}				
			});
		}
		
		
		//Extract, transform, load (ETL) 
		//Procedure of copying data from one or more sources into a destination system which represents the data differently from the source(s) or in a different context  than the source(s).
		return jobBuilderFactory.get("ETL-Load")
				.incrementer(new RunIdIncrementer())
				.start(step)
				.build();
		
		
		/*	if you have multiple steps
				jobBuilder.Factory.get("ETL-Load")
				.incrementer(new RunIdIncrementer())
				.flow(step)
				.next(step)
				.build();		
		*/
	}
	
	@Bean
	public FlatFileItemReader<User> itemReader(@Value("${input}") Resource resource){		
		FlatFileItemReader<User> flatFileItemReader = new FlatFileItemReader<>();
		flatFileItemReader.setResource(resource);
		flatFileItemReader.setName("CSV-Reader");
		flatFileItemReader.setLinesToSkip(1);
		flatFileItemReader.setLineMapper(lineMapper());
		return flatFileItemReader;
	}
	
	private LineMapper<User> lineMapper() {
		DefaultLineMapper<User> defaultLineMapper = new DefaultLineMapper<>();
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		
		lineTokenizer.setDelimiter(",");
		lineTokenizer.setStrict(false);
		lineTokenizer.setNames(new String[] {"id", "name" ,"dept", "salary"});

		defaultLineMapper.setLineTokenizer(lineTokenizer);
		
		BeanWrapperFieldSetMapper<User> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(User.class);
		
		defaultLineMapper.setFieldSetMapper(fieldSetMapper);		
		return defaultLineMapper;
	}
}
