#!/usr/bin/env bash

#set -x
# This script checks the prerequisites and starts the applications in the correct order.

check_build_targets() {
  # check if the build targets exist
  if [ ! -f "authorization-server/target/authorization-server.jar" ]; then
    echo "auth-server.jar does not exist."
    return 1
  fi
  if [ ! -f "resource-server/target/resource-server.jar" ]; then
    echo "resource-server.jar does not exist."
    return 1
  fi
  if [ ! -f "client-app/target/client-app.jar" ]; then
    echo "client-app.jar does not exist."
    return 1
  fi
}

check_prerequisites() {

  # check if there is a auth-server entry inn your /etc/hosts file
  if ! grep -q "auth-server" /etc/hosts ; then
    echo "Please add the following line to your /etc/hosts file:"
    echo "   127.0.0.1 auth-server"
    echo
    return 1
  fi
  echo "auth-server entry found in /etc/hosts. Continuing..."

  # check if screen is installed
  if screen -v >/dev/null 2>&1; then
    echo "screen is installed. Continuing..."
  else
    echo "screen is not installed. Please install screen."
    return 1
  fi

  # check if we run java 17
  if [ "$(java --version | awk 'NR==1 && $2 ~ /^17\.0/ {print 17}')" != "17" ] ; then
    echo "You are not running java 17"
    return 1
  else
    echo "You are running java 17. Continuing..."
  fi
}

create_screen_config() {
  # create the screen config file
  echo "Creating screen config file..."
  cat > start-apps.screenrc <<EOF
screen -t "Client Application" bash -c "sleep 5s; java -jar client-app/target/client-app.jar"
split -v
focus
screen -t "Resource Server" bash -c "sleep 5s; java -jar resource-server/target/resource-server.jar"
split
focus
screen -t "Authorization Server" bash -c "java -jar authorization-server/target/authorization-server.jar"
EOF
}

start_applications() {
  # start the applications
  echo "Starting the applications..."
  create_screen_config
  gnome-terminal -- screen -c start-apps.screenrc
}

if ! check_prerequisites ; then
  exit 1
fi

if ! check_build_targets; then
  echo "Build targets not found. Building applications..."
  echo "The build log is in mvn.log"
  mvn -B -l mvn.log clean install
fi

if ! check_build_targets; then
  echo "Build failed. Please check mvn.log for details."
  exit 1
fi

start_applications

echo
echo Kill the applications with:
for screen_id in $(screen -ls | awk 'NR >1 && !/^[0-9]+ Socket/ {print $1}') ; do echo "screen -XS ${screen_id} quit" ; done
echo

