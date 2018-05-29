package com.otaku.EHentai;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ThreadObserver implements Observer{
	
	@Autowired
	ExecutorService executor;
	
	@Override
	public void update(Observable o, Object arg) {
        System.out.print("线程"+o.getClass().getName()+"将被重启");
		Object kurumi = null;
		try {
			kurumi = o.getClass().newInstance();
		} catch (Exception e) {
			System.err.print("获取线程实例失败");;
		}
		executor.execute((Runnable) kurumi);
	}
}
