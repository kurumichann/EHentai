# EHentai
doujinshi info collection

#Profile</br>
  做这个的初衷纯粹是想做个etl收集点数据来玩，而且e绅士的搜索不太好用，不能按自己想要的标签进行多重过滤。</br>
  根据页数遍历E绅士的各本子属性，主要包括本子名称，提交时间，提交者，作者，收藏数量，平均分，语言还有各种本子标签等。没有使用网站提供的API，因为觉得太麻烦233。
其实根据数据库范式应该单独建一个标签表的，但是数据量太大小服务器撑不住，暂时把tag属性拼接成一个字段了orz。</br>
  因为没有做ip反向代理（后面可能会加上），所以严格控制线程的数量和请求的间隔（事实上现在 4s*随机数 的间隔爬了3w+条后也被限制访问了）。</br>
  
#Dependency</br>
  jsoup,springboot(starter,actuator),mysql-connector-java,myabtis。具体可见pom.xml</br>
  
#Log</br>
  2018.5.24 </br>
  提交springboot项目</br>
  2018.5.26</br>
  使用actuator做程序监控</br>
  2018.5.27
  修改了时间间隔策略，可以根据配置文件确定各线程的数量
#how to start</br>
  构建</br>
      本项目是maven工程，所以只需要导入项目，eclipse的话项目右键-》maven-》update Project即可</br>
      数据库建表语句在 /script/EHentai.sql里。数据库ip和库名需要在mybatis-config.xml里面配</br>
  启动</br>
      start.java->RUN Application</br>
