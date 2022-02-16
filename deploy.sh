#!/usr/bin/env bash

if [[ -z "${VERSION}" ]]; then
  echo "\$VERSION variable not set. It should be like 2.0.0"
  echo "It can be set either export command or during deploy invocation:"
  echo "VERSION=XXX ./deploy.sh"
  exit 1
fi

mvn clean test && mvn versions:set -DnewVersion=${VERSION} && mvn deploy