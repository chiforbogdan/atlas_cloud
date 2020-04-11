#!/bin/bash

echo "******** Create client certificate ********"

echo "Please enter the client full name (with no spaces):"
 
read fullname

FULLNAME=$fullname openssl req -out ca/intermediate-client/csr/$fullname.csr.pem -newkey rsa:2048 -nodes -keyout ca/intermediate-client/private/$fullname.key.pem -config openssl_client.cnf
openssl ca -config openssl_intermediate_client_subca.cnf -days 1800 -notext -md sha512 -in ca/intermediate-client/csr/$fullname.csr.pem -out ca/intermediate-client/certs/$fullname.crt.pem

mkdir -p artifacts/clients/$fullname
cp ca/intermediate-client/private/$fullname.key.pem artifacts/clients/$fullname
cp ca/intermediate-client/certs/$fullname.crt.pem artifacts/clients/$fullname
