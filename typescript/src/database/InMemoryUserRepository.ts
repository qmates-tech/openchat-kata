import { RegisteredUser, UserToRegister } from "../domain/entities/User";
import UserRepository from "../domain/repositories/UserRepository";

export default class InMemoryUserRepository implements UserRepository {
  users: UserToRegister[] = []

  store(user: UserToRegister): void {
    this.users.push(user)
  }

  isUsernameAlreadyUsed(usernameToFind: string): boolean {
    return this.users.some((u: UserToRegister) => u.username === usernameToFind)
  }

  getAll(): RegisteredUser[] {
    return [...this.users]
  }

  reset(): void {
    this.users = []
  }
}