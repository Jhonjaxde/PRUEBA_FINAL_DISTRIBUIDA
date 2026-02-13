#docker build -f src/main/docker/Dockerfile -t jjarteaga/todo-service .
#docker push
#docker compose -f docker-compose-metricas.yml up -d
FROM eclipse-temurin:21.0.9_10-jre-alpine-3.23
ENV LANGUAGE='en_US:en'

RUN mkdir /app
WORKDIR /app
#COPIARTODO LO QUE SE GENERA WUARKUS APP DE LA MAQUINA PARA EL CONTENEDOR
#hacer el orden correcto de este el principio poner el menos que cambia por el mas que cambia
#ordenar siempre de menos que cambia al mas que cambia
#docker hub cambia la capa que cambia y mas rapido
COPY build/quarkus-app/lib/ ./lib
COPY build/quarkus-app/quarkus ./quarkus
COPY build/quarkus-app/app/ ./app
COPY build/quarkus-app/quarkus-run.jar app.jar

#
CMD ["java", "-jar", "app.jar"]

