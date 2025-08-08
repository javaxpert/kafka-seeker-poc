FROM openjdk:21
ADD ./build/libs/template-java-project*.jar template-java-project.jar
ENTRYPOINT java -jar ./template-java-project.jar