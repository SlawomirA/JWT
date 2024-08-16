# Wybierz obraz bazowy z JDK 21
FROM openjdk:21-jdk-alpine AS build

# Ustaw katalog roboczy
WORKDIR /app

# Skopiuj plik pom.xml i pliki z katalogu .mvn
COPY pom.xml ./
COPY .mvn .mvn

# Pobierz zależności Mavena (szybsze budowanie, jeśli nie zmieniają się zależności)
RUN mvn dependency:go-offline

# Skopiuj kod źródłowy aplikacji
COPY src src

# Zbuduj aplikację
RUN mvn clean package -DskipTests

# Użyj mniejszego obrazu JDK do uruchomienia aplikacji
FROM openjdk:21-jdk-alpine

# Ustaw katalog roboczy
WORKDIR /app

# Skopiuj plik JAR z poprzedniego etapu budowy
COPY --from=build /app/target/*.jar app.jar

# Otwórz port, na którym działa aplikacja
EXPOSE 8082

# Przyjmij nazwę aplikacji i datę jako zmienne środowiskowe
ARG PROJECT_NAME
ENV PROJECT_NAME=${PROJECT_NAME}
ENV BUILD_DATE=${BUILD_DATE}

# Ustaw polecenie do uruchomienia aplikacji
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
