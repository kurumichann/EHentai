package conf;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.otaku.EHentai.HandlerThread;
import com.otaku.EHentai.ProducerThread;
import com.otaku.EHentai.WriteThread;

import entity.EHentai;

@Configuration
public class Conf {
 
	public volatile static BlockingQueue<String> toBeHandledQueue;
	public volatile static BlockingQueue<EHentai> handledQueue;
	
	int producerNo = 0;
	int handlerNo = 0;
	int writeNo = 0;
	
	@Bean BlockingQueue<String> toBeHandledQueue(){
		if(toBeHandledQueue!=null){
			return toBeHandledQueue;
		}else{
			synchronized (this) {
				if(toBeHandledQueue!=null){
					return toBeHandledQueue;
				}else{
					toBeHandledQueue = new LinkedBlockingQueue<>(1000);
					return toBeHandledQueue;
				}
			}
		}
	}
	
	@Bean BlockingQueue<EHentai> handledQueue(){
		if(handledQueue!=null){
			return handledQueue;
		}else{
			synchronized (this) {
				if(handledQueue!=null){
					return handledQueue;
				}else{
					handledQueue = new LinkedBlockingQueue<>(1000);
					return handledQueue;
				}
			}
		}
	}
	
	@Bean
	public ProducerThread producerThread(){
		return new ProducerThread(producerNo++);
	}
	
	@Bean
	public HandlerThread handlerThread(){
		return new HandlerThread(handlerNo++);
	}
	
	@Bean
	public WriteThread writeThread(){
		return new WriteThread(writeNo);
	}
	
}
