!#/bin/bash

processId=$(curl -X POST "http://localhost:9999/api/startIngest" -H "Content-Type: application/json"  -d '{"longTermTag":"MT"}' | sed -n '/ *"processID": *"/ { s///; s/".*//; p; }')

while sleep 1; 
do  
  curl -X GET "http://localhost:9999/api/getstatus/$processId" -H "Content-Type: application/json";
  echo '\n\n';
done
