

## 规划

![1569068467729](C:\Users\yuhuipan\AppData\Roaming\Typora\typora-user-images\1569068467729.png)



## 配置

### core-site.xml      -->  defaultFS

~~~
<property>
  <name>fs.defaultFS</name>
  <value>hdfs://mycluster</value>
</property>

<property>
  <name>ha.zookeeper.quorum</name>
  <value>node2:2181,node3:2181,node4:2181</value>
</property>
~~~

### hdfs-site.xml

~~~
<property>
  <name>dfs.nameservices</name>
  <value>mycluster</value>
</property>

<property>
  <name>dfs.ha.namenodes.mycluster</name>
  <value>nn1,nn2</value>
</property>
<!--rpc-address-->
<property>
  <name>dfs.namenode.rpc-address.mycluster.nn1</name>
  <value>node1:8020</value>
</property>
<property>
  <name>dfs.namenode.rpc-address.mycluster.nn2</name>
  <value>node2:8020</value>
</property>
<!--http-address-->
<property>
  <name>dfs.namenode.http-address.mycluster.nn1</name>
  <value>node1:50070</value>
</property>
<property>
  <name>dfs.namenode.http-address.mycluster.nn2</name>
  <value>node2:50070</value>
</property>
<!--journalnode-->
<property>
  <name>dfs.namenode.shared.edits.dir</name>
  <value>qjournal://node1:8485;node2:8485;node3:8485/mycluster</value>
</property>
<property>
  <name>dfs.journalnode.edits.dir</name>
  <value>/var/bigdata/hadoop/ha/dfs/jn</value>
</property>
<!--ConfiguredFailoverProxyProvider-->
<property>
  <name>dfs.client.failover.proxy.provider.mycluster</name>
  <value>org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider</value>
</property>
<!--ssh免密-->
<property>
  <name>dfs.ha.fencing.methods</name>
  <value>sshfence</value>
</property>
<property>
  <name>dfs.ha.fencing.ssh.private-key-files</name>
  <value>/root/.ssh/id_rsa</value>
</property>
<!--automatic-failover-->
<property>
  <name>dfs.ha.automatic-failover.enabled</name>
  <value>true</value>
</property>
~~~

### 流程

基础设施

~~~
# ssh免密
1）启动start-dfs.sh脚本的机器需要将公钥分发给别的节点
2）在HA模式下，每一个NN身边会启动ZKFC，ZKFC会用免密的方式控制自己和其他NN节点的NN状态
~~~

应用搭建

~~~
HA 依赖 ZK 搭建ZK集群
修改Hadoop的配置文件，并集群同步
~~~

初始化启动

~~~
1)先启动JN  hadoop-daemon.sh start journalnode
2)选择一个NN做格式化：hdfs namenode -format
3)启动这个格式化的NN，以备另外一台同步 Hadoop-daemon.sh start namenode
4)在另外一台机器中：hdfs namenode -bootstrapStandby
5)格式化zk:  hdfs zkfc -formatZk
6)start-dfs.sh
~~~

使用

~~~
1)停止之前的集群
2)免密：node1 node2
node2:
cd ~/.ssh
ssh-keygen -t dsa -P '' -f ./id_dsa
cat id_dsa.pub >> authoried keys
scp ./id_dsa.pub node1:`pwd`/node2.pub
node1:
cd ~/.ssh
cat node2.pub >> authoried keys
3)zookeeper 集群搭建 java语言开发 jdk ；部署在2,3,4
node2:
tar xf zookeeper...tar.gz
mv zoo....  /opt/bigdata
cd /opt/bigdata/zoo...
cd conf
cp zoo_sample.cfg zoo.cfg
vi zoo.cfg
	dataDir=/var/bigdata/hadoop/zk
	server.1=node2:2888:3888
	server.2=node3:2888:3888
	server.3=node4:2888:3888
mkdir /var/bigdata/hadoop/zk
echo 1 > /var/bigdata/hadoop/zk/myid
vi /etc/profile
	export ZOOKEEPER_HOME=/opt/bigdata/zookeeper-3.4.6
	export PATH=$PATH:$JAVA_HOME/bin:$HADOOP_HOME/bin:$HADOOP_HOME/sbin:$ZOOKEEPER_HOME/bin

~~~

