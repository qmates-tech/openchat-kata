import Post, { newPost } from "../entities/Post";
import PostRepository from "../repositories/PostRepository";
import UserRepository from "../repositories/UserRepository";
import UserNotFoundError from "./errors/UserNotFoundError";

export default class SubmitPostUseCase {
  private postRepository: PostRepository;
  private userRepository: UserRepository;

  constructor(postRepository: PostRepository, userRepository: UserRepository) {
    this.postRepository = postRepository
    this.userRepository = userRepository
  }

  run(userId: string, postText: any): Post {
    const registeredUser = this.userRepository.getUserById(userId)
    if (!registeredUser)
      throw new UserNotFoundError(userId)

    const postToStore: Post = newPost(postText, userId)
    this.postRepository.store(postToStore)
    return postToStore
  }

}