# Open Chat Backend Kata in Java
Requirements:

- `openjdk-18.0.2` or newer

> or just `docker` in alternative, see "Docker way" at the end

### Run it

Start the jetty server:

```
$ ./gradlew run
```

visit `http://localhost:8000`

### Create sqlite databases:

To create ready-to-use sqlite db files, use the dedicated gradle tasks:

```
$ ./gradlew createProductionDb createTestDb
```

### Run tests

To run unit tests:

```
$ ./gradlew test --tests 'unit*'
```

To run integration tests (remember to create the sqlite database first):

```
$ ./gradlew test --tests 'integration*'
```

To run all tests (remember to start the application first with `./gradlew run`):

```
$ ./gradlew test
```

## Docker way

If you do not have java installed in you machine, everything can be executed in a temporary container.

Start the temporary container on bash with:

```
$ docker run --rm -it -p 8000:8000 -v $PWD:/app -w /app --name openchat-backend openjdk:18.0-slim bash
root@620af4e91dbf:/app#
```

Here you can execute setup commands normally as described above:

```
# ./gradlew run
# ./gradlew create....
```

To open a new bash session in the same temporary container:

```
$ docker exec -it openchat-backend bash
```
