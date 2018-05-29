package com.otaku.EHentai;

import java.util.concurrent.ExecutorService;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import conf.Conf;

@SpringBootApplication
@ComponentScan
@ComponentScan(basePackageClasses=Conf.class)
public class Start implements ApplicationRunner,CommandLineRunner{
	
	@Autowired
	ProducerThread producerThread;
	
	@Autowired
	HandlerThread handlerThread;
	
	@Autowired
	WriteThread writeThread;
	
	@Autowired
	ThreadObserver threadObserver;
	
	@Autowired
	ExecutorService executor;
	
	@Autowired
	Logger log;
	
	@Value("${localEnvironment}")
	boolean localEnvironment;
	
	@Value("${conf.producerThreadNumber}")
	int producerThreadNumber;
	
	@Value("${conf.handlerThreadNumber}")
	int handlerThreadNumber;
	
	@Value("${conf.writeThreadNumber}")
	int writeThreadNumber;
	
	public static void main(String args[]){
		SpringApplication.run(Start.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		
	}

	@Override
	public void run(String... args) throws Exception {
		if(localEnvironment){
			System.setProperty("http.proxySet", "true"); 
			System.setProperty("http.proxyHost", "127.0.0.1"); 
			System.setProperty("http.proxyPort", "1080");
			
			System.setProperty("https.proxySet", "true"); 
			System.setProperty("https.proxyHost", "127.0.0.1"); 
			System.setProperty("https.proxyPort", "1080");
		}
		
		producerThread.addObserver(threadObserver);
		handlerThread.addObserver(threadObserver);
		writeThread.addObserver(threadObserver);
		
		log.info("Link Start");
		for(int i = 0 ; i < producerThreadNumber ; i++){
			executor.execute(producerThread);
		}
		for(int i = 0 ; i < handlerThreadNumber ; i++){
			executor.execute(handlerThread);
		}
		for(int i = 0 ; i < writeThreadNumber ; i++){
			executor.execute(writeThread);
		}
	}
}
