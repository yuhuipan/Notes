# centos 7

ip addr

serivce network restart

# systemctl



# docker 

systemctl start docker

docker search mysql / java / redis /

docker pull mysql / .. / ..

docker images 查看本地下载好的所有镜像

docker rmi image-id 命令删除指定的本地镜像

# root forget

启动，在选择进入系统的界面按“e”进入编辑页面

然后按向下键，找到以“Linux16”开头的行，在该行的最后面输入“init=/bin/sh”

接下来按“ctrl+X”组合键进入单用户模式

接下来再输入“mount -o remount,rw /”(注意mount与－o之间和rw与/之间的有空格)

然后再输入“passwd”回车

接下来再输入touch /.autorelabel,回车

输入exec /sbin/init,回车

# there are stoped jobs

jobs -l

fg %1  或者 kill %1

# 网卡配置

vi /etc/sysconfig/network-scripts/ifcfg-ens33

BOOTPROTO="static"
IPADDR="192.168.88.128"
NETMASK="255.255.255.0"
GATEWAY="192.168.88.2"
DNS1="114.114.114.114"

vm sift + pgUp / pgDn 查看之前的日志

# 主机名

vi /etc/hostname

# host与主机名映射的文件



vi /etc/hosts

systemctl stop firewalld  --关闭防火墙

systemctl disable firewalld  --禁用开机启动

systemctl status firewalld  --查看防火墙状态

vi /etc/selinux/config --禁用selinux     SELINUX=disabled 



yy 复制    nyy复制多行

p 粘贴

# 时间同步

yum install ntp -y

vi /etc/ntp.conf

server ntp1.aliyun.com  --加入这一行

systemctl start ntpd

systemctl enable ntpd  -- 开机启动



