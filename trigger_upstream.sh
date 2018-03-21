#!/bin/bash

build_info=$(curl "https://circleci.com/api/v1.1/project/github/CueNinja/maven-repo?circle-token=${CI_TOKEN}&limit=1&filter=completed")
build_num=$(echo $build_info | jq ". [0] .build_num")
echo "Last build was ${build_num}"
curl -X POST "https://circleci.com/api/v1.1/project/github/CueNinja/maven-repo/${build_num}/retry?circle-token=${CI_TOKEN}"