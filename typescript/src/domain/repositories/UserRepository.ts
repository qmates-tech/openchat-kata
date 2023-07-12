import { RegisteredUser, UserToRegister } from "../entities/User"

export default interface UserRepository {
  store(user: UserToRegister): void
  isUsernameAlreadyUsed(username: string): boolean
  getAll(): RegisteredUser[]
  getUserById(userId: string): RegisteredUser | null
  reset(): void
}