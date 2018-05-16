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
   $ sudo apt-get install maven
   

2. Finally, compile the project using the following command::

   $ cd umbrella
   $ mvn package 
   


Run a Sample Application
------------------------

To run an SDN application that we implemented using Umbrella APIs, we can use the following commands::
    
     $ cd umbrella
     $ mvn package
     $ java -cp target/umbrella-[Version]-SNAPSHOT-jar-with-dependencies.jar apps.[Application filename]

