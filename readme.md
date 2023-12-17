Improvements:
- Use BigDecimal instead of double/float to calculate
- Store interest in the database as a DECIMAL type
- Respond with 404 instead of 500 in case you type an invalid loan type

To run it, open the terminal and change directory to this task, and then:
1. Compile with gradle: `./gradlew build`
2. Run the application: `java -jar build/libs/kontek-task-0.0.1-SNAPSHOT.jar`
3. Head to http://localhost:8080/swagger-ui/index.html