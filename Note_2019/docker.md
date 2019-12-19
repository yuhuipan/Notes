# Docker安装

~~~
yum install -y yum-utils device-mapper-persistent-data lvm2
yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
yum makecache
yum -y install docker-ce

systemctl start docker
docker version
docker pull hello-world

docker run -di -p 8000:8080 tomcat

// 进入容器
docker exec -it 容器id /bin/bash   （-it交互模式）
cat /proc/version
java -version
exit

// docker 存放
/var/lib/docker

docker images
docker ps
docker ps -a
docker rm containerId
docker rmi imageId


docker build -t 机构/镜像名<:tags> Dockerfile目录

~~~

# Dockerfile 自动部署Tomcat应用

~~~
FROM tomcat:latest
MAINTAINER yh.com
WORKDIR /usr/local/tomcat/webapps
ADD docker-web ./docker-web
~~~

~~~
docker build -t yuhui.com/mywebapp:1.0 .  (Dockerfile文件所在目录运行)
~~~

