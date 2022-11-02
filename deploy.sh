#!/bin/bash

function buildJarFiles() {
  ./gradlew bootJar
}

function dockerDeployAll() {
  docker-compose \
    -f compose/docker-compose-services.yml \
    -f compose/docker-compose-cloud.yml \
    -f compose/docker-compose-user.yml \
    -f compose/docker-compose-mail.yml \
    up -d --build
}

function dockerBuild() {
  docker-compose \
    -f compose/docker-compose-services.yml \
    -f compose/docker-compose-cloud.yml \
    -f compose/docker-compose-user.yml \
    -f compose/docker-compose-mail.yml \
    build
}

case $1 in

network)
  case $2 in
  create)
    docker network create orderup &&
      echo "created orderup network"
    ;;
  down)
    docker network rm orderup &&
      echo "removed docker network"
    ;;
  *)
    echo "create (creates a docker network named \"orderup\")"
    echo "down (removes a docker network named \"orderup\")"
    ;;
  esac
  ;;

build)
  case $2 in
  jars)
    echo "building jar files..."
    buildJarFiles
    ;;
  docker)
    echo "building docker containers..."
    dockerBuild
    ;;
  *)
    echo "jars (builds all relevant jar files)"
    echo "docker (builds all docker images)"
    ;;
  esac
  ;;

deploy)
  case $2 in
  all)
    buildJarFiles && dockerBuild && dockerDeployAll
    ;;
  keycloak)
    echo "building keycloak server..."
    ;;
  user)
    docker-compose \
      -f compose/docker-compose-services.yml \
      -f compose/docker-compose-cloud.yml \
      -f compose/docker-compose-user.yml \
      up -d --build
    ;;
  mail)
    docker-compose \
      -f compose/docker-compose-services.yml \
      -f compose/docker-compose-cloud.yml \
      -f compose/docker-compose-user.yml \
      -f compose/docker-compose-mail.yml \
      up -d --build
    ;;
  *)
    echo "I'm not sure what you want to deploy"
    ;;
  esac
  ;;

status)
  docker-compose \
    -f compose/docker-compose-services.yml \
    -f compose/docker-compose-cloud.yml \
    -f compose/docker-compose-user.yml \
    -f compose/docker-compose-monitoring.yml \
    -f compose/docker-compose-mail.yml ps
  ;;

stop)
  # shellcheck disable=SC2046
  docker stop $(docker ps -aq --filter name="$2")
  ;;

clear)
  # shellcheck disable=SC2046
  docker stop $(docker ps -aq --filter name="$2")
  # shellcheck disable=SC2046
  docker rm $(docker ps -aq --filter name="$2")
  ;;

*)
  echo "unknown command"
  ;;

esac
