# ATLAS IoT Security Cloud
TODO add description

---

## Prerequisites
 Make sure you have installed all of the following prerequisites:
 - Apache Tomcat 9 - https://tomcat.apache.org/download-90.cgi
 -- In order to configure the TLS Tomcat connector, the following packages must be installed:
```
sudo apt-get install libapr1 libapr1-dev libtcnative-1
```
 
 - Mosquitto MQTT broker 1.6.8+
 -- In order to install the required version, the following repository must be added (Mosquitto version can be verified using `/usr/sbi/mosquitto -v`):
```
sudo apt-add-repository ppa:mosquitto-dev/mosquitto-ppa
```
- Mosquitto client tools must be installed (mosquitto_passwd tool is required). These should be installed along with the Mosquitto broker.

---

## Configuration
 The following elements must be configured before building and deploying the application:
1. Validate the following fields from the `application.properties` file
 - The following field indicates the path mosquitto_passwd utility tool (replace this if necessary). The full path of the binary can be found using the following command `which mosquitto_passwd`.
```
atlas-cloud.passwordTool = /usr/local/bin/mosquitto_passwd
```
 - The following field indicates a temporary directory which must be accessed by the Tomcat user - Read/Write/Execute permissions (replace this if necessary)
```
atlas-cloud.tmpDir = /tmp
```
2. Generate the server and client certificates along with the certificate chain. The web application uses a server certificate and a client certificate (mutual TLS authentication).
The following script must be executed to generate the certificates: `misc/scripts/pki/pki_init.sh`.
This script will generate the following certificates: a *ROOT CA*, an *intermediate CA (server sub-CA)* which signs the server certificate, an *intermediate CA (client sub-CA)* which signs the client certificate, the *server certificate* and the *client certificate*.
The `pkit_init.sh` script will prompt for a set of default information for the certificate authorities (can be used as default), a **FQDN** for the server certificate and a **fullname (with no spaces)** for the client certificate.
The PKI script will create the following directories:
* `misc/scripts/pki/ca` - this directory holds the certificate and private keys for the ROOT CA, server sub-CA and client sub-CA
* `misc/scripts/pki/artifacts` - this directory holds the generate server and client certificates as follows:
  * `client.truststore.pem` - this file contains the certificate truststore (contains the ROOT certificate and the server sub-CA certificate) which must be deployed on the **ATLAS Gateway** side.
  * `server.truststore.pem` - this file contains the certificate truststore (contains the ROOT certificate and the client sub-CA certificate) which must be deployed on the **Tomcat server**. This is used to valide the client certificate which accesses the web application using mutual authentication TLS.
  * `clients` - this directory contains one or more client certificate directories which are named with the client **Fullname**. Each client certificate directory contains the following files:
    * `<Fullname>.chain.pem` - this file contains the certificate chain for the client (contains the ROOT certificate and the client sub-CA certificate)
    * `<Fullname>`.crt.pem` - this file contains the client certificate
    * `<Fullname>`.key.pem` - this file contains the client private key
    * `<Fullname>.p12` - this file contains the client certificate and the private key in a PKCS#12 format (encrypted with the password required by the `pki_init.sh` script)
  * `servers` - this directory contains one or more server certificate directories which are named with the server **FQDN**. Each server certificate directory contains the following file:
    * `<FQDN>.chain.pem` - this file contains the certificate chain for the client (contains the ROOT certificate and the server sub-CA certificate)
    * `<FQDN>.crt.pem` - this file contains the server certificate
    * `<FQDN>.key.pem` - this file contains the server private key
---

## Build
  The application is build using the following command:
  ```
    mvn clean package -DskipTests
  ```

---

## Deployment
1. Deploy the *Mosquitto* files
* Run the `deploy.sh` script, which will install a credentials reload script and a wrapper executable for this script in `usr/local/sbin`. This script will also install the mosquitto config file in `/etc/mosquitto` directory.
* Copy the server certificate and private key into `/etc/mosquitto/certs` directory as follows:
```
cp misc/scripts/pki/artifacts/servers/<FQDN>.chain.pem /etc/mosquitto/certs/server.chain.pem
cp misc/scripts/pki/artifacts/servers/<FQDN>.crt.pem /etc/mosquitto/certs/server.crt.pem
cp misc/scripts/pki/artifacts/servers/<FQDN>.key.pem /etc/mosquitto/certs/server.key.pem
```
* Restart Mosquitto using `systemctl restart mosquitto`. Verify that Mosquitto is running using `systemctl status mosquitto` and that Mosquitto is listening on ports 1883 and 8883 using `sudo netstat -plotnu | grep 1883` and `sudo netstat -plotnu | grep 8883`
2. Copy the generated WAR application **atlas-cloud-1.0.war** into the Tomcat webapps directory as ROOT.war
3. Copy the Tomcat config from `misc/config/tomcat/server.xml` into Tomcat **conf** directory.
4. Copy server certificate and private key into the Tomcat **conf** directory as follows:
```
cp misc/scripts/pki/artifacts/servers/<FQDN>.chain.pem <Tomcat dir>/conf/server.chain.pem
cp misc/scripts/pki/artifacts/servers/<FQDN>.crt.pem <Tomcat dir>/conf/server.crt.pem
cp misc/scripts/pki/artifacts/servers/<FQDN>.key.pem <Tomcat dir>/conf/server.key.pem
```

