FROM tomcat:9.0.14-jre8-alpine
RUN rm -rf /usr/local/tomcat/webapps/*
COPY ./troparo-web/target/troparo.war /usr/local/tomcat/webapps/troparo.war
CMD ["catalina.sh","run"]