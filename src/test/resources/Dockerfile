FROM ubuntu

RUN apt-key adv --keyserver hkp://p80.pool.sks-keyservers.net:80 --recv-keys B97B0AFCAA1A47F044F244A07FCC7D46ACCC4CF8 \
	&& echo "deb http://apt.postgresql.org/pub/repos/apt/ precise-pgdg main" > /etc/apt/sources.list.d/pgdg.list \
	&& apt-get update && apt-get install -y python-software-properties software-properties-common \
		postgresql-9.6 postgresql-client-9.6 postgresql-contrib-9.6

USER postgres
RUN    /etc/init.d/postgresql start &&\
    psql --command "CREATE USER sa WITH SUPERUSER LOGIN PASSWORD '123';" \
    && createdb -O sa reservation \
    && touch ~/.pgpass \
    && chmod 600 ~/.pgpass \
    && echo "*:*:*:sa:123" >> ~/.pgpass \
    && echo "host all  all    0.0.0.0/0  md5" >> /etc/postgresql/9.6/main/pg_hba.conf \
    && echo "listen_addresses='*'" >> /etc/postgresql/9.6/main/postgresql.conf

EXPOSE 5432

VOLUME ["/etc/postgresql", "/var/log/postgresql", "/var/lib/postgresql"]

CMD [
		"/usr/lib/postgresql/9.6/bin/postgres",
		"-D",
		"/var/lib/postgresql/9.6/main",
		"-c",
		"config_file=/etc/postgresql/9.6/main/postgresql.conf"
	]