~~~
1.最终去开发MR计算程序
	*HDFS和YARN 是两个概念
2.hadoop2.x出现了一个yarn：资源管理  》 MR 没有后台常服务
	yarn模型：container 容器，里面会运行AppMaster,map/reduce Task
		解耦
		mapreduce on yarn
	架构：
		RM
		NM
	搭建：
				NN	RM	JN	ZKFC	ZK	DN	NM
		node1	v		v	  v				
		node2	v		v	  v		v	v	v
		node3		v	v			v	v	v
		node4		v				v	v	v
3.通过官网
etc/hadoop/mapred-site.xml:  >mapreduce on yarn
    <configuration>
        <property>
            <name>mapreduce.framework.name</name>
            <value>yarn</value>
        </property>
    </configuration>
    
etc/hadoop/yarn-site.xml:
// shuffle 洗牌 M -shuffle > R -
    <configuration>
        <property>
            <name>yarn.nodemanager.aux-services</name>
            <value>mapreduce_shuffle</value>
        </property>
    </configuration>
    <!--HA-->
    <property>
        <name>yarn.resourcemanager.ha.enabled</name>
        <value>true</value>
    </property>
    <property>
        <name>yarn.resourcemanager.cluster-id</name>
        <value>cluster_yarn_rm</value>
    </property>
    <property>
        <name>yarn.resourcemanager.ha.rm-ids</name>
        <value>rm1,rm2</value>
    </property>
    <property>
        <name>yarn.resourcemanager.hostname.rm1</name>
        <value>node3</value>
    </property>
    <property>
        <name>yarn.resourcemanager.hostname.rm2</name>
        <value>node4</value>
    </property>
    <property>
        <name>yarn.resourcemanager.zk-address</name>
        <value>node2:2181,node3:2181,node4:2181</value>
    </property>
~~~

流程

~~~
1.修改配置文件并分发
2.启动  
	start-yarn.sh  启动nodemanager
	yarn-daemon.sh start resourcemanager(node3,node4上手工启动)
node3:8088    about  ,  nodes
node4:8088
cd /opt/bigdata/hadoop-2.6.5/share/hadoop/mapreduce
hadoop jar hadoop-mapreduce-examples-2.6.5.jar wordcount /data/wc/input /data/wc/output
~~~

Idea开发 

~~~
pom : hadoop-common  hadoop-hdfs
加入 hadoop-client
<dependency>
    <groupId>org.apache.hadoop</groupId>
    <artifactId>hadoop-client</artifactId>
    <version>2.6.5</version>
</dependency>
~~~

代码

~~~
// TODO
~~~

上传jar包运行

~~~
Hadoop jar hadoop_hdfs-1.0-SNAPSHOT 主类全限定名
~~~

