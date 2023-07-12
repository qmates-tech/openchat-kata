# Open Chat Backend Kata in Typescript

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

To run all tests:

```
$ yarn test
```
