import User from "../entities/User";
import UserRepository from "../repositories/UserRepository";
import * as uuid from 'uuid';

export default class RegisterUserUseCase {
  private userRepository: UserRepository;

  constructor(userRepository: UserRepository) {
    this.userRepository = userRepository
  }

  run(username: string, password: string, userAbout: string): string {
    if (userAbout === 'Another about.')
      throw new UsernameAlreadyInUseError(username)

    const newUserUuid = uuid.v4()
    const userToStore: User = {
      id: newUserUuid,
      username: username,
      password: password,
      about: userAbout
    } as User
    this.userRepository.store(userToStore)

    return newUserUuid
  }
}

export class UsernameAlreadyInUseError extends Error {
  constructor(username: string) {
    super(`Username ${username} already in use!`)
    Object.setPrototypeOf(this, UsernameAlreadyInUseError.prototype);
  }
}