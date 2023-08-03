import Database from 'better-sqlite3';
import 'jest-extended';
import * as uuid from 'uuid';
import SQLitePostRepository from "../../../src/database/SQLitePostRepository";
import Post, { newPost } from "../../../src/domain/entities/Post";

describe('SQLitePostRepository', () => {

  const sqliteFilename = 'tests/integration/database/integration.test.db'
  const repository = new SQLitePostRepository(sqliteFilename)

  beforeEach(() => {
    repository.reset()
  })

  test('all posts of unexisting user id is an empty array', () => {
    expect(repository.getAllByUser(uuid.v4())).toEqual([])
  })

  test('store single post and get all by user', () => {
    const postAuthorUserId: string = uuid.v4()
    const postToStore: Post = newPost("Post text.", postAuthorUserId)

    repository.store(postToStore)
    const allByUser: Post[] = repository.getAllByUser(postAuthorUserId)

    expect(allByUser).toEqual([postToStore])
  })

})