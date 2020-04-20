# tengine #

~~~
一、下载:
tengine.taobao.org
二、安装到/usr/local/tengine：
1.进入到tengine根目录  ./configure --prefix=/usr/local/tengine 
2.config error 
yum install gcc openssl-devel pcre-devel zlib-devel -y
3.执行 ./configure --prefix=/usr/local/tengine
4.make && make install
三、启动
1.cd /usr/local/tengine/sbin
2.执行 ./nignx
四、停止
kill -9 pid

~~~

##### 服务器内核每个进程能够打开多少个文件句柄个数 #####

~~~
cat /proc/sys/fs/file-max
95406
~~~




