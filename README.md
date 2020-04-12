# atlas_cloud
ATLAS IoT security cloud
 
## Prerequisites
 Make sure you have installed all of the following prerequisites:
 - Apache Tomcat 9 - https://tomcat.apache.org/download-90.cgi
 -- In order to configure the TLS Tomcat connector, the following packages must be installed:
```
sudo apt-get install libapr1 libapr1-dev libtcnative-1
```
  
 - Mosquitto MQTT broker 1.6.8
 -- In order to install the required version, the following repository must be added:
```
sudo apt-add-repository ppa:mosquitto-dev/mosquitto-ppa
```
- Mosquitto client tools must be installed and mosquitto_passwd tool must be located in the /usr/local/sbin directory. Otherwise, configure the correct path for the mosquitto_passwd tool in the application.properties file. 
