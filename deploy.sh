#!/bin/bash

DEPLOY_DIR=/usr/local/sbin

if [[ $EUID > 0 ]]; then # we can compare directly with this syntax.
  echo "Please run this script as root/sudo"
  exit 1
fi

echo "******** Compile MQTT credentials reload wrapper executable ********"
gcc misc/exec/atlas_broker_credentials_exec.c -o atlas_broker_credentials_exec

echo "******** Deploy the MQTT credentials artifcats ********"
mv atlas_broker_credentials_exec $DEPLOY_DIR
cp misc/scripts/atlas_broker_credentials.sh $DEPLOY_DIR
# Set SUID to the credentials script wrapper and proper ownership
chown root:root $DEPLOY_DIR/atlas_broker_credentials.sh
chmod 700 $DEPLOY_DIR/atlas_broker_credentials.sh
chown root:root $DEPLOY_DIR/atlas_broker_credentials_exec
chmod 4755 $DEPLOY_DIR/atlas_broker_credentials_exec



