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

  test('getAllByUser returns only user\'s posts', () => {
    const firstUserId: string = uuid.v4()
    const secondUserId: string = uuid.v4()

    repository.store(newPost("First user, first post.", firstUserId))
    repository.store(newPost("Second user, first post.", secondUserId))
    repository.store(newPost("First user, second post.", firstUserId))
    repository.store(newPost("First user, third post.", firstUserId))
    repository.store(newPost("Second user, second post.", secondUserId))

    const firstUserPosts: Post[] = repository.getAllByUser(firstUserId)
    const secondUserPosts: Post[] = repository.getAllByUser(secondUserId)

    expect(firstUserPosts).toHaveLength(3)
    expect(firstUserPosts).toSatisfyAll(p => p.userId == firstUserId)
    expect(firstUserPosts).toSatisfyAny(p => p.text == "First user, first post.")
    expect(firstUserPosts).toSatisfyAny(p => p.text == "First user, third post.")
    expect(secondUserPosts).toHaveLength(2)
    expect(secondUserPosts).toSatisfyAll(p => p.userId == secondUserId)
    expect(secondUserPosts).toSatisfyAny(p => p.text == "Second user, second post.")
  })

})