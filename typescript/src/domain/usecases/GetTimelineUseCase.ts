import Post from "../entities/Post";
import PostRepository from "../repositories/PostRepository";
import UserRepository from "../repositories/UserRepository";
import UserNotFoundError from "./errors/UserNotFoundError";

export default class GetTimelineUseCase {
  private postRepository: PostRepository;
  private userRepository: UserRepository;

  constructor(postRepository: PostRepository, userRepository: UserRepository) {
    this.postRepository = postRepository
    this.userRepository = userRepository
  }

  run(userId: string): Post[] {
    const registeredUser = this.userRepository.getUserById(userId)
    if (!registeredUser)
      throw new UserNotFoundError(userId)

    const usersPosts =  this.postRepository.getAllByUser(userId)
    return usersPosts.sort((a, b) => b.dateTime.getTime() - a.dateTime.getTime())
  }

}

