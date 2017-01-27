FROM cwds/javajdk
RUN mkdir /opt/perry
RUN mkdir /opt/perry/logs
ADD config/perry.yml /opt/perry/perry.yml
ADD config/shiro.ini /opt/perry/config/shiro.ini
ADD build/libs/perry-dist.jar /opt/perry/perry.jar
EXPOSE 8080
WORKDIR /opt/perry
CMD ["java", "-jar", "perry.jar","server","perry.yml"]
