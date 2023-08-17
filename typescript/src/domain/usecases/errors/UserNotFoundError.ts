export default class UserNotFoundError extends Error {
  constructor(userId: string) {
    super(`User with uuid [${userId}] not found!`);
    Object.setPrototypeOf(this, UserNotFoundError.prototype);
  }
}
