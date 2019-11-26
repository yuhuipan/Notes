

## 规划

![1569068467729](C:\Users\yuhuipan\AppData\Roaming\Typora\typora-user-images\1569068467729.png)



## 配置HADOOP(cd $HADOOP_HOME/etc/hadoop)

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
        <name>dfs.replication</name>
        <value>2</value>
    </property>
    <property>
        <name>dfs.namenode.name.dir</name>
        <value>/var/bigdata/hadoop/ha/dfs/name</value>
    </property>
    <property>
        <name>dfs.datanode.data.dir</name>
        <value>/var/bigdata/hadoop/ha/dfs/data</value>
    </property>

	1)一对多，逻辑到物理节点的映射
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
    
    2)JN在哪里启动，数据存那个磁盘
    <!--journalnode-->
    <property>
      <name>dfs.namenode.shared.edits.dir</name>
      <value>qjournal://node1:8485;node2:8485;node3:8485/mycluster</value>
    </property>
    <property>
      <name>dfs.journalnode.edits.dir</name>
      <value>/var/bigdata/hadoop/ha/dfs/jn</value>
    </property>
    
    3)HA角色切换的代理类和实现方法，我们用的ssh免密
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
      <value>/root/.ssh/id_dsa</value>
    </property>
    
    4)开启自动化，启动zk进程
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
1)先启动JN  hadoop-daemon.sh start journalnode (node01, node02, node03)
2)选择一个NN做格式化：hdfs namenode -format (node01 or node02)
3)启动这个格式化的NN，以备另外一台同步 Hadoop-daemon.sh start namenode
4)在另外一台机器中：hdfs namenode -bootstrapStandby
5)格式化zk:  hdfs zkfc -formatZk
6)start-dfs.sh
~~~

使用

1)停止之前的集群

2)免密：node1 node2

~~~
node2:
cd ~/.ssh
ssh-keygen -t dsa -P '' -f ./id_dsa
cat id_dsa.pub >> authorized_keys
scp ./id_dsa.pub node1:`pwd`/node2.pub
node1:
cd ~/.ssh
cat node2.pub >> authorized_keys

~~~

## zookeeper

3)zookeeper 集群搭建 java语言开发 jdk ；部署在2,3,4

~~~
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
	
mkdir -p /var/bigdata/hadoop/zk
echo 1 > /var/bigdata/hadoop/zk/myid

vi /etc/profile
	export ZOOKEEPER_HOME=/opt/bigdata/zookeeper-3.4.6
	export PATH=$PATH:$JAVA_HOME/bin:$HADOOP_HOME/bin:$HADOOP_HOME/sbin:$ZOOKEEPER_HOME/bin 

分发到其他机器上
cd /opt/bigdata
scp -r ./zookeeper-3.4.6/ node3:`pwd`
scp -r ./zookeeper-3.4.6/ node4:`pwd`

node3:
mkdir -p /var/bigdata/hadoop/zk
echo 2 > /var/bigdata/hadoop/zk/myid
vi /etc/profile
	export ZOOKEEPER_HOME=/opt/bigdata/zookeeper-3.4.6
	export PATH=$PATH:$JAVA_HOME/bin:$HADOOP_HOME/bin:$HADOOP_HOME/sbin:$ZOOKEEPER_HOME/bin
. /etc/profile
node4:
mkdir /var/bigdata/hadoop/zk
echo 3 > /var/bigdata/hadoop/zk/myid
vi /etc/profile
	export ZOOKEEPER_HOME=/opt/bigdata/zookeeper-3.4.6
	export PATH=$PATH:$JAVA_HOME/bin:$HADOOP_HOME/bin:$HADOOP_HOME/sbin:$ZOOKEEPER_HOME/bin
source /etc/profile

启动zookeeper(node2,3,4)
zkServer.sh start
查看状态
zkServer.sh status

~~~

4)配置hadoop的core-site.xml和hdfs-site.xml (full-->ha)

5)分发配置

6)初始化

~~~
hadoop-daemon.sh start journalnode
~~~

7)使用验证

~~~
1)看jn的日志和目录变化
2）node4
zkCli.sh
	ls /
	启动之后可以看到锁：
	get /hadoop-ha/mycluster/ActiveStandbyElectorLock
3)杀死namenode 杀死zkfc
a)kill active NN
b)kill active NN 身边的zkfc
c)shutdown activeNN主机的网卡 ：
	node2节点一直阻塞降级
	node1节点恢复网卡
	最终node2变成active
~~~



~~~
创建目录：
hdfs dfs -mkdir -p /user/root
上传文件：
hdfs dfs -put data.txt /
~~~

node1~node4:

~~~
1)添加用户god：
    useradd god
    passwd god
2)将资源与用户绑定（a,安装部署程序目录 b,数据存放目录）
    chown -R god src
    chown -R god /opt/bigdata/hadoop-2.6.5/
    chown -R god /var/bigdata/hadoop/
3)切换到god去启动  start-dfs.sh  <-需要免密
	给god做免密  HA免密的两个场景都要做 (authoried_keys  -rw- rw- ---)
	su god
	ssh localhost >> 为了拿到.ssh目录
	node1,node2:
        cd ~/.ssh
        ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa (-t类型，-P密码 -f保存到)
	node1:
        ssh-copy-id -i id_rsa node1 
        ssh-copy-id -i id_rsa node2
        ssh-copy-id -i id_rsa node3
        ssh-copy-id -i id_rsa node4
    node2:
        ssh-copy-id -i id_rsa node1 
        ssh-copy-id -i id_rsa node2
4)hdfs-site.xml
    <property>
      <name>dfs.ha.fencing.ssh.private-key-files</name>
      <value>/home/god/.ssh/id_rsa</value>
    </property>
分发
    scp hdfs-site.xml node2:`pwd`
    scp hdfs-site.xml node3:`pwd`
    scp hdfs-site.xml node4:`pwd`

5)启动 god : $ start-dfs.sh
	hdfs dfs -mkdir /user/god

	hdfs dfs -mkdir /temp
修改资源/tmp的归属组(hdfs dfs -chown god:groupname /temp)
	hdfs dfs -chgrp groupname /temp
修改资源权限
	hdfs dfs -chmod 770 /temp
创建good用户(useradd good \n passwd \n ......)
在NN所在节点上创建对应组groupname，将god用户添加到groupname组内
	groupadd groupname
	usermod -a -G groupname good
	id good
	hdfs dfsadmin -refreshUserToGroupsMappins (execute in god)
	su good
	hdfs groups

~~~

idea 构建项目

~~~
<dependency>
<groupId>org.apache.hadoop</groupId>
<artifactId>hadoop-common</artifactId>
<version>2.6.5</version>
</dependency>
<dependency>
<groupId>org.apache.hadoop</groupId>
<artifactId>hadoop-hdfs</artifactId>
<version>2.6.5</version>
</dependency>
~~~

block块放置的位置

~~~
/var/bigdata/hadoop/ha/dfs/data/current/BP-732148681-192.168.88.128-1569142952322/current/finalized/subdir0/subdir0
~~~

shift + g 移动到文件末尾