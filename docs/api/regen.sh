#!/bin/bash

wget -O api.json "http://localhost:8080/v2/api-docs?group=g4mify"
java -jar swagger2markup-cli-1.2.0.jar convert -i api.json -d build -c api.properties