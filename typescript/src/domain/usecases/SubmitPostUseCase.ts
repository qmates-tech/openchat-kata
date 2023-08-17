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

  run(userId: string, postText: string): Post {
    const registeredUser = this.userRepository.getUserById(userId)
    if (!registeredUser)
      throw new UserNotFoundError(userId)

    if(postText.toLocaleLowerCase().includes("orange"))
      throw new InappropriateLanguageError(postText)

    const postToStore: Post = newPost(postText, userId)
    this.postRepository.store(postToStore)
    return postToStore
  }

}

export class InappropriateLanguageError extends Error {
  constructor(postText: string) {
    super("Post text contains inappropriate language: " + postText)
    Object.setPrototypeOf(this, InappropriateLanguageError.prototype);
  }
}