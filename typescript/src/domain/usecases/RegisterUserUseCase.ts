import { newUserToRegister } from '../entities/User';
import UserRepository from "../repositories/UserRepository";

export default class RegisterUserUseCase {
  private userRepository: UserRepository;

  constructor(userRepository: UserRepository) {
    this.userRepository = userRepository
  }

  run(username: string, password: string, userAbout: string): string {
    if (this.userRepository.isUsernameAlreadyUsed(username))
      throw new UsernameAlreadyInUseError(username)

    const userToStore = newUserToRegister(username, password, userAbout)
    this.userRepository.store(userToStore)

    return userToStore.id
  }
}

export class UsernameAlreadyInUseError extends Error {
  constructor(username: string) {
    super(`Username ${username} already in use!`)
    Object.setPrototypeOf(this, UsernameAlreadyInUseError.prototype);
  }
}