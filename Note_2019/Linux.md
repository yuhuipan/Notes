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

