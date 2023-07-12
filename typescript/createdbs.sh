#!/bin/sh

yarn tsc
node ./dist/src/database/createdb.js src/database/production.db
node ./dist/src/database/createdb.js tests/integration/database/integration.test.db