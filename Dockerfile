FROM tomcat:9.0.14-jre8
RUN rm -rf /usr/local/tomcat/webapps/*
COPY ./troparo-web/target/library-webservice.war /usr/local/tomcat/webapps/library-webservice.war
COPY ./troparo-technical/src/main/resources/* /usr/app/resources/
CMD ["catalina.sh","run"]