import Database from 'better-sqlite3';
import 'jest-extended';
import SQLiteUserRepository from "../../../src/database/SQLiteUserRepository";
import { RegisteredUser, UserToRegister } from "../../../src/domain/entities/User";

describe('SQLiteUserRepository', () => {

  const sqliteFilename = 'tests/integration/database/integration.test.db'
  const repository = new SQLiteUserRepository(sqliteFilename)

  beforeEach(() => {
    repository.reset()
  })

  test('retrieve users from an empty db', () => {
    expect(repository.getAll()).toEqual([])
  })

  test('store some users, retrieve them and reset', () => {
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

    repository.reset()
    expect(repository.getAll()).toHaveLength(0)
  })

  test('recognizes already used username', () => {
    const alice: UserToRegister = {
      id: "046a4497-9fd1-4b89-ad21-fd2d7562c0e0",
      username: "alice90", password: "any",
      about: "About alice user."
    }
    repository.store(alice)

    expect(repository.isUsernameAlreadyUsed("alice90")).toBeTrue()
    expect(repository.isUsernameAlreadyUsed("bob88")).toBeFalse()
  })

  describe('store method', () => {

    test('rejects user storing with invalid uuid as id', () => {
      const invalid: UserToRegister = {
        id: 'invalid',
        username: 'any', password: 'any', about: 'any'
      }
      const uuidV1: UserToRegister = {
        id: 'ea4626ec-1fe8-11ee-be56-0242ac120002', // uuid v1
        username: 'any', password: 'any', about: 'any'
      }

      expect(() => {
        repository.store(invalid)
      }).toThrowWithMessage(Error, 'Cannot store user, invalid v4 uuid id value.')

      expect(() => {
        repository.store(uuidV1)
      }).toThrowWithMessage(Error, 'Cannot store user, invalid v4 uuid id value.')
    })

    test('rejects already stored uuid value as id', () => {
      const user: UserToRegister = {
        id: 'fc05ce73-87bd-458d-9b10-57941b557be8',
        username: 'any', password: 'any', about: 'any'
      }
      repository.store(user)

      expect(() => {
        repository.store(user)
      }).toThrowWithMessage(Error, 'Cannot store user, uuid value already used.')
    })

    test('stores passwords as sha256', () => {
      const alice: UserToRegister = {
        id: '61c4b7ff-d738-4f18-9a15-1d6e3c051867',
        username: 'any', about: 'any',
        password: 'notcryptedpassword',
      }
      repository.store(alice)

      const selectResult: any = new Database(sqliteFilename)
        .prepare('SELECT password FROM users WHERE id = ?')
        .get('61c4b7ff-d738-4f18-9a15-1d6e3c051867')
      expect(selectResult.password)
        .toBe('0e667c78b4d937db64bfefbcb572e66095a1c2a41a948b519e52b09638819127'); // sha256
    })

  })

  describe('get user by id', () => {

    test('returns null with no users', () => {
      expect(repository.getUserById("7a06f0bc-910b-43f1-884c-ce292a95c5d1")).toBeNull()
      expect(repository.getUserById("invalid")).toBeNull()
    })

    test('store a user and retrieve it', () => {
      const aliceUUID = "36715ff2-3faf-4b88-8a11-127c9c4ef971"
      const alice: UserToRegister = {
        id: aliceUUID,
        username: "alice90", password: "notcryptedpassword",
        about: "About alice user.",
      }
      repository.store(alice)

      const retrievedUser = repository.getUserById(aliceUUID)

      const expected: RegisteredUser = {
        id: aliceUUID,
        username: "alice90",
        about: "About alice user."
      }
      expect(retrievedUser).toStrictEqual(expected)
    })

  })
})