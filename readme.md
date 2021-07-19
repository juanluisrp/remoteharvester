# CSW-harvester

## Intro

This service provides functionality to extract records from remote Catalog Service for the Web (CSW) interfaces providing iso19139 records.
Documents are stored in a PostGreSQL database. The history of previous harvest runs is persisted. You can set up a process to include records 
from federated catalogues, which are linked from records in the harvested catalogue.

Development of this program was sponsored by the European Commission - Joint Research Centre (JRC) within Service 
Contract NUMBER – 941143 – IPR – 2021 with subject matter "Facilitating a sustainable evolution and maintenance of 
the INSPIRE Geoportal", performed in the period 2021-2023.

Contact: 
	JRC Unit B.6 Digital Economy, 
    Via Enrico Fermi 2749, 21027 Ispra, Italy; 
	JRC-INSPIRE-SUPPORT@ec.europa.eu


## Methods

### POST /api/startharvest (url, longTermTag, [lookForNestedDiscoveryService])

Triggers a harvest process (returns ProcessID).

```
curl
  -X POST "http://localhost:9999/api/startHarvest" 
  -H "Content-Type: application/json"
  -d '{"url":"https://msdi.data.gov.mt/geonetwork/srv/eng/csw","longTermTag":"MT","lookForNestedDiscoveryService":false}'
```

### GET /api/getstatus/{ProcessID}

Retrieves the status of a running process.

```
curl "http://localhost:9999/api/getstatus/aaa-bbb-ccc"
```

###  GET /api/getlog/{ProcessID}

Retrieves the logs of a process.

```
curl "http://localhost:9999/api/getlog/aaa-bbb-ccc"
```

###  GET /api/abortprocess/{ProcessID}

Aborts a running process.

```
curl "http://localhost:9999/api/abortprocess/aaa-bbb-ccc"
```


## Installation

Deploy as a war on tomcat/jetty.
Software requires ActiveMQ and PostGreSQL. 

## Configuration

Configuration goes in the config.properties file, or using environment variables.

## License

This software is made available under GPLv2
