FROM maven:3.8.4-openjdk-17

LABEL maintainer="Lea Laux <lea.laux@st.oth-regensburg.de>"

WORKDIR /studentrack
COPY . /studentrack

RUN mvn install:install-file -Dfile=terminportal-backend.jar -DgroupId=sw.helblingd -DartifactId=terminportal-backend -Dversion=0.0.1-SNAPSHOT -Dpackaging=jar -DgeneratePom=true


RUN mvn clean package -DskipTests

WORKDIR /studentrack/target

ENTRYPOINT ["java", "-jar", "Studentrack-0.0.1-SNAPSHOT.jar"]
