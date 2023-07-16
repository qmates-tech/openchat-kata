# Open Chat Backend Kata in Java
Requirements:

- `openjdk-18.0.2` or later

> or just `docker` in alternative, see "Docker way" at the end

### Run it

Start the jetty server:

```
$ ./gradlew run
```

visit `http://localhost:8000`

### Create sqlite databases:

To create ready-to-use sqlite db files, use `createdb` node script:

```
$ ./gradlew createProductionDb createTestDb
```

### Run tests

To run unit tests:

```
$ ./gradlew test --tests 'acceptance*'
```

To run integration tests (remember to create the sqlite database first):

```
$ ./gradlew test --tests 'integration*'
```

To run all tests (remember to start the application first with `yarn start`):

```
$ ./gradlew test
```

## Docker way

If you do not have java installed in you machine, everything can be executed in a temporary container.

Start the temporary container on bash with:

```
$ docker run --rm -it -p 8000:8000 -v $PWD:/app -w /app --name openchat-backend java:todo bash
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

```
$ ./gradlew tests
```

## TODO

- [ ] move unit tests under a unit folder (?) -> and integration as well
- [ ] commands to run only unit, integrations, acceptance + readme
- [ ] docker way readme instructions
