FROM tomcat:9.0.14-jre8-alpine
RUN rm -rf /usr/local/tomcat/webapps/*
COPY ./troparo-web/target/troparo-web-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/troparo-web-1.0-SNAPSHOT.war
CMD ["catalina.sh","run"]