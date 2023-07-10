import User from "../entities/User";
import UserRepository from "../repositories/UserRepository";

export default class GetAllUsersUseCase {
  private userRepository: UserRepository;

  constructor(userRepository: UserRepository) {
    this.userRepository = userRepository
  }

  run(): User[] {
    return this.userRepository.getAll();
  }
}