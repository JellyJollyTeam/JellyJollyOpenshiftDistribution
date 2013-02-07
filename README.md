Jelly Jolly Openshift 发布版
===========================

Jelly Jolly是一个开源的博客引擎。这个发布版运行于Openshift的PAAS平台之上。
以下是部署并运行Jelly Jolly的步骤，并且假设您已安装并正确配置rhc（Openshift命令行工具）。
如果您还没有配置Openshift的命令行工具，请访问https://openshift.redhat.com/community/get-started，
并选择COMMAND LINE栏目，根据Openshift提供的步骤进行安装。
安装过程将需要5至10分钟，其中可能需要您多次输入Openshift的密码。
如果希望使用脚本自动执行，并且您的操作系统是Unix或其衍生（如Linux），请下载资料库中的install.sh运行。
安装是自动完成的，无需其他繁琐的过程。

在Openshift上创建一个新的应用
--------------------------
您需要为您的Jelly Jolly应用取一个名字，必须全部为英语字母(a-z)，例如jellyjolly

    rhc app create -a <应用的名字> -t jbossews-1.0

添加一个数据库应用
---------------

    rhc cartridge add -c mysql-5.1 -a <应用的名字>

下载Jelly Jolly的源码，并部署
--------------------------

    cd <应用的名字>
    git remote add upstream -m master git://github.com/JellyJollyTeam/JellyJollyOpenshiftDistribution.git
    git pull -s recursive -X theirs -f upstream master
    git push origin master

删除源码（可选）
------------
删除刚才部署所需的源码

    cd ..
    rm -rf <应用名称>

配置您已安装好的Jelly Jolly
------------------------
访问

    http://<应用的名字>-<您的命名空间>.rhcloud.com/install

根据提示，完成相关配置

    http://<应用的名字>-<您的命名空间>.rhcloud.com
