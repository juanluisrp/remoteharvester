#!/bin/bash

if [ "$#" -ne 1 ] ; then
  echo "Usage: $0 harvesterId" >&2
  exit 1
fi

host=localhost
port=9999
harvesterId=$1


processId=$(curl -X POST "http://$host:$port/api/startIngest" -H "Content-Type: application/json"  -d '{"longTermTag":"$harvesterId"}' | sed -n '/ *"processID": *"/ { s///; s/".*//; p; }')

while sleep 1; 
do  
  curl -X GET "http://$host:$port/api/getstatus/$processId" -H "Content-Type: application/json";
  echo '\n\n';
done
