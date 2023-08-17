import SQLitePostRepository from "./database/SQLitePostRepository";
import SQLiteUserRepository from "./database/SQLiteUserRepository";
import PostRepository from "./domain/repositories/PostRepository";
import UserRepository from "./domain/repositories/UserRepository";
import GetAllUsersUseCase from "./domain/usecases/GetAllUsersUseCase";
import GetTimelineUseCase from "./domain/usecases/GetTimelineUseCase";
import RegisterUserUseCase from "./domain/usecases/RegisterUserUseCase";
import SubmitPostUseCase from "./domain/usecases/SubmitPostUseCase";

export default class AppFactory {
  private static userRepositoryInstance: UserRepository | null;
  private static postRepositoryInstance: PostRepository | null;

  static buildGetAllUsersUseCase() {
    return new GetAllUsersUseCase(AppFactory.getUserRepository())
  }

  static buildRegisterUserUseCase() {
    return new RegisterUserUseCase(AppFactory.getUserRepository())
  }

  static buildGetTimelineUseCase(): GetTimelineUseCase {
    return new GetTimelineUseCase(
      AppFactory.getPostRepository(),
      AppFactory.getUserRepository()
    )
  }

  static buildSubmitPostUseCase(): SubmitPostUseCase {
    return new SubmitPostUseCase(
      AppFactory.getPostRepository(),
      AppFactory.getUserRepository()
    )
  }

  static getUserRepository(): UserRepository {
    if (!this.userRepositoryInstance)
      this.userRepositoryInstance = new SQLiteUserRepository('src/database/production.db')

    return this.userRepositoryInstance
  }

  static getPostRepository(): PostRepository {
    if (!this.postRepositoryInstance)
      this.postRepositoryInstance = new SQLitePostRepository('src/database/production.db')

    return this.postRepositoryInstance
  }

  static resetRepositories(): void {
    this.userRepositoryInstance?.reset()
    this.postRepositoryInstance?.reset()
  }
}