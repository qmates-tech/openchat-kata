import InMemoryUserRepository from "../database/InMemoryUserRepository";
import UserRepository from "../domain/repositories/UserRepository";

export default class AppFactory {

    private static userRepositoryInstance: UserRepository | null;

    static getUserRepository(): UserRepository {
        if(!this.userRepositoryInstance)
            this.userRepositoryInstance = new InMemoryUserRepository

        return this.userRepositoryInstance
    }

    static resetRepositories(): void {
        this.userRepositoryInstance = null
    }
}