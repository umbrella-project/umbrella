# What is Umbrella? 

Umbrella is a unified software defined development framework that provides  a new set of APIs for implementing of SDN applications, keeping the abstractions independent of the Northbound APIs used by specific SDN controllers. The main design goals are: 

* Design and implement a development framework that provides a new set of abstractions for SDN applications, keeping the abstractions independent of the NB APIs used by specific SDN controllers.

* Design and implement a set of modules that use the proposed abstractions to provide information needed by SDN applications, such as topology, network statistics, and real time topology changes.
    
* Increase portability of SDN applications across SDN controllers, and make it easy for a programmer to evaluate a specific application on multiple SDN controllers (e.g., to compare performance).
    
* Provide a software defined network programming framework that reduces programming complexity, allows a programmer to write SDN applications without requiring a programmer to master low-level details for each SDN controller, and avoids locking an application to a specific controller.

* Provides a framework uses a hybrid approach that utilizes both of reactive and proactive approach for managing and programming of SDN networks that offers better scalability than a completely reactive network management. 

#### [Umbrella Architecture](http://umbrella.readthedocs.io/en/latest/overview.html)


## Getting Started

[Installation](http://umbrella.readthedocs.io/en/latest/getting_started.html#installation)

[Run a sample application](http://umbrella.readthedocs.io/en/latest/getting_started.html#run-a-sample-application)


## Sample Applications

[Forwarding](http://umbrella.readthedocs.io/en/latest/forwarding.html)

[Firewall](http://umbrella.readthedocs.io/en/latest/firewall.html)



## Publications

Our work [Umbrella: a unified software defined development framework](https://arxiv.org/pdf/1805.09250.pdf) appears at [ANCS 2018](http://www.ancsconf.org/)

## How to cite our work

@inproceedings{Comer:2018:UUS:3230718.3233546,
 author = {Comer, Douglas and Karandikar, Rajas H. and Rastegarnia, Adib},
 title = {Umbrella: A Unified Software Defined Development Framework},
 booktitle = {Proceedings of the 2018 Symposium on Architectures for Networking and Communications Systems},
 series = {ANCS '18},
 year = {2018},
 isbn = {978-1-4503-5902-3},
 location = {Ithaca, New York},
 pages = {148--150},
 numpages = {3},
 url = {http://doi.acm.org/10.1145/3230718.3233546},
 doi = {10.1145/3230718.3233546},
 acmid = {3233546},
 publisher = {ACM},
 address = {New York, NY, USA},
 keywords = {REST API, networks programming interfaces, northbound API, software defined networking},
} 




## LICENSE 
Umbrella is released under the Apache Licesne Version 2.0. 
