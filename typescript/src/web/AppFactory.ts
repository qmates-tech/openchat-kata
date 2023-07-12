import SqlLitePostRepository from "../database/SqlLitePostRepository";
import SqlLiteUserRepository from "../database/SqlLiteUserRepository";
import PostRepository from "../domain/repositories/PostRepository";
import UserRepository from "../domain/repositories/UserRepository";

export default class AppFactory {

  private static userRepositoryInstance: UserRepository | null;
  private static postRepositoryInstance: PostRepository | null;

  static getUserRepository(): UserRepository {
    if (!this.userRepositoryInstance)
      this.userRepositoryInstance = new SqlLiteUserRepository('src/database/production.db')

    return this.userRepositoryInstance
  }

  static getPostRepository(): PostRepository {
    if (!this.postRepositoryInstance)
      this.postRepositoryInstance = new SqlLitePostRepository('src/database/production.db')

    return this.postRepositoryInstance
  }

  static resetRepositories(): void {
    this.userRepositoryInstance?.reset()
    this.postRepositoryInstance?.reset()
  }
}