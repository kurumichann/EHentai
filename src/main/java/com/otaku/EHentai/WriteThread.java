package com.otaku.EHentai;

import java.util.Observable;
import java.util.concurrent.BlockingQueue;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import entity.EHentai;

@Component
public class WriteThread extends Observable implements Runnable{

	int threadNo;
	
	@Autowired
	BlockingQueue<EHentai> handledQueue;
	
	@Autowired
	Logger log;
	EHentai hentai = new EHentai();
	
	public WriteThread(int threadNo, ThreadObserver threadObserver) {
		this.threadNo = threadNo;
	    this.addObserver(threadObserver);
	}
	
	int cnt = 0;
	
	@Override
	public void run() {
        while(true){
        	EHentai manga = null;
        	try {
        		System.out.println("!!!"+handledQueue.size());
				manga = handledQueue.take();
				log.info("共取出了"+cnt+++"条  handledQueue: "+handledQueue.size()+"/1000\n");
				manga.saveData();
			} catch (Exception e) {
				notifyObservers();
				log.error("炸了，等观察者重启本线程");
				e.printStackTrace();
			}

        }
	}

}
