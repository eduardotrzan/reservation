# Reservation

This is a Reservation Rest Service.

## Local use

### Creating a database

Pre-requirement: Have Postgres 9.6 installed.

Run in terminal: `createdb -h localhost -p 5432 -U root reservation`

### Running application (one of the following)

#### Pre-requirement: 
- Have Java 9 installed;
- Make sure to add `--add-modules java.xml.bind` as VM Options, otherwise you'll have `JAXBException` (This step is only needed if `jaxb-api` is removed from pom.xml).

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
- [ ] Prepare H2 or TestContainers for Integration Tests
- [x] Named Queries migration to DAO and create a Booking with fetch on Guest+Room to avoid N+1
- [x] Docker
- [ ] Deployment


### Performance and Scalability

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
	
Materialized Views
It's a table which would contain the information pulled by a query. The good part of having it is that high demanding queries that 
rely in different aggregations could be improved by having it's data stored in a table where it's content reflects the needed query.
Materialized views can be indexed and refreshed. This approach goes well with Command Query Responsibility Segregation (CQRS), where
different actions (Create, Update, Delete) goes in a different flow from the Read.

On the hand, a high demanding insert scenario would for the materialized view to be refreshed with frequency which would increase the load
of the DB for each time it is refreshed. Also in a sharded environment, the materialized view wouldn't cover all data.


#### Memcached / Redis
In-memory caching could improve search for availability, avoiding hitting the DB too many times.
They could scale the in the same way as the points above for the DB.

It's important to avoid state during horizontal scaling of machine depending on then. The best way is to have In-memory detached
from the running application, eg, different docker containers of EC2 machines. The biggest concern is to avoid stateful situations
were data changes in one environment and it's outdated for another. 


#### Kafka / RabbitMQ
Another option for scaling would be to split the domain of the application creating 2 or more micro-service (MS). Supposing that user and/or
team management becomes a detached micro-service (MS) from the reservation and the reserve functionality can create an user while reserving, then:

It would be possible to create an async logic were a temporary reservation is created in a pending state, while a message is published in a 
messaging bus. The MS responsible for registering the user would process the request and acknowledge the reservation to properly 
finish it. This would avoid the 2PC (Two Phase Commit) where a MS commits changes in a DB that is not it's own.

Some important points on this matter are related to unavailability of service for any possible case. The registration happen on the first MS,
but for some reason it couldn't reach the second MS or the message queue. Therefore, it would be important to have a retrial job to enforce
the communication. 

Another scenario would be the fact the second MS was able to receive and not process the request. Depending on the type of the error, a 
retry job would make sense, some other an async acknowledge would be required to inform the first MS, which would be responsible to apply a
failure flow.

An important pattern would for avoiding inconsistencies would be Event Sourcing, which consists on storing the sequence of the change events as 
it arrived in the system. It is usually applied with CQRS, where create, update and deletes are tracked.

Even though the careful validation flows a data inconsistency could happen. Supposing a scenario were we have an overbooking, it would mean
that both request for the same period (or overlapping periods) were successfully processed. In those cases, the solution would be more reactive,
than protective, which would mean that good monitoring tools (or data consistency checks) would identify similar scenarios and apply an automatic
or expect manual resolution for the conflicts.

The importance of the flow is to avoid `Distributed Transaction`, where the services rely on the availability of the environment.

To scale Kafka, it's possible to cluster it and the replication would happen with a Leader (original topic) and follower (copy of original). 
A problem that could happen is related to the latency of replication, as the data is considered committed only when all brokers have the 
data in sync. This could represent a bottleneck in cases where the replication is stuck.

The structure of Leader and Followers, allow the consistency of input and the source of truth on the data. In case a Leader collapse, a Follower
is elected to become the next leader. Once the original Leader is back online, Kafka will restore the data and pass the control back to it.


#### Other Micro-Services Challenges
Micro-service is not only a self standing block of code, but a business boundary. It's often associated to Domain Driven Design, where the effort
is properly identify the the domain of a problem you want to solve, defining clear constraints and concepts. As business grow, different visions are
incorporated to the original plan, which with time question the domain design, culminating in a re-design of the solution where boundaries might not
be clear enough.

Reporting could become a complex Frankenstein where different pieces will be assembled from different scenarios. SQL Aggregations won't be available
and logic might need to be duplicated in order to interpret the data in the needed context.

Monitoring is not only about DB, but the behavior and production of each MS. While issues for concurrency can be challenging, producing errors
from on MS as events are created can be complicated to debug and screening distributed logs. 

Cyclic dependencies between services impose barriers while deploying MSs where changes in one could deeply affect others. This could mean a crash
on the system availability or a cascade change on all dependencies. Common libs without retro-compatibility is another case where changes could 
affect MSs in a chain. 



  
