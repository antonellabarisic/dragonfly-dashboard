FROM ros:foxy

ENV NVIDIA_VISIBLE_DEVICES \
    ${NVIDIA_VISIBLE_DEVICES:-all}
ENV NVIDIA_DRIVER_CAPABILITIES \
    ${NVIDIA_DRIVER_CAPABILITIES:+$NVIDIA_DRIVER_CAPABILITIES,}graphics

ENV DEBIAN_FRONTEND noninteractive
ENV WORKSPACE=/workspace

# Install dependencies
RUN apt-get -qq update && \
    apt-get -qq upgrade && \
    apt-get -qq install apt-utils build-essential vim software-properties-common

# Install Java
RUN add-apt-repository ppa:linuxuprising/java && apt-get -qq update && \
	echo "oracle-java11-installer-local shared/accepted-oracle-license-v1-2 select true" | debconf-set-selections

RUN mkdir -p /var/cache/oracle-jdk11-installer-local/
COPY docker/jdk-11.0.*_linux-x64_bin.tar.gz /var/cache/oracle-jdk11-installer-local/

RUN apt-get -qq install ros-foxy-mavros oracle-java11-installer-local openjfx unzip

RUN rm /bin/sh && ln -s /bin/bash /bin/sh

# Install ROS Bridge
RUN apt-get -qq install ros-foxy-rosbridge-server

# Clear apt cache to save on space
RUN rm -rf /var/lib/apt/lists/*

# Setup arcgis workspace
ENV ARCGIS_VERSION=100.10.0
RUN mkdir -p /root/.arcgis/$ARCGIS_VERSION/
COPY ./docker/arcgis-java-jnilibs-$ARCGIS_VERSION.zip /tmp/

RUN echo "Xft.dpi: 192" > ~/.Xdefaults

RUN unzip /tmp/arcgis-java-jnilibs-$ARCGIS_VERSION.zip -d /root/.arcgis/$ARCGIS_VERSION/ \
    && rm /tmp/arcgis-java-jnilibs-$ARCGIS_VERSION.zip

COPY ./docker/arcgis-java-resources-$ARCGIS_VERSION.zip /tmp/

RUN unzip /tmp/arcgis-java-resources-$ARCGIS_VERSION.zip -d /root/.arcgis/$ARCGIS_VERSION/ \
	&& rm /tmp/arcgis-java-resources-$ARCGIS_VERSION.zip

# Install java_rosbridge snapshot
ENV JAVA_ROSBRIDGE_DIR=edu/brown/cs/burlap/java_rosbridge
ENV JAVA_ROSBRIDGE_VERSION=2.0.3-SNAPSHOT
RUN mkdir -p /root/.m2/repository/$JAVA_ROSBRIDGE_DIR/$JAVA_ROSBRIDGE_VERSION/
COPY ./docker/*.jar /root/.m2/repository/$JAVA_ROSBRIDGE_DIR/$JAVA_ROSBRIDGE_VERSION/
COPY ./docker/*.pom /root/.m2/repository/$JAVA_ROSBRIDGE_DIR/$JAVA_ROSBRIDGE_VERSION/

# Setup Gradle - hoping this aids performance
RUN mkdir -p $WORKSPACE/gradle/wrapper
ADD build.gradle $WORKSPACE
ADD gradlew $WORKSPACE
ADD gradle/wrapper/* $WORKSPACE/gradle/wrapper/

RUN cd $WORKSPACE && ./gradlew clean

COPY . $WORKSPACE

# Install dragonfly infrastructure
RUN source /opt/ros/foxy/setup.bash && \
	cd $WORKSPACE/dragonfly && \
	colcon build

# Install dragonfly desktop

RUN cd $WORKSPACE && \
	./gradlew build --parallel

# Setup Entrypoint
COPY ./docker/entrypoint.sh /
ENTRYPOINT ["/entrypoint.sh"]

CMD ["bash"]