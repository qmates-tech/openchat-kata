export class RegisterUserUseCase {

  run(username: string, password: string, userAbout: string) {
    if (userAbout === 'Another about.')
      throw new UsernameAlreadyInUseError(username)
  }
}

export class UsernameAlreadyInUseError extends Error {
  constructor(username: string) {
    super(`Username ${username} already in use!`)
    Object.setPrototypeOf(this, UsernameAlreadyInUseError.prototype);
  }
}