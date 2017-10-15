# Reservation

This is a Reservation Rest Service.

## Local use

### Creating a database

Pre-requirement: Have Postgres 9.6 installed.

Run in terminal: `createdb -h localhost -p 5432 -U root reservation`

### Running application (one of the following)

#### Pre-requirement: 
- Have Java 9 installed;
- Make sure to add `--add-modules java.xml.bind` as VM Options, otherwise you'll have `JAXBException`.

#### Run Options
1 - mvn spring-boot:run
    
2 - run as java application the class: Application.java

##### Docker

###### Run Docker compose 
docker-compose build && docker-compose up

###### Accessing Container's bash
docker exec -ti reservation-ms /bin/bash

## Using the Application
- Download [Postman](https://www.getpostman.com/);
- Import Postman collections from `~/Documentation/API/RES-DOC-ApiCalls.postman_collection.json`;
- Configure your hostname for where the service is running;
- Use the Valid and Invalid calls to use the system.

## Testing the system
- In the root folder run `mvn test` and it will run all available tests;

## Technical Tips/Points
- Remember to add quotes on entity names, as keywords might lead to errors while running automatic queries. Ex.: User entity.
- Using UUID.randomUUID().toString() for the Booking ref, according to what I've found, version 4 of UUID, the risk of collision is 1 in 103 trillion.
- Check the folder Documentation/API to find some sample API Calls.
- The server runs in http://localhost:8484, check application.yml for more details.
- If running using Docker the server will be in http://localhost:8585, check docker-compose.yml for more details.


### Functionalities

- [x] Check availability
- [x] Make a reservation
- [x] Modify a reservation
- [x] Cancel a reservation
- [x] Find all (reference only)
- [x] Integration Test Cases (API) {Still some work and more coverage, but Postman folder contains success and failure calls}
- [x] Fix maven spring boot run
- [x] Integrate Unit Test Cases - Unit Tests for Code and API
- [ ] Increase Unit Test Coverage + More business related tests
- [ ] Prepare H2 in memory for Integration Tests
- [ ] Named Queries migration to DAO and create a Booking with fetch on Guest+Room to avoid N+1
- [x] Docker
- [ ] Deployment


### Performance
#### Database
The main point of issues on an environment with thousands of request for reservation falls on booking and more specific,
UUID, START DATE and END DATE.

Some solutions:
1. UNIQUE index on UUID for insertion
	* This will allow the DB to handle new Booking rows without having to scan table for duplicated;
2. INDEX on UUID
	* This will speed the FIND BY a Booking UUID;
3. INDEX on START DATE and END DATE
	* This will speed the FIND availability without UUID; 
4. INDEX on UUID, START DATE, END DATE
	* This will speed the FIND availability with UUID;
5. CONSTRAINT for date range on START DATE and END DATE
	* This will allow the DB to handle new Booking rows without having to scan table for duplicated;

Other ways to improve performance on DB could be:
* Replication
	* This would mean a full duplication of the DB in a master-slave format.
	* Downside, master gets overloaded with lots of writes, therefore slows down replicas.
* Clustering a table on index `CLUSTER booking USING booking_idx01;`
	* The good side is that this will improve the speed of readings in a table were data doesn't change too often. The
	reason is related to the fact cluster organizes data ordered by index, therefore allow B-Tree search;
	* On the other hand, cluster takes a toll on highly insertable tables, as decreases "wiggle room" for HOT (Heap-only tuples).
	It is possible also to happen Last Page Insert Latch Contention, which is a single place at the end of B-Tree, where multiple 
	queries compete against each other
* Partition: 
	* which would be a type of vertical partition. On this we split columns of a given table into 2 or more, depending on the need.
	This allows for example search from UUID not be impacted by the availability.
	* The not so good, the data will grow independently, but for 2 different tables. Queries adds more cost on joins.
* Sharding:
	* which consists in horizontal partition. We splint the rows of a given table in multiple nodes. This allows specific,
	information per region/node.
	* Problems from vertical partition might happen. Different region/node creates complexity on joins and aggregations.
	
#### Memcached / Redis
In-memory caching could improve search for availability, avoiding hitting the DB too many times.
They could scale the in the same way as the points above for the DB.

It's important to avoid state during horizontal scaling of machine depending on then. The best way is to have In-memory detached
from the running application, eg, different docker containers of EC2 machines. The biggest concern is to avoid stateful situations
were data changes in one environment and it's outdated for another. 

#### Kafka



