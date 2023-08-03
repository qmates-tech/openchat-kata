import Database from 'better-sqlite3';
import Post from '../domain/entities/Post';
import PostRepository from "../domain/repositories/PostRepository";

export default class SQLitePostRepository implements PostRepository {

  private readonly db

  constructor(filename: string) {
    this.db = new Database(filename)
  }

  store(post: Post): void {
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