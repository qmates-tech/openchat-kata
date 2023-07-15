import Database, { SqliteError } from 'better-sqlite3';
import crypto from 'crypto';
import * as uuid from 'uuid';
import { RegisteredUser, UserToRegister } from "../domain/entities/User";
import UserRepository from "../domain/repositories/UserRepository";

export default class SQLiteUserRepository implements UserRepository {

  private readonly db

  constructor(filename: string) {
    this.db = new Database(filename)
  }

  store(user: UserToRegister): void {
    if (!uuid.validate(user.id) || uuid.version(user.id) != 4)
      throw new Error('Cannot store user, invalid v4 uuid id value.')

    let result: Database.RunResult;
    try {
      result = this.db
        .prepare('INSERT INTO users (id, username, password, about) VALUES (?, ?, ?, ?)')
        .run(
          user.id,
          user.username,
          crypto.createHash('sha256').update(user.password).digest('hex'),
          user.about
        )
    } catch (err: any) {
      if (err instanceof SqliteError && err.message === 'UNIQUE constraint failed: users.id')
        throw Error('Cannot store user, uuid value already used.')

      throw err
    }

    if (result.changes != 1)
      throw Error('Error during user store operation!')
  }

  isUsernameAlreadyUsed(usernameToFind: string): boolean {
    const result: any = this.db
      .prepare('SELECT count(*) as count FROM users WHERE username = ?')
      .get(usernameToFind)

    return result.count > 0
  }

  getAll(): RegisteredUser[] {
    const result: any[] = this.db.prepare('SELECT * FROM users').all()

    return result.map((u: any) => ({
      id: u.id,
      username: u.username,
      about: u.about,
    }))
  }

  getUserById(userId: string): RegisteredUser | null {
    const rows: any[] = this.db
      .prepare('SELECT * FROM users WHERE id = ?')
      .all(userId)

    if (rows.length === 0)
      return null

    const firstRow = rows[0]
    return {
      id: firstRow.id,
      username: firstRow.username,
      about: firstRow.about,
    }
  }

  reset(): void {
    this.db.exec('DELETE FROM users');
  }

}