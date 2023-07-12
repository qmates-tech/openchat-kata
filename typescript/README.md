# Open Chat Backend Kata in Typescript

Requirements:

- `node 16.18` or later

> or just `docker` in alternative, see "Docker way" at the end

### Run it

```
$ yarn install
$ yarn tsc
$ yarn start
```

visit `http://localhost:8000`

> remember to enable yarn if needed: `corepack enable`

### Create sqlite databases:

To create ready-to-use sqlite db files, use `createdb` node script:

```
$ yarn tsc
$ node ./dist/src/database/createdb.js src/database/production.db
$ node ./dist/src/database/createdb.js tests/integration/database/integration.test.db
```

or simpler via sh script:

```
$ ./createdbs.sh
```

### Run tests

To run unit tests:

```
$ yarn test tests/unit
```

To run integration tests (remember to create the sqlite database first):

```
$ yarn test tests/integration
```

To run all tests (remember to start the application first with `yarn start`):

```
$ yarn test
```

## Docker way

If you do not have node installed in you machine, everything can be executed in a temporary container.

Start the temporary container on bash with:

```
$ docker run --rm -it -p 8000:8000 -v $PWD:/app -w /app --name openchat-backend node:16.18 bash
root@620af4e91dbf:/app#
```

Here you can execute setup commands normally as described above:

```
# yarn install
# yarn ...
```

To open a new bash tab in the same container temporary:

```
$ docker exec -it openchat-backend bash
```
