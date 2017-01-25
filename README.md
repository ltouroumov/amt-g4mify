# G4mify - Gamification Platform

This platform allows clients to [gamify](https://en.wikipedia.org/wiki/Gamification) applications using simple API calls.

## Documentation

  * [API Getting Started](docs/api.md)
  * [API Documentation](docs/api/build/paths.md)
  * [API Definitions](docs/api/build/definitions.md)
  * [Domain Model](docs/models.md)
  * [Rules Syntax](docs/rules.md)

## Building

The project uses the [gradle build system](https://gradle.org).

```
# Move to the api directory
cd api
# Generate artifacts
./gradlew build
```

## Running

The project provides docker and docker-compose support for running the API. If you want to use the api only, simply build the docker image from `api/Dockerfile` and provide a link to a database accessible via `postgres-db` with a database named `kwizz`. The application uses `kwizz` as both username and password for access, these setting can be changed in `res/main/application.properties`.

Otherwise simply run `docker-compose up db` wait a few seconds for postgres to initialize the database then run `docker-compose up api`. The api is now available at `http://localhost:8080` (configurable in `docker-compose.yml`).