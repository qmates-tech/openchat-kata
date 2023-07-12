import Database from 'better-sqlite3';
import Post from '../domain/entities/Post';
import PostRepository from "../domain/repositories/PostRepository";

export default class SqlLitePostRepository implements PostRepository {

  private readonly db

  constructor(filename: string) {
    this.db = new Database(filename)
  }

  store(post: Post): void {
    // todo
  }

  reset(): void {
    //this.db.exec('DELETE FROM posts');
  }

}