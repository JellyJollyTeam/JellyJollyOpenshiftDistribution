#!/bin/bash
read -p 'Please enter a name for your Jelly Jolly application: ' APP_NAME
rhc app create -a $APP_NAME -t jbossews-1.0
rhc cartridge add -c mysql-5.1 -a $APP_NAME
cd $APP_NAME
git remote add upstream -m master git://github.com/JellyJollyTeam/JellyJollyOpenshiftDistribution.git
git pull -s recursive -X theirs -f upstream master
git push origin master
