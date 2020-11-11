package com.speedyteller;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@SpringBootApplication
public class ProcessApplication {
	
	@Autowired
	private Processor processor;

	public static void main(String[] args) {
		SpringApplication.run(ProcessApplication.class, args);
	}
	
	@Bean
	public void worker() throws InterruptedException, ExecutionException {
		System.out.println("================");
		System.out.println("We are started!");
		System.out.println("================");
		
		final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(10);
		executor.setMaxPoolSize(100);
		executor.initialize();
		
		for(int poolsize = 0; poolsize <= executor.getCorePoolSize(); poolsize++ ) {
			processor.generateOrderNumber(executor);
		}
	}
}
