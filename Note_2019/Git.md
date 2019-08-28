---
typora-root-url: ..\pics
---

#  git clone

git clone url

#  git branch

git branch	--查看本地分支

git branch -r	--查看远程分支

git branch -a	--查看所有分支（本地和远程）

git branch -D branchName	--删除本地分支（-D也可以是--delete）

![1566958137786](/1566958137786.png)

# git checkout

切换分支

git checkout branchName

拉取远程分支并创建本地分支

方法一：

git fetch 

git checkout -b branchName origin/branchName

方法二：

git fetch origin branchName:branchName