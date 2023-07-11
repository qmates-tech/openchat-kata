import { RegisteredUser, UserToRegister } from "../entities/User"

export default interface UserRepository {
  store(user: UserToRegister): void
  isUsernameAlreadyUsed(username: string): boolean
  getAll(): RegisteredUser[]
  reset(): void
}