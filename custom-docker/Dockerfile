FROM docker.devops.intellectivelab.com/unity-classic/unity-7:7.6.1-ol
RUN rm -Rf /opt/ol/wlp/usr/servers/defaultServer/apps/unity.ear
COPY --chown=1001:0 ./target/custom-webapp.war /opt/ol/wlp/usr/servers/defaultServer/apps/custom-webapp.war
COPY --chown=1001:0 ./target/config/liberty/server.xml /opt/vu/server.xml
COPY --chown=1001:0 ./target/config/unity/ /opt/vu/