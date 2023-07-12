import Database from 'better-sqlite3';
import fs from 'fs';

if (process.argv.length === 2) {
  console.error('ERROR! Usage: node createdb.js <filepath>')
  process.exit(1)
}

const dbFilename = process.argv[2]
console.log('Creating database file ' + dbFilename + ' ...')

const db = new Database(dbFilename)
const dbSchemaFilename = 'src/database/schema.sql'
const dbSchemaSql = fs.readFileSync(dbSchemaFilename, 'utf8')
db.exec(dbSchemaSql)

console.log('Succesfully created!')
