FROM openjdk:19

EXPOSE 8080

ADD backend/target/Stable-Buddy.jar Stable-Buddy.jar

CMD ["sh", "-c", "java -jar Stable-Buddy.jar --spring.data.mongodb.uri=$MONGO_DB_URI"]