package com.otaku.EHentai;

import java.util.Observable;
import java.util.concurrent.BlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;

import entity.EHentai;

public class WriteThread extends Observable implements Runnable{

	int threadNo;
	@Autowired
	BlockingQueue<EHentai> handledQueue;
	EHentai hentai = new EHentai();
	
	public WriteThread(int threadNo) {
		this.threadNo = threadNo;
	}
	
	int cnt = 0;
	
	@Override
	public void run() {
        while(true){
        	EHentai manga = null;
        	try {
				manga = handledQueue.take();
				System.out.println("共取出了"+cnt+++"条\nhandledQueue: "+handledQueue.size()+"/1000\n");
				manga.saveData();
			} catch (Exception e) {
				notifyObservers();
				System.out.println("炸了，等观察者重启本线程");
				e.printStackTrace();
			}

        }
	}

}
