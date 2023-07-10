import User from "../entities/User"

export default interface UserRepository {
    store(user: User): void
    isUsernameAlreadyUsed(username: string): boolean
    getAll(): User[]
}