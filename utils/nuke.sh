#!/bin/bash

dir=$(dirname $0)
psql -d g4mify < $dir/nuke.sql