import Post, { newPost } from "../entities/Post";
import PostRepository from "../repositories/PostRepository";

export default class SubmitPostUseCase {
  private postRepository: PostRepository;

  constructor(postRepository: PostRepository) {
    this.postRepository = postRepository
  }

  run(userId: string, postText: any): Post {
    const post: Post = newPost(postText, userId)
    this.postRepository.store(post)
    return post
  }

}