# RPC Template
## Version
* jdk 17
* spring-boot 3.4.1
* lombok 1.18.30
* hutool 5.8.26
* registry implement
  * redis 
    * server 7.0.15
    * redisson 3.43.0
  * etcd-core 0.7.7 (doesn't work)
## Knowledges
* Proxy class: generating proxy class in the provider to fulfill consumers' requests.
* singleton model: RpcConfig in the RpcApplication.
* Factory model: generating proxy of method provider.
* SPI design
