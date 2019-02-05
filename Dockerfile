FROM tomcat:9.0.14-jre8-alpine
RUN rm -rf /usr/local/tomcat/webapps/*
EXPOSE 8080:8080
COPY ./troparo-web/target/troparo-web.war /usr/local/tomcat/webapps/troparo-web.war
CMD ["catalina.sh","run"]