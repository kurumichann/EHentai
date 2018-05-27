package dao;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import entity.EHentai;

public class EHentaiDao_Imp implements EHentaiDao_Interface{

	SqlSession session;
	
	{
    	String resource = "mybatis-config.xml";      
        Reader reader = null;
		try {
			reader = Resources.getResourceAsReader(resource);
		} catch (IOException e) {
			e.printStackTrace();
		}          
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);  
        session = sqlSessionFactory.openSession();
	}
	
	@Override
	public void putManga(EHentai manga) {
		session.insert("putManga", manga);
		session.commit();
	}

}
