Jelly Jolly Openshift Distribution
==================================

Jelly Jolly is an open source blog engine. This distribution is used to deploy on Openshift.
Here are the steps to deploy it, assuming that you have already created an account of Openshift and have installed the git and rhc client.
It will take about 5 minutes to get all things done.

Create an application to deploy on OpenShift
--------------------------------------------
You have to name you jellyjolly application first, such as jellyjolly or blog.

    rhc app create -a <your-app-name> -t jbossews-1.0

Add a mysql cartridge to your application
-----------------------------------------

    rhc app cartridge add -c mysql-5.1 -a <your-app-name>

Download the source code of Jelly Jolly from Github and push it to the openshift repo
-------------------------------------------------------------------------------------

    cd <app-name>
    git remote add upstream -m master git://github.com/JellyJollyTeam/JellyJollyOpenshiftDistribution.git
    git pull -s recursive -X theirs upstream master
    git push

Delete the source code if you really want to
--------------------------------------------
It is needed to remove the source code if you want to deploy it again.

    cd ..
    rm -rf <app-name>

Install the Jelly Jolly
-----------------------
Goto

    http://<your-app-name>-<your-namespace>.rhcloud.com/install

Fill in the table on your website to make it work.

That's it, you can now checkout your Jelly Jolly at:

    http://<your-app-name>-<your-namespace>.rhcloud.com
