Overview
========

The Northbound (NB) APIs that SDN controllers provide differ in terms of architecture, syntax, naming convention, data resources, and usage. Using NB APIs to write SDN applications makes each application dependent on the API of a specific controller. To bring NB APIs from different vendors under one umbrella and make programming of SDN applications independent of specific controllers, we designed and implemented a unified software defined development framework that we call Umbrella. The main design goals are as follows:

    * Design and implement a development framework that provides a new set of abstractions for SDN applications, keeping the abstractions independent of the NB APIs used by specific SDN controllers.
    * Design and implement a set of modules that use the proposed abstractions to provide information needed by SDN applications, such as topology, network statistics, and real time topology changes.
    * Increase portability of SDN applications across SDN controllers, and make it easy for a programmer to evaluate a specific application on multiple SDN controllers (e.g., to compare performance).
    * Provide a software defined network programming framework that reduces programming complexity, allows a programmer to write SDN applications without requiring a programmer to master low-level details for each SDN controller, and avoids locking an application to a specific controller.


Umbrella Architecture
---------------------




Getting Started
===============

Installation 
------------

1. First, clone umbrella repository using the following command::
   
   $ git clone https://github.com/umbrella-project/umbrella
   
   
2. Umbrella is a Java based platform. In order to be able to compile the code, you need to install Oracle Java 1.8 and Maven using the following commands::

   $ sudo apt-get install software-properties-common -y && \
   $ sudo add-apt-repository ppa:webupd8team/java -y && \
   $ sudo apt-get update && \
   $ echo "oracle-java8-installer shared/accepted-oracle-license-v1-1 select true" | sudo debconf-set-selections && \
   $ sudo apt-get install oracle-java8-installer oracle-java8-set-default -y   

   
   $sudo apt-get install maven
   

2. Finally, compile the project using the following command::

   $ cd umbrella
   $ mvn package 
   


Run a Sample Application
------------------------


Umbrella APIs
=============

Flow Service APIs
-----------------


Topology Service APIs
---------------------



Sample Applications
===================


