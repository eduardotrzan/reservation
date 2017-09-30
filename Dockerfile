FROM ubuntu

# Versions
ARG MAVEN_VERSION=3.5.0
ARG MAVEN_BASE_URL=http://apache.claz.org/maven/maven-3
ARG MAVEN_URL=${MAVEN_BASE_URL}/${MAVEN_VERSION}/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz
ARG SHA=beb91419245395bd69a4a6edad5ca3ec1a8b64e41457672dc687c173a495f034

RUN apt-key adv --keyserver hkp://p80.pool.sks-keyservers.net:80 --recv-keys B97B0AFCAA1A47F044F244A07FCC7D46ACCC4CF8 \
    && echo "deb http://apt.postgresql.org/pub/repos/apt/ precise-pgdg main" > /etc/apt/sources.list.d/pgdg.list \
    && echo "deb http://ppa.launchpad.net/webupd8team/java/ubuntu xenial main" | tee /etc/apt/sources.list.d/webupd8team-java.list \
    && echo "deb-src http://ppa.launchpad.net/webupd8team/java/ubuntu xenial main" | tee -a /etc/apt/sources.list.d/webupd8team-java.list \
    && apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys EEA14886 \
    && apt-get update \
    && echo debconf shared/accepted-oracle-license-v1-1 select true | debconf-set-selections \
    && apt-get install -y oracle-java9-set-default \
    && echo 'JAVA_HOME=/usr/lib/jvm/java-9-oracle/' >> /etc/bash.bashrc \
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
    && update-rc.d postgresql enable \
    && echo ${MAVEN_URL} \
    && mkdir -p /usr/share/maven /usr/share/maven/ref \
    && curl -fsSL -o /tmp/apache-maven.tar.gz ${MAVEN_URL} \
    && echo "${SHA}  /tmp/apache-maven.tar.gz" | sha256sum -c - \
    && tar -xzf /tmp/apache-maven.tar.gz -C /usr/share/maven --strip-components=1 \
    && rm -f /tmp/apache-maven.tar.gz \
    && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

COPY ./run.sh /opt/scripts/run.sh
RUN chmod -R 777 /opt/reservation /opt/logs /opt/scripts \
	&& ln -sf /usr/share/zoneinfo/EST /etc/localtime

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