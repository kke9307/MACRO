package com.movie.configration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import com.movie.job.KkomarangHardBatchJob;
import com.movie.job.KkomarangHospitalBatchJob;
import com.movie.job.KkomarangYakwaBatchJob;
import com.movie.job.TelegramGetChatUpdateJob;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableScheduling
public class ScheduleConfiguration implements SchedulingConfigurer  {
	private final int POOL_SIZE = 100;
	
	@Override
	public void configureTasks(ScheduledTaskRegistrar str) {
		ThreadPoolTaskScheduler tts = new ThreadPoolTaskScheduler();
		tts.setPoolSize(POOL_SIZE);
		tts.initialize();

		str.setTaskScheduler(tts);
		
	}

	@Bean
	@ConditionalOnProperty(value = "batch.scheduler.enable.hardjob", matchIfMissing = true, havingValue = "true")
	public KkomarangHardBatchJob createHardMovieJobTrigger() {
		return new KkomarangHardBatchJob();
	}
	@Bean
	@ConditionalOnProperty(value = "batch.scheduler.enable.getchatid", matchIfMissing = true, havingValue = "true")
	public TelegramGetChatUpdateJob createTelegramGetChatIdTrigger() {
		return new TelegramGetChatUpdateJob();
	}
	@Bean
	@ConditionalOnProperty(value = "batch.scheduler.enable.yakwa", matchIfMissing = true, havingValue = "true")
	public KkomarangYakwaBatchJob createHardYaketingTrigger() {
		return new KkomarangYakwaBatchJob();
	}
	@Bean
	@ConditionalOnProperty(value = "batch.scheduler.enable.hospital", matchIfMissing = true, havingValue = "true")
	public KkomarangHospitalBatchJob createHardHospitalTrigger() {
		return new KkomarangHospitalBatchJob();
	}
	
}