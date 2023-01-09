#!/bin/bash

setupEnvVars() {
  disableAuthChecks=$1 # 0=false, 1=true

  export ADMIN_TANAGRA_CORE_USE_ADC=true

  export ADMIN_DATABASE_NAME=admin_db
  export ADMIN_DB_INITIALIZE_ON_START=false
  export ADMIN_DB_USERNAME=dbuser
  export ADMIN_DB_PASSWORD=dbpwd

  export ADMIN_AUTH_IAP_GKE_JWT=false
  if [ $disableAuthChecks == 1 ]; then
    export ADMIN_AUTH_DISABLE_CHECKS=true
    export ADMIN_AUTH_BEARER_TOKEN=false
  else
    export ADMIN_AUTH_DISABLE_CHECKS=false
    export ADMIN_AUTH_BEARER_TOKEN=true
  fi
}

COMMAND=$1
if [ ${#@} == 0 ]; then
    setupEnvVars 0
elif [ $COMMAND = "disable-auth" ]; then
    setupEnvVars 1
else
    echo "Usage: $0 [disable-auth]"
    exit 1
fi

./gradlew service:bootRun
