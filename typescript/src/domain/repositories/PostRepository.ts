import Post from "../entities/Post"

export default interface PostRepository {
  store(post: Post): void
  reset(): void
}