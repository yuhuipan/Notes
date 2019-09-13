# centos 7

```
ip addr
serivce network restart
```

# systemctl

```
systemctl status xxx
systemctl start xxx
systemctl stop xxx
systemctl restart xxx
```

# docker 

```
systemctl start docker
docker search mysql / java / redis /
docker pull mysql / .. / ..
docker images 查看本地下载好的所有镜像
docker rmi image-id 命令删除指定的本地镜像
```

# root forget

```
1.启动，在选择进入系统的界面按“e”进入编辑页面
2.然后按向下键，找到以“Linux16”开头的行，在该行的最后面输入“init=/bin/sh”
3.接下来按“ctrl+X”组合键进入单用户模式
4.接下来再输入“mount -o remount,rw /”(注意mount与－o之间和rw与/之间的有空格)
5.然后再输入“passwd”回车
6.接下来再输入touch /.autorelabel,回车
7.输入exec /sbin/init,回车
```

# there are stoped jobs

```
jobs -l
fg %1  或者 kill %1
```

# 网卡配置

vi /etc/sysconfig/network-scripts/ifcfg-ens33

```
BOOTPROTO="static"
IPADDR="192.168.88.128"
NETMASK="255.255.255.0"
GATEWAY="192.168.88.2"
DNS1="114.114.114.114"
```

vm sift + pgUp / pgDn 查看之前的日志

# 主机名

```
vi /etc/hostname
```

# host与主机名映射的文件

```
vi /etc/hosts
systemctl stop firewalld  --关闭防火墙
systemctl disable firewalld  --禁用开机启动
systemctl status firewalld  --查看防火墙状态
vi /etc/selinux/config --禁用selinux     SELINUX=disabled 
```

# vi

```
yy 复制    
nyy复制多行
p 粘贴
/ 查找
```

# 时间同步

```
yum install ntp -y
vi /etc/ntp.conf
server ntp1.aliyun.com  --加入这一行
systemctl start ntpd
systemctl enable ntpd  -- 开机启动
```

# rpm

```
rpm -ivh 包名    安装
rpm -qa   查找所有安装过的软件包  | grep jdk
```

有些软件只认：/usr/java/default

java : /usr/java/default

# 设置环境变量

```
vi /etc/profile
添加两行
export JAVA_HOME=/usr/java/default
export PATH=$PATH:$JAVA_HOME/bin
```

source /etc/profile  或者  . /etc/profile  使修改的配置生效

# 设置ssh免密

~~~
ssh localhost  1.登录自己需要密码，2.被动生成了 /root/.ssh
ssh-keygen -t dsa -P '' -f ~/.ssh/id_dsa    (-t类型，-P密码 -f保存到)
cat ~/.ssh/id_das.pub>> ~/.ssh/authorized_keys
~~~

结论：B包含了A的公钥，B登录A就可以免密了

# hadoop

1.规划路径

```
mkdir /opt/bigdata
解压
tar xvf hadoop-2.6.5.tar.gz
移动
mv hadoop-2.6.5 /opt/bigdata
```

vi /etc/profile  （环境变量加入HADOOP_HOME)

~~~
export HADOOP_HOME=/opt/bigdata/hadoop-2.6.5
将 $HADOOP_HOME/bin 和$HADOOP_HOME/sbin加入到PATH中
~~~

2.配置  

hadoop-env.sh 

```
cd $HADOOP_HOME/etc/hadoop
vi hadoop-env.sh (必须给Hadoop配置JAVA_HOME,要不ssh过去找不到)
export JAVA_HOME=/usr/java/default
```

core-site.xml: (给出nameNode角色在哪启动)

```
<configuration>
    <property>
        <name>fs.defaultFS</name>
        <value>hdfs://node1:9000</value>
    </property>
</configuration>
```

 hdfs-site.xml: (配置hdfs 副本数为1; datanode,namenode,secondarynamenode目录)

```
<configuration>
    <property>
        <name>dfs.replication</name>
        <value>1</value>
    </property>
    <property>
        <name>dfs.namenode.name.dir</name>
        <value>/var/bigdata/hadoop/local/dfs/name</value>
    </property>
    <property>
        <name>dfs.datanode.data.dir</name>
        <value>/var/bigdata/hadoop/local/dfs/data</value>
    </property>
    <property>
        <name>dfs.namenode.secondary.http-address</name>
        <value>node1:50090</value>
    </property>
    <property>
        <name>dfs.namenode.checkpoint.dir</name>
        <value>/var/bigdata/hadoop/local/dfs/secondary</value>
    </property>

</configuration>
```

vi slaves  （配置dataNoed角色在哪启动）,添加下面内容

```
node1
```

# 启动

格式化  format the filesystem

```
bin/hdfs namenode -format
```

启动、停止

```
sbin/start-dfs.sh
sbin/stop-dfs.sh
```

windows hosts文件 c:/windows/system32/drivers/etc/hosts  ip  主机映射

http://node1:50070   http://node1:50090 

简单使用

```
hdfs dfs -mkdir /bigdata
hdfs dfs -mkdir -p /user/root

```

验证知识点

```
cd /var/bigdata/hadoop/local/dfs/name/current
观察editlog的id是不是在fsimages的后面
cd /var/bigdata/hadoop/local/dfs/secondary/current
观察 SNN 只需要从NN拷贝最后时点的FSImages和增量的editlog
```

#  hadoop HA模式

