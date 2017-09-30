FROM maven:3.5.0-ibmjava-9

RUN apt-key adv --keyserver hkp://p80.pool.sks-keyservers.net:80 --recv-keys B97B0AFCAA1A47F044F244A07FCC7D46ACCC4CF8 \
    && echo "deb http://apt.postgresql.org/pub/repos/apt/ precise-pgdg main" > /etc/apt/sources.list.d/pgdg.list \
    && apt-get update \
    && apt-get install -y vim curl wget git software-properties-common \
        postgresql-9.6 postgresql-client-9.6 postgresql-contrib-9.6 postgresql-server-dev-9.6 \
        libmemcached-dev zlib1g-dev libicu-dev libpng-dev \
        sudo \
    && mkdir /docker && touch /docker/debug.log \
    && chpasswd && adduser postgres sudo \
    && echo "postgres ALL=(ALL) NOPASSWD: ALL" >> /etc/sudoers \
    && apt-get remove -y --purge software-properties-common \
    && apt-get -y autoremove \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/* \
    && mkdir /opt/reservation \
    && mkdir /opt/logs \
    && mkdir /opt/scripts \
    && touch /opt/logs/reservation.log \
    && update-rc.d postgresql enable

COPY ./run.sh /opt/scripts/run.sh
RUN sudo chmod 777 /opt/scripts/run.sh && sudo ln -sf /usr/share/zoneinfo/EST /etc/localtime

USER postgres
RUN sudo chmod 666 /opt/logs/reservation.log \
    && service postgresql start \
    && psql --command "CREATE USER root WITH SUPERUSER LOGIN PASSWORD 'root';" \
    && createdb -O root reservation \
    && echo "host all  all    0.0.0.0/0  md5" >> /etc/postgresql/9.6/main/pg_hba.conf \
    && echo "listen_addresses='*'" >> /etc/postgresql/9.6/main/postgresql.conf \
    && touch ~/.pgpass \
    && chmod 600 ~/.pgpass \
    && echo "*:*:*:root:root" >> ~/.pgpass

EXPOSE 5432
EXPOSE 8383

CMD ["/opt/scripts/run.sh"]