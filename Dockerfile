#原始镜像
FROM adoptopenjdk/openjdk11:debian

#2.指明该镜像的作者和其电子邮件
MAINTAINER xhw "2752181597@qq.com"
EXPOSE 2333
#3.在构建镜像时，指定镜像的工作目录，之后的命令都是基于此工作目录，如果不存在，则会创建目录
WORKDIR /java

ADD build/libs /java
#5.设置时区
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo 'Asia/Shanghai' >/etc/timezone
#更新下载源 安装 工具
RUN mv /etc/apt/sources.list /etc/apt/sources.list.bak && echo "deb http://mirrors.163.com/debian/ buster main non-free contrib" > /etc/apt/sources.list && echo "deb http://mirrors.163.com/debian/ buster-updates main non-free contrib" >> /etc/apt/sources.list && echo "deb http://mirrors.163.com/debian/ buster-backports main non-free contrib" >> /etc/apt/sources.list && echo "deb http://mirrors.163.com/debian-security/ buster/updates main non-free contrib" >> /etc/apt/sources.list && echo "deb-src http://mirrors.163.com/debian/ buster main non-free contrib" >> /etc/apt/sources.list && echo "deb-src http://mirrors.163.com/debian/ buster-updates main non-free contrib" >> /etc/apt/sources.list && echo "deb-src http://mirrors.163.com/debian/ buster-backports main non-free contrib" >> /etc/apt/sources.list && echo "deb-src http://mirrors.163.com/debian-security/ buster/updates main non-free contrib" >> /etc/apt/sources.list && apt-get update && apt-get install -y vim less wget; exit 0

ENTRYPOINT ["java","-jar","petpet-3.8.jar"]
#RUN ["java","-jar","petpet-3.4.jar"]