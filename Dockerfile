FROM maven:3.8.4-openjdk-17

LABEL maintainer="Lea Laux <lea.laux@st.oth-regensburg.de>"

WORKDIR /studentrack
COPY . /studentrack

RUN mvn clean package -DskipTests

WORKDIR /studentrack/target

ENTRYPOINT ["java", "-jar", "Studentrack-0.0.1-SNAPSHOT.jar"]
