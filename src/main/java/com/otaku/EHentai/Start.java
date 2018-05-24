package com.otaku.EHentai;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Start implements ApplicationRunner,CommandLineRunner{
	
	@Autowired
	ProducerThread producer;
	
	@Autowired
	HandlerThread handler;
	
	@Autowired
	HandlerThread handler2;
	
	@Autowired
	WriteThread writeThread;
	
	public static class Listener implements Observer{
		@Override
		public void update(Observable o, Object arg) {
	        System.out.print("线程"+o.getClass().getName()+"将被重启");
			WriteThread wt = new WriteThread(9);
			wt.addObserver(this);
			new Thread(wt).start();
			
		}
	}
	
	public static void main(String args[]){
		SpringApplication.run(Start.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		
	}

	@Override
	public void run(String... args) throws Exception {
//		System.setProperty("http.proxySet", "true"); 
//		System.setProperty("http.proxyHost", "127.0.0.1"); 
//		System.setProperty("http.proxyPort", "1080");
//		
//		System.setProperty("https.proxySet", "true"); 
//		System.setProperty("https.proxyHost", "127.0.0.1"); 
//		System.setProperty("https.proxyPort", "1080");
		
		ExecutorService executor_producer = Executors.newFixedThreadPool(1);
		executor_producer.execute(producer);
		
		ExecutorService executor_handler = Executors.newFixedThreadPool(3);
		executor_handler.execute(handler);
		executor_handler.execute(handler2);
		
		writeThread.addObserver(new Listener());
		new Thread(writeThread).start();
		
	}
}
