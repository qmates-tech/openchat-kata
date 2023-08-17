export default class UserNotFoundError extends Error {
  constructor(userId: string) {
    super(`User with id ${userId} not found!`);
    Object.setPrototypeOf(this, UserNotFoundError.prototype);
  }
}
