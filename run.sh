#!/bin/bash
cd /opt/reservation \
	&& service postgresql start \
	&& mvn install \
	&& mvn spring-boot:run \
	&& tail -f -n 1500 /opt/logs/reservation.log