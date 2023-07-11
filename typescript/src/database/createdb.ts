import fs from 'fs';
import Database from 'better-sqlite3';

if (process.argv.length === 2) {
    console.error('ERROR! Usage: node createdb.js <filepath>');
    process.exit(1);
  }

const dbFilename = process.argv[2]
const db = new Database(dbFilename)

const dbSchemaFilename = 'src/database/schema.sql'
const dbSchemaSql = fs.readFileSync(dbSchemaFilename, 'utf8');
db.exec(dbSchemaSql);