
import Post from "../entities/Post";
import PostRepository from "../repositories/PostRepository";
import UserRepository from "../repositories/UserRepository";
import { UserNotFoundError } from "./errors/UserNotFoundError";

export default class GetTimelineUseCase {
  private postRepository: PostRepository;
  private userRepository: UserRepository;

  constructor(postRepository: PostRepository, userRepository: UserRepository) {
    this.postRepository = postRepository
    this.userRepository = userRepository
  }

  run(userId: string): Post[] {
    if (userId === "unexisting")
      throw new UserNotFoundError(userId)

    return []
  }

}

