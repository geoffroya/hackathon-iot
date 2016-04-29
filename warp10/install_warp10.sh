#!/bin/bash

# Install WARP10 - completly inspired from official DockerFile


# Updating apk index
#RUN apk update && apk add bash curl python


# Installing build-dependencies
#RUN apk add --virtual=build-dependencies ca-certificates wget

#ENV WARP10_VERSION=1.0.5
WARP10_VERSION=1.0.5

# Getting warp10
#RUN mkdir /opt \
#  && cd /opt \
#  && wget https://bintray.com/artifact/download/cityzendata/generic/io/warp10/warp10/$WARP10_VERSION/warp10-$WARP10_VERSION.gz \
#  && tar xzf warp10-$WARP10_VERSION.gz \
#  && rm warp10-$WARP10_VERSION.gz \
#  && ln -s  /opt/warp10-$WARP10_VERSION /opt/warp10

cd /opt
wget https://bintray.com/artifact/download/cityzendata/generic/io/warp10/warp10/$WARP10_VERSION/warp10-$WARP10_VERSION.gz
tar xzf warp10-$WARP10_VERSION.gz
rm warp10-$WARP10_VERSION.gz
ln -s  /opt/warp10-$WARP10_VERSION /opt/warp10

#ENV SENSISION_VERSION=1.0.4
SENSISION_VERSION=1.0.4

# Getting Sensision
#RUN cd /opt \
#    # && wget https://dl.bintray.com/cityzendata/generic/sensision-service-$SENSISION_VERSION.tar.gz \
#    && curl -L https://dl.bintray.com/cityzendata/generic/sensision-service-$SENSISION_VERSION.tar.gz -o ./sensision-service-$SENSISION_VERSION.tar.gz \
#    && tar xzf sensision-service-$SENSISION_VERSION.tar.gz \
#    && rm sensision-service-$SENSISION_VERSION.tar.gz \
#    && ln -s  /opt/sensision-$SENSISION_VERSION /opt/sensision

cd /opt
wget  https://dl.bintray.com/cityzendata/generic/sensision-service-$SENSISION_VERSION.tar.gz
tar xzf sensision-service-$SENSISION_VERSION.tar.gz
rm sensision-service-$SENSISION_VERSION.tar.gz 
ln -s  /opt/sensision-$SENSISION_VERSION /opt/sensision



# Deleting build-dependencies
#RUN apk del build-dependencies

#ENV QUANTUM_VERSION=1.0.12
QUANTUM_VERSION=1.0.12
# Getting quantum
#RUN cd /opt \
#    # && wget https://github.com/cityzendata/warp10-quantum/archive/$QUANTUM_VERSION.tar.gz -O ./warp10-quantum-$QUANTUM_VERSION.tar.gz \
#    && curl -L https://github.com/cityzendata/warp10-quantum/archive/$QUANTUM_VERSION.tar.gz -o ./warp10-quantum-$QUANTUM_VERSION.tar.gz  \
#    && tar xzf warp10-quantum-$QUANTUM_VERSION.tar.gz \
#    && rm warp10-quantum-$QUANTUM_VERSION.tar.gz \
#    && ln -s /opt/warp10-quantum-$QUANTUM_VERSION /opt/quantum

cd /opt
#curl -L https://github.com/cityzendata/warp10-quantum/archive/$QUANTUM_VERSION.tar.gz -o ./warp10-quantum-$QUANTUM_VERSION.tar.gz
wget https://github.com/cityzendata/warp10-quantum/archive/$QUANTUM_VERSION.tar.gz
tar xzf warp10-quantum-$QUANTUM_VERSION.tar.gz
rm warp10-quantum-$QUANTUM_VERSION.tar.gz
ln -s /opt/warp10-quantum-$QUANTUM_VERSION /opt/quantum

#ENV JAVA_HOME=/usr \
#  WARP10_HOME=/opt/warp10-${WARP10_VERSION} SENSISION_HOME=/opt/sensision-${SENSISION_VERSION} \
#  WARP10_VOLUME=/data MAX_LONG=3153600000000

JAVA_HOME=/usr
WARP10_HOME=/opt/warp10-${WARP10_VERSION}
SENSISION_HOME=/opt/sensision-${SENSISION_VERSION}
WARP10_VOLUME=/var/lib/warp10
MAX_LONG=3153600000000

mkdir -p $WARP10_VOLUME

#ENV WARP10_JAR=${WARP10_HOME}/bin/warp10-${WARP10_VERSION}.jar \
#  WARP10_CONF=${WARP10_HOME}/etc/conf-standalone.conf

WARP10_JAR=${WARP10_HOME}/bin/warp10-${WARP10_VERSION}.jar
WARP10_CONF=${WARP10_HOME}/etc/conf-standalone.conf


#COPY warp10.start.sh ${WARP10_HOME}/bin/warp10.start.sh
#COPY worf.sh ${WARP10_HOME}/bin/worf.sh
#COPY bashrc /root/.bashrc

# A VOIR
#warp10.start.sh ${WARP10_HOME}/bin/warp10.start.sh
#worf.sh ${WARP10_HOME}/bin/worf.sh
#bashrc /root/.bashrc

#RUN chmod +x ${WARP10_HOME}/bin/*.sh

#ENV PATH=$PATH:${WARP10_HOME}/bin

#VOLUME ${WARP10_VOLUME}

# Exposing port 8080
#EXPOSE 8080 8081

#CMD ${WARP10_HOME}/bin/warp10.start.sh
