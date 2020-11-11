package com.speedyteller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class Processor {
	
	@Async
	public Future<Integer> generateOrderNumber(ThreadPoolTaskExecutor executor) {
		
		List<Future<Integer>> orders = new ArrayList<>();
		
		ExecutorService threadpool = executor.getThreadPoolExecutor();
		
	    BuilderOrderNumber task = new BuilderOrderNumber();
        Future<Integer> future = threadpool.submit(task);
        System.out.println("Order code => "+ future.hashCode());
        
        orders.add(future);
        
        orders.stream().forEach(order ->{
			try {
				this.processOrder(future);
			} catch (InterruptedException | ExecutionException e) {
				log.warn("Interrupted thread", e);
			    Thread.currentThread().interrupt();
			}
        });
        
        threadpool.shutdown();
        return future;
	}
	
	@Async
	public void processOrder(Future<Integer> order) throws InterruptedException, ExecutionException {

		while (!order.isDone()) {
			System.out.println ("The order " + order.hashCode() + " was not processed!");
			Thread.sleep(3000);
		}
		System.out.println ("The order " + " [" + order.hashCode() + "] was processed!");
		Integer orderNumber = order.get();
		System.out.println ("The number generated for order" + "[" + order.hashCode() + "] is => " + orderNumber);
	}
}