#!/bin/bash



for ((i=1;i<=10;i+=1))
do
    echo $i
    #ifconfig spine402-eth1 up
    ifconfig spine401-eth1 down
    sleep 10
    ifconfig spine401-eth1 up
    #ifconfig spine402-eth1 down
    sleep 10
done
