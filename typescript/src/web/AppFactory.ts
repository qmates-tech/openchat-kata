import SqlLiteUserRepository from "../database/SqlLiteUserRepository";
import UserRepository from "../domain/repositories/UserRepository";

export default class AppFactory {

  private static userRepositoryInstance: UserRepository | null;

  static getUserRepository(): UserRepository {
    if (!this.userRepositoryInstance)
      this.userRepositoryInstance = new SqlLiteUserRepository('src/database/production.db')

    return this.userRepositoryInstance
  }

  static resetRepositories(): void {
    this.userRepositoryInstance?.reset()
  }
}