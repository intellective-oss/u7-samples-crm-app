FROM registry.intellective.com/unity/unity:7.7.0.1
RUN rm -Rf /opt/ol/wlp/usr/servers/defaultServer/apps/unity.ear
COPY --chown=1001:0 ./target/custom-webapp.war /opt/ol/wlp/usr/servers/defaultServer/apps/custom-webapp.war
COPY --chown=1001:0 ./target/config/liberty/server.xml /opt/vu/server.xml
COPY --chown=1001:0 ./target/config/unity/ /opt/vu/