# ATLAS IoT Security cloud
TODO add description

---

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
- Mosquitto client tools must be installed (mosquitto_passwd tool is required)

---

## Configuration
 The following elements must be configured before building and deploying the application:
1. Validate the following fields from the application.properties file
 - The following field indicates the path mosquitto_passwd utility tool (replace this if necessary):
```
atlas-cloud.passwordTool = /usr/local/bin/mosquitto_passwd
```
 - The following field indicates a temporary directory which must be accessed by the Tomcat user - Read/Write/Execute permissions (replace this if necessary)
```
atlas-cloud.tmpDir = /tmp
```
2. Generate the server and client certificates along with the certificate chain. The web application uses a server certificate and a client certificate (mutual TLS authentication).
The following script must be executed to generate the certificates.
`misc/scripts/pki/pki_init.sh`
This script will generate the following certificates: a ROOT CA, an intermediate CA which signs the server certificate, an intermediate CA which signs the client certificate, the server certificate and the client certificate. The **pkit_init.sh** script will prompt a set of default information for the certificate authorities, a FQDN for the server certificate and a fullname (with no spaces) for the client certificate.
---

## Build
  The application is build using the following command:
  ```
    mvn clean package -DskipTests
  ```

---

## Deployment 
Copy the generated WAR application atlas-cloud-1.0.war into the Tomcat webapps directory as ROOT.war
