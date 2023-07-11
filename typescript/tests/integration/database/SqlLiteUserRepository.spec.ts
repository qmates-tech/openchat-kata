import Database from 'better-sqlite3'
import 'jest-extended'
import SqlLiteUserRepository from "../../../src/database/SqlLiteUserRepository"
import { RegisteredUser, UserToRegister } from "../../../src/domain/entities/User"

describe("SqlLiteUserRepository", () => {

  const sqliteFilename = 'tests/integration/database/integration.test.db'
  const repository = new SqlLiteUserRepository(sqliteFilename)

  afterEach(() => {
    const db = new Database(sqliteFilename)
    db.exec('DELETE FROM users');
  })

  test("retrieve users from an empty db", () => {
    expect(repository.getAll()).toEqual([])
  })

  test("store some users and retrieve them", () => {
    const alice: UserToRegister = {
      id: "785cd17b-5ad4-497a-84bf-e1543f31170e",
      username: "alice90", password: "any",
      about: "About alice user."
    }
    const bob: UserToRegister = {
      id: "0d6c940e-a125-4d7f-b39b-eafa64123ef9",
      username: "bob88", password: "any",
      about: "About bob user."
    }

    repository.store(alice)
    repository.store(bob)
    const users = repository.getAll()

    expect(users).toHaveLength(2)
    expect(users).toSatisfyAny((user: RegisteredUser) =>
      user.id === "785cd17b-5ad4-497a-84bf-e1543f31170e" &&
      user.username === "alice90" &&
      user.about === "About alice user."
    )
    expect(users).toSatisfyAny((user: RegisteredUser) =>
      user.id === "0d6c940e-a125-4d7f-b39b-eafa64123ef9" &&
      user.username === "bob88" &&
      user.about === "About bob user."
    )
  })

  test("recognize already used username", () => {
    const alice: UserToRegister = {
      id: "046a4497-9fd1-4b89-ad21-fd2d7562c0e0",
      username: "alice90", password: "any",
      about: "About alice user."
    }
    repository.store(alice)

    expect(repository.isUsernameAlreadyUsed("alice90")).toBeTrue()
    expect(repository.isUsernameAlreadyUsed("bob88")).toBeFalse()
  })

  //test("cannot store user with not valid uuid 4 id", () => {
  //test("password are stored as sha256", () => {

})