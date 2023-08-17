import Database from 'better-sqlite3';
import * as uuid from 'uuid';
import Post from '../domain/entities/Post';
import PostRepository from "../domain/repositories/PostRepository";

export default class SQLitePostRepository implements PostRepository {

  private readonly db

  constructor(filename: string) {
    this.db = new Database(filename)
  }

  store(post: Post): void {
    if (!uuid.validate(post.id) || uuid.version(post.id) != 4)
      throw new Error('Cannot store post, invalid v4 uuid id value.')

    let result: Database.RunResult;
    try {
      result = this.db
        .prepare('INSERT INTO posts (id, userId, text, dateTime) VALUES (?, ?, ?, ?)')
        .run(
          post.id,
          post.userId,
          post.text,
          post.dateTime.toISOString()
        )
    } catch (err: any) {
      if (err.name === 'SqliteError' && err.code === 'SQLITE_CONSTRAINT_PRIMARYKEY')
        throw Error('Cannot store post, uuid value already used.')

      throw err
    }

    if (result.changes != 1)
      throw Error('Error during user store operation!')
  }

  getAllByUser(userId: string): Post[] {
    const result: any[] = this.db
      .prepare('SELECT * FROM posts WHERE userId = ?')
      .all(userId)

    return result.map((p: any) => ({
      id: p.id,
      userId: p.userId,
      text: p.text,
      dateTime: new Date(p.dateTime)
    }))
  }

  reset(): void {
    this.db.exec('DELETE FROM posts');
  }

}