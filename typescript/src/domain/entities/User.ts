import * as uuid from 'uuid'

export type UserToRegister = {
  readonly id: string,
  readonly username: string,
  readonly password: string,
  readonly about: string
}

export function newUserToRegister(username: string, password: string, about: string): UserToRegister {
  return {
    id: uuid.v4(),
    username: username,
    password: password,
    about: about
  }
}

export type RegisteredUser = {
  readonly id: string,
  readonly username: string,
  readonly about: string
}
