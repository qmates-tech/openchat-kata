import User from "../domain/entities/User";
import UserRepository from "../domain/repositories/UserRepository";

export default class InMemoryUserRepository implements UserRepository {
    users: User[] = []

    store(user: User): void {
        this.users.push(user)
    }

    isUsernameAlreadyUsed(usernameToFind: string): boolean {
        return this.users.some((u: User) => u.username === usernameToFind)
    }

    getAll(): User[] {
        return [...this.users]
    }

}