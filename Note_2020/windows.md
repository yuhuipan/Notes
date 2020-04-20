##### 1. 查看8080端口号占用情况 返回PID

~~~
netstat -ano|find "8080"
netstat -ano|findstr 8080
~~~

##### 2. tasklist查看进程使用PID

~~~
tasklist|find "pid"
tasklist|findstr pid
~~~

##### 3. 杀进程

~~~
taskkill /f /t /im java.exe

taskkill /f /t /pid pid

/F 指定要强行终止的进程
/T Tree kill: 终止指定的进程和任何由此启动的子进程
/IM image name 指定要终止的进程的映像名称。通配符 '*'可用来指定所有映像名
/PID process id 指定要终止的进程的PID
/FI filter 指定筛选进或筛选出查询的的任务
~~~

